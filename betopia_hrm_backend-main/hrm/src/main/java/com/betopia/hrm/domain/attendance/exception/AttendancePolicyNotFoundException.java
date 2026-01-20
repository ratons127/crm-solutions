package com.betopia.hrm.domain.attendance.exception;

public class AttendancePolicyNotFoundException extends RuntimeException {
    public AttendancePolicyNotFoundException(String message) {
        super(message);
    }
}
