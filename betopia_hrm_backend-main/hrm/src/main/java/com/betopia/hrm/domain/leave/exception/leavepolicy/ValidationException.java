package com.betopia.hrm.domain.leave.exception.leavepolicy;

public class ValidationException extends RuntimeException {
    private final String errorCode;

    public ValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
