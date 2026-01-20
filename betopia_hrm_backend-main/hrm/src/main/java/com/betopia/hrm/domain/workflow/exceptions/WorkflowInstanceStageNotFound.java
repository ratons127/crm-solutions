package com.betopia.hrm.domain.workflow.exceptions;

public class WorkflowInstanceStageNotFound extends RuntimeException {
    public WorkflowInstanceStageNotFound(String message) {
        super(message);
    }
}
