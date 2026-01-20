package com.betopia.hrm.domain.company.exception;

public class NotificationEventsAlreadyExistException extends RuntimeException {
    public NotificationEventsAlreadyExistException(String message) {
        super(message);
    }
}
