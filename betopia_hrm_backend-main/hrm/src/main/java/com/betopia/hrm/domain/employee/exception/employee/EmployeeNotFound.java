package com.betopia.hrm.domain.employee.exception.employee;

public class EmployeeNotFound extends RuntimeException{
    public EmployeeNotFound(String message){
        super(message);
    }
}
