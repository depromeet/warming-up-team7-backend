package com.warmup.familytalk.auth.model;

import java.util.Collection;
import java.util.Collections;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.warmup.familytalk.auth.service.PasswordAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import reactor.core.publisher.Mono;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"userId", "username"})
public class User implements UserDetails, PasswordAware {
    //
    // 아래 필드 이름은 user.csv에서 key 값으로 사용되고 있으므로, 변경에 주의를 요한다.
    //
    private long userId;
    private String username;
    private String password;
    private String country;

    private String profileImageNumber;

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

    public boolean equalsIn(long userId) {
        return this.userId == userId;
    }

    public boolean equalsIn(String username) {
        return StringUtils.equals(this.username, username);
    }

    public String toCsvFormat() {
        return String.format("%s, %s, %s, %s, %s",
                             userId,
                             username,
                             password,
                             enabled,
                             role.name());
    }

    private String toS3ProfileUrl(String imageId) {
        return String.format("https://team7wp.s3.ap-northeast-2.amazonaws.com/profileImage/%s.png", imageId);
    }

    @Data
    public static class Response {
        private long userId;
        private String username;
        private String country;
        private String profileImageUrl;

        public static Mono<Response> of(final Mono<User> byUserId) {
            return byUserId.map(user -> {
                Response response = new Response();
                response.setUserId(user.getUserId());
                response.setUsername(user.getUsername());
                response.setCountry(user.getCountry());
                response.setProfileImageUrl(user.getProfileImageUrl());
                return response;
            });
        }
    }
}
