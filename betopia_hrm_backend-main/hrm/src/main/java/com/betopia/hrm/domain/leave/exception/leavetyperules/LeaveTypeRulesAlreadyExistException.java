package com.betopia.hrm.domain.leave.exception.leavetyperules;

public class LeaveTypeRulesAlreadyExistException extends RuntimeException {
    public LeaveTypeRulesAlreadyExistException(String message) {
        super(message);
    }
}
