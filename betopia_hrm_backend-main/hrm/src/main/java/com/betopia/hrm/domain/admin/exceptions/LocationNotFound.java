package com.betopia.hrm.domain.admin.exceptions;

public class LocationNotFound extends RuntimeException{
    public LocationNotFound(String message) {
        super(message);
    }
}
