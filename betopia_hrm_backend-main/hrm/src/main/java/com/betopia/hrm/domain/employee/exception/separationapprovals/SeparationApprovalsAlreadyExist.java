package com.betopia.hrm.domain.employee.exception.separationapprovals;

public class SeparationApprovalsAlreadyExist extends RuntimeException {
    public SeparationApprovalsAlreadyExist(String message) {
        super(message);
    }
}
