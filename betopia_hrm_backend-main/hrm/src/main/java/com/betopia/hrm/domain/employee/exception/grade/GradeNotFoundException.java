package com.betopia.hrm.domain.employee.exception.grade;

public class GradeNotFoundException extends RuntimeException{
    public GradeNotFoundException(String message) {
        super(message);
    }
}
