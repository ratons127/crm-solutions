package com.betopia.hrm.domain.leave.exception.leavepolicy;

public class LeavePolicyAlreadyExistException extends RuntimeException {
    public LeavePolicyAlreadyExistException(String message) {
        super(message);
    }
}
