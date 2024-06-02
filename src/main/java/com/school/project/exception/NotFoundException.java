package com.school.project.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ApiException{
    public NotFoundException(String resource, Long var){
        super(HttpStatus.NOT_FOUND, String.format("%s With id = %s not found",resource,var ));
    }
}
