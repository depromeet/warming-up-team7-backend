package com.warmup.familytalk.auth.model;

import java.util.Arrays;

import com.warmup.familytalk.auth.service.PasswordAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

// TODO: use spring validator
// e.g. @NotBlank
public class Auth {
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request implements PasswordAware {
        private String username;
        private String password;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequest implements PasswordAware {
        private String username;
        private String password;
        private String passwordForValidation;

        // TODO: not just using String class
        private String region;

        public boolean isValidPassword() {
            return StringUtils.equals(password, passwordForValidation);
        }

        public boolean isValidUsername() {
            // TODO: implements;
            return true;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Response {
        private String token;
    }

    public static class Factory {
        public static Response response(JwtToken jwtToken) {
            return new Response(jwtToken.toTokenString());
        }
    }
}
