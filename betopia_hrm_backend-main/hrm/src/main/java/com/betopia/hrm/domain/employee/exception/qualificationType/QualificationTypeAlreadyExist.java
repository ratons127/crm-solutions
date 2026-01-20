package com.betopia.hrm.domain.employee.exception.qualificationType;

public class QualificationTypeAlreadyExist extends RuntimeException{
    public QualificationTypeAlreadyExist(String message) {
        super(message);
    }
}
