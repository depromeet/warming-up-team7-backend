package com.warmup.familytalk.auth.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.auth.service.PasswordAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import reactor.core.publisher.Mono;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "username"})
public class User implements UserDetails, PasswordAware {
    private long userId;
    private String username;
    private String nickname;
    private String password;
    private String country;

    private int profileImageNumber;

    private Boolean enabled;
    private Role role;

    public String getProfileImageUrl() {
        return toS3ProfileUrl(profileImageNumber);
    }

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
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
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

    private String toS3ProfileUrl(int imageId) {
        return String.format("https://team7wp.s3.ap-northeast-2.amazonaws.com/profileImage/%s.png", imageId);
    }

    @Data
    public static class Response {
        private long userId;
        private String username;
        private String nickname;
        private String country;
        private String profileImageUrl;

        public static Mono<Response> of(final Mono<User> byUserId) {
            return byUserId.map(user -> {
                Response response = new Response();
                response.setUserId(user.getUserId());
                response.setUsername(user.getUsername());
                response.setNickname(user.getNickname());
                response.setCountry(user.getCountry());
                response.setProfileImageUrl(user.getProfileImageUrl());
                return response;
            });
        }
    }
}
