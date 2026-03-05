package com.nadila.accounting_app_demo.controller;

import com.nadila.accounting_app_demo.dto.request.LoginRequest;
import com.nadila.accounting_app_demo.dto.request.RegisterRequest;
import com.nadila.accounting_app_demo.dto.response.ApiResponse;
import com.nadila.accounting_app_demo.dto.response.AuthResponse;
import com.nadila.accounting_app_demo.dto.response.UserResponse;
import com.nadila.accounting_app_demo.entity.Role;
import com.nadila.accounting_app_demo.entity.User;
import com.nadila.accounting_app_demo.exception.BadRequestException;
import com.nadila.accounting_app_demo.exception.ResourceAlreadyExistsException;
import com.nadila.accounting_app_demo.exception.ResourceNotFoundException;
import com.nadila.accounting_app_demo.repository.RoleRepository;
import com.nadila.accounting_app_demo.repository.UserRepository;
import com.nadila.accounting_app_demo.security.jwtUtils.JwtTokenProvider;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    // ─────────────────────────────────────────────
    // LOGIN
    // ─────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtTokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found: " + request.getUsername()
                ));

        log.info("User logged in: {}", request.getUsername());

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole().getName())
                .build();

        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }

    // ─────────────────────────────────────────────
    // REGISTER
    // ─────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException(
                    "Username already exists: " + request.getUsername()
            );
        }

        // ✅ Find by ID instead of name
        Role role = roleRepository.findById(request.getRoleId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found with id: " + request.getRoleId()
                ));

        if (role.getName().equals("ROLE_SUPER_ADMIN")) {
            throw new BadRequestException(
                    "Cannot assign ROLE_SUPER_ADMIN during registration"
            );
        }

        User savedUser = userRepository.save(
                User.builder()
                        .username(request.getUsername())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(role)
                        .build()
        );

        log.info("New user registered: {} with role: {}",
                savedUser.getUsername(), role.getName());

        UserResponse userResponse = UserResponse.builder()
                .id(savedUser.getId())
                .username(savedUser.getUsername())
                .role(savedUser.getRole().getName())
                .createdAt(savedUser.getCreatedAt())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        HttpStatus.CREATED,
                        "User created successfully",
                        userResponse
                ));
    }
}
