package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowInstanceNotFound extends RuntimeException {
    public WorkflowInstanceNotFound(String message) {
        super(message);
    }
}
