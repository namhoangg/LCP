package com.lcp.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class UserDetailsCustom implements UserDetails {
    private Long userId;

    private String username;

    private String password;

    private Boolean isClient;

    private Boolean isSuperAdmin;

    private Long clientId; // only for client

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsCustom(UserClaims userClaims) {

        List<GrantedAuthority> authorities = new ArrayList<>();
        this.authorities = authorities;
        this.userId = userClaims.getUserId();
        this.username = userClaims.getEmail();
        this.isClient = userClaims.getIsClient();
        this.isSuperAdmin = userClaims.getIsSuperAdmin();
        this.clientId = userClaims.getClientId();
    }

    public static UserDetailsCustom getCurrentUser() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.isAuthenticated()) {
                return null;
            }

            return (UserDetailsCustom) authentication.getPrincipal();
        } catch (Exception ex) {
            return null;
        }
    }

    public static Long getCurrentUserId() {
        return Objects.requireNonNull(getCurrentUser()).getUserId();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}
