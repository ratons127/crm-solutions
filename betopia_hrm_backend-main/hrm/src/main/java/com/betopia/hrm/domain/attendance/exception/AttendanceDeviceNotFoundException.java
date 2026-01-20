package com.betopia.hrm.domain.attendance.exception;

public class AttendanceDeviceNotFoundException extends RuntimeException {
    public AttendanceDeviceNotFoundException(String message) {
        super(message);
    }
}
