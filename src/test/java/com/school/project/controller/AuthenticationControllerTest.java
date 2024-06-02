package com.school.project.controller;

import com.school.project.dto.AuthLoginDto;
import com.school.project.dto.AuthTokenDto;
import com.school.project.dto.RegisterDto;
import com.school.project.service.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister() throws Exception {
        RegisterDto registerDto = new RegisterDto();
        registerDto.setFirstName("dara");
        registerDto.setLastName("meng");
        registerDto.setEmail("dara@gmail.com");
        registerDto.setPassword("12341234");

        ResponseEntity<?> expectedResponse = ResponseEntity.accepted().build();

        doNothing().when(authenticationService).register(registerDto);

        ResponseEntity<?> actualResponse = authenticationController.register(registerDto);

        assertEquals(expectedResponse, actualResponse);
        verify(authenticationService, times(1)).register(registerDto);
    }

    @Test
    void testLogin() {
        AuthLoginDto authLoginDto = new AuthLoginDto("dara@gmial.com","12341234");
        AuthTokenDto authTokenDto = new AuthTokenDto("783456");

        when(authenticationService.login(authLoginDto)).thenReturn(authTokenDto);

        ResponseEntity<AuthTokenDto> responseEntity = authenticationController.login(authLoginDto);

        assertEquals(authTokenDto, responseEntity.getBody());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    void testConfirm() throws Exception {
        String token = "some_token";

        doNothing().when(authenticationService).activateAccount(token);

        authenticationController.confirm(token);

        verify(authenticationService, times(1)).activateAccount(token);
    }
}
