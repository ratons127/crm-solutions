package com.betopia.hrm.domain.base.response;

import java.util.List;

public class GlobalResponse {

    private List<String> errors;
    private Object data;
    private String message;
    private boolean success;
    private int status;

    public GlobalResponse(List<String> errors, Object data, String message, boolean success, int status) {
        this.errors = errors;
        this.data = data;
        this.message = message;
        this.success = success;
        this.status = status;
    }

    // Factory methods
    public static GlobalResponse success(Object data, String message, int status) {
        return new GlobalResponse(null, data, message, true, status);
    }

    public static GlobalResponse error(List<String> errors, String message, int status) {
        return new GlobalResponse(errors, null, message, false, status);
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
