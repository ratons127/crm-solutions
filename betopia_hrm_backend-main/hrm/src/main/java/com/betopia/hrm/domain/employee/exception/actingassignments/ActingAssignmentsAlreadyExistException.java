package com.betopia.hrm.domain.employee.exception.actingassignments;

public class ActingAssignmentsAlreadyExistException extends RuntimeException {
    public ActingAssignmentsAlreadyExistException(String message) {
        super(message);
    }
}
