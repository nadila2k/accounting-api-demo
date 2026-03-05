package com.nadila.accounting_app_demo.controller;

import com.nadila.accounting_app_demo.entity.Permission;
import com.nadila.accounting_app_demo.service.PermissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/admin/permissions")
@RequiredArgsConstructor
@PreAuthorize("hasRole('SUPER_ADMIN')")   // class-level guard
public class PermissionManagementController {

    private final PermissionService permissionService;

    // ── GET /api/v1/admin/permissions ─────────────────────────────────────────
    /** List every permission that exists in the system. */
    @GetMapping
    public ResponseEntity<List<Permission>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    // ── GET /api/v1/admin/permissions/users/{userId} ──────────────────────────
    /** Get the current permission set of a specific user. */
    @GetMapping("/users/{userId}")
    public ResponseEntity<Set<String>> getUserPermissions(@PathVariable Long userId) {
        return ResponseEntity.ok(permissionService.getUserPermissions(userId));
    }

    // ── PUT /api/v1/admin/permissions/users/{userId} ──────────────────────────
    /**
     * REPLACE a user's entire permission set.
     * Body: ["audit:read", "finance:create", "finance:read"]
     */
    @PutMapping("/users/{userId}")
    public ResponseEntity<String> setUserPermissions(
            @PathVariable Long userId,
            @RequestBody Set<String> permissionKeys) {

        permissionService.setUserPermissions(userId, permissionKeys);
        return ResponseEntity.ok("Permissions updated for user " + userId);
    }

    // ── POST /api/v1/admin/permissions/users/{userId}/grant ───────────────────
    /**
     * GRANT additional permissions to a user (additive, keeps existing ones).
     * Body: ["payment:read"]
     */
    @PostMapping("/users/{userId}/grant")
    public ResponseEntity<String> grantPermissions(
            @PathVariable Long userId,
            @RequestBody Set<String> permissionKeys) {

        permissionService.grantPermissions(userId, permissionKeys);
        return ResponseEntity.ok("Permissions granted to user " + userId);
    }

    // ── POST /api/v1/admin/permissions/users/{userId}/revoke ──────────────────
    /**
     * REVOKE specific permissions from a user.
     * Body: ["audit:delete"]
     */
    @PostMapping("/users/{userId}/revoke")
    public ResponseEntity<String> revokePermissions(
            @PathVariable Long userId,
            @RequestBody Set<String> permissionKeys) {

        permissionService.revokePermissions(userId, permissionKeys);
        return ResponseEntity.ok("Permissions revoked from user " + userId);
    }
}
