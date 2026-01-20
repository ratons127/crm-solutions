package com.betopia.hrm.domain.employee.exception.personalHobby;

public class PersonalHobbyAlreadyExistException extends RuntimeException{
    public PersonalHobbyAlreadyExistException(String message){
        super(message);
    }
}
