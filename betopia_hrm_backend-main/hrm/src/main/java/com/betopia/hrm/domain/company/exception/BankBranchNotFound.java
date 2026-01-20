package com.betopia.hrm.domain.company.exception;

public class BankBranchNotFound extends RuntimeException {
    public BankBranchNotFound(String message) {
        super(message);
    }
}
