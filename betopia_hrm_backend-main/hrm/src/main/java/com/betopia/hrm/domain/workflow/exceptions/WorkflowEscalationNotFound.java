package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowEscalationNotFound extends RuntimeException {
    public WorkflowEscalationNotFound(String message) {
        super(message);
    }
}
