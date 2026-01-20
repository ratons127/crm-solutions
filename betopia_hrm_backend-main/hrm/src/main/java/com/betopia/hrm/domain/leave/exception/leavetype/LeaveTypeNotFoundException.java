package com.betopia.hrm.domain.leave.exception.leavetype;

public class LeaveTypeNotFoundException extends RuntimeException {
    public LeaveTypeNotFoundException(String message) {
        super(message);
    }
}
