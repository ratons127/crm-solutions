package com.betopia.hrm.domain.leave.exception.leaveyear;

public class LeaveYearAlreadyExistException extends RuntimeException {
    public LeaveYearAlreadyExistException(String message) {
        super(message);
    }
}
