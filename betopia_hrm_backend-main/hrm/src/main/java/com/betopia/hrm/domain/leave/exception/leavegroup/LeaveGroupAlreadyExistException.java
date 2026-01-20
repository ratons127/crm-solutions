package com.betopia.hrm.domain.leave.exception.leavegroup;

public class LeaveGroupAlreadyExistException extends RuntimeException {
    public LeaveGroupAlreadyExistException(String message) {
        super(message);
    }
}
