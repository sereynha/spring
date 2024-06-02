package com.school.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
public class RegisterDto {
    @NotNull(message = "Firstname is mandatory")
    private String firstName;
    @NotNull(message = "Lastname is mandatory")
    private String lastName;
    @Email(message = "Email is not well formatted")
    @NotNull(message = "Email is mandatory")
    private String email;
    @NotNull(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be 8 characters long minimum")
    private String password;
}
