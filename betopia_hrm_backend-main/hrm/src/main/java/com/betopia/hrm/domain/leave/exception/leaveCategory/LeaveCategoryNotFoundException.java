package com.betopia.hrm.domain.leave.exception.leaveCategory;

public class LeaveCategoryNotFoundException extends RuntimeException {
    public LeaveCategoryNotFoundException(String message) {
        super(message);
    }
}
