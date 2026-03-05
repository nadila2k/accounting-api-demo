package com.nadila.accounting_app_demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Permission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Unique permission key, e.g. "audit:read".
     * Spring Security's @PreAuthorize will check against this value.
     */
    @Column(nullable = false, unique = true)
    private String name;

    /** Human-readable label shown in admin UIs. */
    @Column
    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<User> users = new HashSet<>();
}

