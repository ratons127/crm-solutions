package com.betopia.hrm.domain.users.exception.user;

public class PasswordPolicyNotFound extends RuntimeException {

    public PasswordPolicyNotFound(String message) {
        super(message);
    }
}
