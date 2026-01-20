package com.betopia.hrm.domain.company.exception;

public class TeamDeleteNotAllowedException extends RuntimeException {
    public TeamDeleteNotAllowedException(String message) {
        super(message);
    }
}
