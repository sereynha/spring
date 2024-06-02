package com.school.project.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.*;

@Getter
public enum BusinessErrorCodes {
    CREATE(201,CREATED,"Create is Successful"),
    NO_CODE(501, NOT_IMPLEMENTED, "No code"),
    INCORRECT_CURRENT_PASSWORD(400, BAD_REQUEST, "Current password is incorrect"),
    NEW_PASSWORD_DOES_NOT_MATCH(401, BAD_REQUEST, "The new password does not match"),
    ACCOUNT_LOCKED(423, FORBIDDEN, "User account is locked"),
    ACCOUNT_DISABLED(403, FORBIDDEN, "User account is disabled"),
    BAD_CREDENTIALS(401, FORBIDDEN, "Login and / or Password is incorrect"),
    ;

    private final int code;
    private final String description;
    private final HttpStatus httpStatus;

    BusinessErrorCodes(int code, HttpStatus status, String description) {
        this.code = code;
        this.description = description;
        this.httpStatus = status;
    }
}