package com.warmup.familytalk.auth.model;

public enum Role {
    ROLE_USER, ROLE_ADMIN, ROLE_TEST;

    public static Role parse(final String input) {
        System.out.println(input);
        return valueOf(input);
    }
}
