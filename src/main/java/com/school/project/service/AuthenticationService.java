package com.school.project.service;

import com.school.project.dto.AuthLoginDto;
import com.school.project.dto.AuthTokenDto;
import com.school.project.dto.RegisterDto;
import com.school.project.model.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface AuthenticationService {
    void register(RegisterDto dto) throws MessagingException;
    List<User> getAll();
    void sendValidationEmail(User user) throws MessagingException ;
    AuthTokenDto login(AuthLoginDto request);
    void activateAccount(String token) throws MessagingException;
}
