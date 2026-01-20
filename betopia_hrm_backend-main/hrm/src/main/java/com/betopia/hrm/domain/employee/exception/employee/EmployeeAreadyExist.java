package com.betopia.hrm.domain.employee.exception.employee;

public class EmployeeAreadyExist extends RuntimeException{
    public EmployeeAreadyExist(String message){
        super(message);
    }
}
