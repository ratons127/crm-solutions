package com.betopia.hrm.domain.workflow.exceptions;

public class StageApproverNotFound extends RuntimeException {
    public StageApproverNotFound(String message) {
        super(message);
    }
}
