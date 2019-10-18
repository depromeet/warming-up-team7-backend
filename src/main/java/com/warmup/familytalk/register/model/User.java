package com.warmup.familytalk.register.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.auth.service.PasswordAware;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "username"})
public class User implements UserDetails, PasswordAware {

    private long userId;
    private String username;
    private String password;

    @Getter
    @Setter
    private Boolean enabled;

    @Getter
    @Setter
    private List<Role> roles;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream()
                         .map(authority -> new SimpleGrantedAuthority(authority.name()))
                         .collect(Collectors.toList());
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @JsonProperty
    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equalsIn(long userId) {
        return this.userId == userId;
    }

    public boolean equalsIn(String username) {
        return StringUtils.equals(this.username, username);
    }
}
