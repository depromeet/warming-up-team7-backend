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
    public static class RegisterRequestOne implements PasswordAware {
        private String username;
        private String nickname;
        private String password;
        private String passwordForValidation;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequestTwo {
        private String username;
        private String country;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RegisterRequestThree {
        private String username;
        private int profileImageNumber;
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
