package com.school.project.service;

import com.school.project.config.jwt.JwtService;
import com.school.project.dto.AuthLoginDto;
import com.school.project.dto.AuthTokenDto;
import com.school.project.dto.RegisterDto;
import com.school.project.mail.EmailService;
import com.school.project.model.Role;
import com.school.project.model.Token;
import com.school.project.model.User;
import com.school.project.repository.RoleRepository;
import com.school.project.repository.TokenRepository;
import com.school.project.repository.UserRepository;
import com.school.project.service.impl.AuthenticationServiceImpl;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenRepository tokenRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;


    private AuthenticationService authenticationService;


    @BeforeEach
    public void setup() {
        authenticationService = new AuthenticationServiceImpl(userRepository,roleRepository,passwordEncoder,tokenRepository,emailService,authenticationManager,jwtService);
    }

    @Test
    public void testRegister() throws MessagingException {
        //given
        RegisterDto dto = new RegisterDto();
        dto.setFirstName("John");
        dto.setLastName("Vich");
        dto.setEmail( "john.doe@example.com");
        dto.setPassword("password");
        Role userRole = new Role();
        userRole.setName("USER");

        //when
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        doNothing().when(emailService).sendEmail(any(), any(), any(), any(), any(), any());
        authenticationService.register(dto);

        //then
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any(), any(), any());
    }

    @Test
    public void testLogin() {
        //given
        AuthLoginDto loginDto = new AuthLoginDto("john.doe@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        User user = new User();
        user.setEmail("john.doe@example.com");

        //when
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(user);
        when(jwtService.generateToken(anyMap(), eq(user))).thenReturn("jwtToken");
        AuthTokenDto tokenDto = authenticationService.login(loginDto);

        //then
        assertEquals("jwtToken", tokenDto.getToken());
    }

    @Test
    public void testActivateAccount() throws MessagingException {
        //given
        String token = "activationToken";
        Token savedToken = new Token();
        savedToken.setToken(token);
        savedToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
        User user = new User();
        user.setId(1L);
        savedToken.setUser(user);

        //when
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(savedToken));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        authenticationService.activateAccount(token);

        //then
        assertTrue(user.isEnabled());
        verify(userRepository, times(1)).save(user);
        verify(tokenRepository, times(1)).save(savedToken);
    }

    @Test
    public void testActivateAccountTokenExpired() throws MessagingException {
        //given
        String token = "activationToken";
        Token savedToken = new Token();
        savedToken.setToken(token);
        savedToken.setExpiresAt(LocalDateTime.now().minusMinutes(1)); // Expired token
        User user = new User();
        user.setId(1L);
        savedToken.setUser(user);

        //when
        when(tokenRepository.findByToken(token)).thenReturn(Optional.of(savedToken));
        doNothing().when(emailService).sendEmail(any(), any(), any(), any(), any(), any());

        //then
        assertThatThrownBy(() -> authenticationService.activateAccount(token))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Activation token has expired");
        verify(emailService, times(1)).sendEmail(any(), any(), any(), any(), any(), any());
    }

    @Test
    void testGetAll() {
        //given
        List<User> userList = List.of(new User(), new User());

        //when
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = authenticationService.getAll();

        //then
        assertEquals(userList, result);
    }
}