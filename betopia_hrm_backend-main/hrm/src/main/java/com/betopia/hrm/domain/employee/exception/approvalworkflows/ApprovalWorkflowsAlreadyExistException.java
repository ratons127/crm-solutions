package com.betopia.hrm.domain.employee.exception.approvalworkflows;

public class ApprovalWorkflowsAlreadyExistException extends RuntimeException {
    public ApprovalWorkflowsAlreadyExistException(String message) {
        super(message);
    }
}
