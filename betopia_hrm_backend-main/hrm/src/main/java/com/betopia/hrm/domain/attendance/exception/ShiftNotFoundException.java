package com.betopia.hrm.domain.attendance.exception;

public class ShiftNotFoundException extends RuntimeException {
    public ShiftNotFoundException(String message) {
        super(message);
    }
}
