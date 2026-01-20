package com.betopia.hrm.domain.employee.exception.employmentstatus;

public class EmploymentStatusAlreadyExistException extends RuntimeException {
    public EmploymentStatusAlreadyExistException(String message) {
        super(message);
    }
}
