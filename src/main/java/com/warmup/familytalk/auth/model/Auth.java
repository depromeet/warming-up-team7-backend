package com.warmup.familytalk.auth.model;

import com.warmup.familytalk.auth.service.PasswordAware;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        private String nickname;
        private String password;
        private String passwordForValidation;
        private String country;
        private String profileImageNumber;

        // TODO: not just using String class
        private String region;
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
