package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowAuditLogNotFound extends RuntimeException {
    public WorkflowAuditLogNotFound(String message) {
        super(message);
    }
}
