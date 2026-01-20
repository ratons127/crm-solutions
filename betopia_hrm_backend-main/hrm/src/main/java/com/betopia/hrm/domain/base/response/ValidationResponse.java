package com.betopia.hrm.domain.base.response;

import java.util.Map;

public class ValidationResponse {

    private Map<String, Object> errors;
    private String message;
    private int statusCode;

    public ValidationResponse(Map<String, Object> errors, String message, int statusCode)
    {
        this.errors = errors;
        this.message = message;
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getErrors() {
        return errors;
    }

    public void setErrors(Map<String, Object> errors) {
        this.errors = errors;
    }
}
