package com.school.project.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;

import jakarta.mail.MessagingException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.*;
import static com.school.project.exception.BusinessErrorCodes.*;

class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleLockedException() {
        LockedException exception = new LockedException("Account is locked");
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(ACCOUNT_LOCKED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(ACCOUNT_LOCKED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals(exception.getMessage(), response.getBody().getError());
    }

    @Test
    void handleDisabledException() {
        DisabledException exception = new DisabledException("Account is disabled");
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(ACCOUNT_DISABLED.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(ACCOUNT_DISABLED.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals(exception.getMessage(), response.getBody().getError());
    }

    @Test
    void handleBadCredentialsException() {

        // When
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException();

        // Then
        assertEquals(UNAUTHORIZED, response.getStatusCode());
        assertEquals(BAD_CREDENTIALS.getCode(), response.getBody().getBusinessErrorCode());
        assertEquals(BAD_CREDENTIALS.getDescription(), response.getBody().getBusinessErrorDescription());
        assertEquals("Login and / or Password is incorrect", response.getBody().getError());
    }

    @Test
    void handleMessagingException() {
        // given
        MessagingException exception = new MessagingException("Email sending failed");

        // when
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        // then
        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(exception.getMessage(), response.getBody().getError());
    }

    @Test
    void handleActivationTokenException() {
        // give
        ActivationTokenException exception = new ActivationTokenException("Invalid token");

        // when
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        // then
        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(exception.getMessage(), response.getBody().getError());
    }

    @Test
    void handleMethodArgumentNotValidException() {
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(new BeanPropertyBindingResult(Collections.emptyList(), "target"));
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleMethodArgumentNotValidException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(Collections.emptySet(), response.getBody().getValidationErrors());
    }

    @Test
    void handleGeneralException() {
        Exception exception = new Exception("Internal server error");
        ResponseEntity<ExceptionResponse> response = globalExceptionHandler.handleException(exception);

        assertEquals(INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal error, please contact the admin", response.getBody().getBusinessErrorDescription());
        assertEquals(exception.getMessage(), response.getBody().getError());
    }

    @Test
    void handleApiException() {
        ApiException exception = new ApiException(BAD_REQUEST, "API error");
        ResponseEntity<?> response = globalExceptionHandler.handleApiException(exception);

        assertEquals(BAD_REQUEST, response.getStatusCode());
        assertEquals(exception.getMessage(), ((ErrorResponseTwo) response.getBody()).getMessage());
        assertEquals(BAD_REQUEST, ((ErrorResponseTwo) response.getBody()).getStatus());
    }
}