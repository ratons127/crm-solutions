package com.betopia.hrm.domain.employee.exception.employeeTypes;

public class EmployeeTypesAlreadyExist extends RuntimeException{
    public EmployeeTypesAlreadyExist(String message){
        super(message);
    }
}
