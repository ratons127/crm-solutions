package com.betopia.hrm.domain.employee.exception.transferrequest;

public class TransferRequestNotFoundException extends RuntimeException {
    public TransferRequestNotFoundException(String message) {
        super(message);
    }
}
