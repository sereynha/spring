package com.school.project.exception;

import org.springframework.http.HttpStatus;

public class DuplicatedException  extends ApiException{
    public DuplicatedException(String resource,String var){
        super(HttpStatus.CONFLICT, String.format(resource+ " is "+ var));
    }
}
