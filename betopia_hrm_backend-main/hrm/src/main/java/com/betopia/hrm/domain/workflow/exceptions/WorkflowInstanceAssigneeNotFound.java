package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowInstanceAssigneeNotFound extends RuntimeException {
    public WorkflowInstanceAssigneeNotFound(String message) {
        super(message);
    }
}
