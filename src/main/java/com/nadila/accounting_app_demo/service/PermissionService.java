package com.nadila.accounting_app_demo.service;

import com.nadila.accounting_app_demo.entity.Permission;
import com.nadila.accounting_app_demo.entity.User;
import com.nadila.accounting_app_demo.repository.PermissionRepository;
import com.nadila.accounting_app_demo.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final UserRepository userRepository;
    private final PermissionRepository permissionRepository;

    // ── read ──────────────────────────────────────────────────────────────────

    /** All permissions that exist in the system. */
    public List<Permission> getAllPermissions() {
        return permissionRepository.findAll();
    }

    /** Permissions currently assigned to a specific user. */
    public Set<String> getUserPermissions(Long userId) {
        User user = findUser(userId);
        return user.getPermissions().stream()
                .map(Permission::getName)
                .collect(Collectors.toSet());
    }

    // ── write ─────────────────────────────────────────────────────────────────

    /**
     * REPLACE the user's permission set with the supplied list.
     * Idempotent: calling twice with the same list is safe.
     */
    @Transactional
    public void setUserPermissions(Long userId, Set<String> permissionKeys) {
        User user = findUser(userId);

        List<Permission> newPerms = permissionRepository.findAllByNameIn(permissionKeys);

        if (newPerms.size() != permissionKeys.size()) {
            Set<String> found = newPerms.stream().map(Permission::getName).collect(Collectors.toSet());
            Set<String> unknown = permissionKeys.stream()
                    .filter(k -> !found.contains(k))
                    .collect(Collectors.toSet());
            throw new IllegalArgumentException("Unknown permissions: " + unknown);
        }

        // Clear existing and replace
        user.getPermissions().clear();
        user.getPermissions().addAll(newPerms);
        userRepository.save(user);
    }

    /**
     * GRANT additional permissions without touching existing ones.
     */
    @Transactional
    public void grantPermissions(Long userId, Set<String> permissionKeys) {
        User user = findUser(userId);
        List<Permission> toGrant = permissionRepository.findAllByNameIn(permissionKeys);
        user.getPermissions().addAll(toGrant);
        userRepository.save(user);
    }

    /**
     * REVOKE specific permissions from a user.
     */
    @Transactional
    public void revokePermissions(Long userId, Set<String> permissionKeys) {
        User user = findUser(userId);
        user.getPermissions().removeIf(p -> permissionKeys.contains(p.getName()));
        userRepository.save(user);
    }

    // ── helpers ───────────────────────────────────────────────────────────────

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
    }
}

