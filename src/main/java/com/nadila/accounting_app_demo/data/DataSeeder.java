package com.nadila.accounting_app_demo.data;

import com.nadila.accounting_app_demo.entity.Permission;
import com.nadila.accounting_app_demo.entity.Role;        // ✅ Add import
import com.nadila.accounting_app_demo.entity.User;        // ✅ Add import
import com.nadila.accounting_app_demo.enums.AppPermission;
import com.nadila.accounting_app_demo.repository.PermissionRepository;
import com.nadila.accounting_app_demo.repository.RoleRepository;
import com.nadila.accounting_app_demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;                                    // ✅ Add import
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements ApplicationRunner {

    private final RoleRepository       roleRepository;
    private final UserRepository       userRepository;
    private final PermissionRepository permissionRepository;
    private final PasswordEncoder      passwordEncoder;

    // ── Role constants ────────────────────────────────────────────────────────
    public static final String ROLE_SUPER_ADMIN          = "ROLE_SUPER_ADMIN";
    public static final String ROLE_ADMIN                = "ROLE_ADMIN";
    public static final String ROLE_ACCOUNTANT           = "ROLE_ACCOUNTANT";
    public static final String ROLE_ASSISTANT_ACCOUNTANT = "ROLE_ASSISTANT_ACCOUNTANT";
    public static final String ROLE_AUDITOR              = "ROLE_AUDITOR";
    public static final String ROLE_ASSISTANT_AUDITOR    = "ROLE_ASSISTANT_AUDITOR";

    // ── Seed users ────────────────────────────────────────────────────────────
    private static final String SUPER_ADMIN_USERNAME = "superadmin";
    private static final String SUPER_ADMIN_PASSWORD = "SuperAdmin@123";
    private static final String ADMIN_USERNAME       = "admin";
    private static final String ADMIN_PASSWORD       = "Admin@123";

    // Demo users to illustrate user-level permissions (not role-level)
    private static final String USER_A_USERNAME = "user_a";
    private static final String USER_A_PASSWORD = "UserA@123";
    private static final String USER_B_USERNAME = "user_b";
    private static final String USER_B_PASSWORD = "UserB@123";

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        seedRoles();
        seedPermissions();
        seedUsers();
    }

    // ── 1. Roles ──────────────────────────────────────────────────────────────
    private void seedRoles() {
        log.info("━━━━ SEEDING ROLES ━━━━");
        List.of(
                ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_ACCOUNTANT,
                ROLE_ASSISTANT_ACCOUNTANT, ROLE_AUDITOR, ROLE_ASSISTANT_AUDITOR
        ).forEach(roleName -> {
            if (!roleRepository.existsByName(roleName)) {
                roleRepository.save(Role.builder().name(roleName).build());
                log.info("✅ Role created: {}", roleName);
            } else {
                log.info("⏭️  Role exists : {}", roleName);
            }
        });
    }

    // ── 2. Permissions ────────────────────────────────────────────────────────
    private void seedPermissions() {
        log.info("━━━━ SEEDING PERMISSIONS ━━━━");
        for (AppPermission ap : AppPermission.values()) {
            if (!permissionRepository.existsByName(ap.key())) {
                permissionRepository.save(
                        Permission.builder()
                                .name(ap.key())
                                .description(ap.description())
                                .build()
                );
                log.info("✅ Permission created: {}", ap.key());
            } else {
                log.info("⏭️  Permission exists : {}", ap.key());
            }
        }
    }

    // ── 3. Users ──────────────────────────────────────────────────────────────
    private void seedUsers() {
        log.info("━━━━ SEEDING USERS ━━━━");

        // superadmin – no explicit permissions needed (bypassed via hasRole check)
        seedUser(SUPER_ADMIN_USERNAME, SUPER_ADMIN_PASSWORD, ROLE_SUPER_ADMIN, Set.of());

        // admin – full access to everything
        seedUser(ADMIN_USERNAME, ADMIN_PASSWORD, ROLE_ADMIN,
                Arrays.stream(AppPermission.values())
                        .map(AppPermission::key)
                        .collect(Collectors.toSet()));

        // user_a – can do everything on audit AND finance
        seedUser(USER_A_USERNAME, USER_A_PASSWORD, ROLE_ACCOUNTANT, Set.of(
                AppPermission.AUDIT_READ.key(),
                AppPermission.AUDIT_CREATE.key(),
                AppPermission.AUDIT_UPDATE.key(),
                AppPermission.AUDIT_DELETE.key(),
                AppPermission.FINANCE_READ.key(),
                AppPermission.FINANCE_CREATE.key(),
                AppPermission.FINANCE_UPDATE.key(),
                AppPermission.FINANCE_DELETE.key()
        ));

        // user_b – can only read audits + read/create finance
        seedUser(USER_B_USERNAME, USER_B_PASSWORD, ROLE_ASSISTANT_ACCOUNTANT, Set.of(
                AppPermission.AUDIT_READ.key(),
                AppPermission.FINANCE_READ.key(),
                AppPermission.FINANCE_CREATE.key()
        ));
    }

    private void seedUser(String username, String password, String roleName,
                          Set<String> permissionKeys) {
        if (userRepository.existsByUsername(username)) {
            log.info("⏭️  User exists: {}", username);
            return;
        }

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new IllegalStateException(roleName + " role not found"));

        Set<Permission> permissions = new HashSet<>(
                permissionRepository.findAllByNameIn(permissionKeys)
        );

        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(role)
                .permissions(permissions)
                .build();

        userRepository.save(user);
        log.info("✅ User created: {} ({}) with {} permission(s)",
                username, roleName, permissions.size());
    }
}
