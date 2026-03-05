package com.nadila.accounting_app_demo.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    /** Role = coarse-grained grouping (ROLE_ADMIN, ROLE_ACCOUNTANT …) */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    /**
     * Fine-grained, per-user permissions.
     * These are checked by @PreAuthorize INDEPENDENTLY of the role,
     * so two users with the same role can have different access.
     */
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_permissions",
            joinColumns        = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // ── convenience helpers ───────────────────────────────────────────────────

    public void addPermission(Permission permission) {
        this.permissions.add(permission);
        permission.getUsers().add(this);
    }

    public void removePermission(Permission permission) {
        this.permissions.remove(permission);
        permission.getUsers().remove(this);
    }
}
