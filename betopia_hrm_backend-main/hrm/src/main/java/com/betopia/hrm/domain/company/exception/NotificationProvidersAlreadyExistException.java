package com.betopia.hrm.domain.company.exception;

public class NotificationProvidersAlreadyExistException extends RuntimeException {
    public NotificationProvidersAlreadyExistException(String message) {
        super(message);
    }
}
