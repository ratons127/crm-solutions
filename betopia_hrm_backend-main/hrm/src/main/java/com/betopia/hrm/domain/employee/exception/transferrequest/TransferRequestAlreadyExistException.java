package com.betopia.hrm.domain.employee.exception.transferrequest;

public class TransferRequestAlreadyExistException extends RuntimeException {
    public TransferRequestAlreadyExistException(String message) {
        super(message);
    }
}
