package com.betopia.hrm.domain.employee.exception.grade;

public class GradeAlreadyExistException extends RuntimeException{
    public GradeAlreadyExistException(String message) {
        super(message);
    }
}
