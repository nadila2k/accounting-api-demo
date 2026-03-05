package com.nadila.accounting_app_demo.repository;

import com.nadila.accounting_app_demo.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;
import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Long> {

    Optional<Permission> findByName(String name);

    boolean existsByName(String name);

    /** Bulk-fetch by key names – used when assigning a list of permissions to a user. */
    List<Permission> findAllByNameIn(Set<String> names);
}
