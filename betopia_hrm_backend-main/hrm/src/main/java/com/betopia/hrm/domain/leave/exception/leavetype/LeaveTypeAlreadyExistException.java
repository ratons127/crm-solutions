package com.betopia.hrm.domain.leave.exception.leavetype;

public class LeaveTypeAlreadyExistException extends RuntimeException {
    public LeaveTypeAlreadyExistException(String message) {
        super(message);
    }
}
