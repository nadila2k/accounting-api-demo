package com.nadila.accounting_app_demo.security;

import com.nadila.accounting_app_demo.entity.Role;
import com.nadila.accounting_app_demo.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        // 1. Coarse-grained role  (e.g. ROLE_ADMIN)
        Role role = user.getRole();
        authorities.add(new SimpleGrantedAuthority(role.getName()));

        // 2. Fine-grained per-user permissions  (e.g. audit:read, finance:create)
        user.getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.getName()))
        );

        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // customize if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // customize if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // customize if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // customize if needed
    }
}
