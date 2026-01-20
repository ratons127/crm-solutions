package com.betopia.hrm.domain.company.exception;

public class BankNotFound extends RuntimeException {
    public BankNotFound(String message) {
        super(message);
    }
}
