package com.betopia.hrm.domain.leave.exception.leavepolicy;

public class LeavePolicyNotFoundException extends RuntimeException {
    public LeavePolicyNotFoundException(String message) {
        super(message);
    }
}
