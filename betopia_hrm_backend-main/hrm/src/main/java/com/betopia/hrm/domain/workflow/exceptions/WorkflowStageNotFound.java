package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowStageNotFound extends RuntimeException {
    public WorkflowStageNotFound(String message) {
        super(message);
    }
}
