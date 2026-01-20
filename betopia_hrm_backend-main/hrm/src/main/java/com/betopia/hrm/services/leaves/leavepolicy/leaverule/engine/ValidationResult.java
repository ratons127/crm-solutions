package com.betopia.hrm.services.leaves.leavepolicy.leaverule.engine;

public class ValidationResult {
    private final boolean valid;
    private final String errorCode;
    private final String message;

    private ValidationResult(boolean valid, String errorCode, String message) {
        this.valid = valid;
        this.errorCode = errorCode;
        this.message = message;
    }

    public static ValidationResult ok() {
        return new ValidationResult(true, null, null);
    }

    public static ValidationResult fail(String code, String msg) {
        return new ValidationResult(false, code, msg);
    }

    // getters


    public boolean isValid() {
        return valid;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }
}
