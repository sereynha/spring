package com.school.project.controller;

import com.school.project.dto.AuthLoginDto;
import com.school.project.dto.AuthTokenDto;
import com.school.project.dto.ErrorDto;
import com.school.project.dto.RegisterDto;
import com.school.project.service.AuthenticationService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {
    private final AuthenticationService authenticationServiceI;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(
            @RequestBody @Valid RegisterDto dto
    ) throws MessagingException {
        authenticationServiceI.register(dto);
        return ResponseEntity.accepted().build();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/login")
    public  ResponseEntity<AuthTokenDto> login(
            @RequestBody AuthLoginDto dto
            ){
        return ResponseEntity.ok(authenticationServiceI.login(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "202", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })

    @GetMapping("/activate-acc")
    public  void confirm(
            @RequestParam String token
    ) throws MessagingException {
        authenticationServiceI.activateAccount(token);
    }
}
