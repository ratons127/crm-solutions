package com.betopia.hrm.domain.attendance.exception;

public class ShiftCategoryNotFoundException extends RuntimeException {
    public ShiftCategoryNotFoundException(String message) {
        super(message);
    }
}
