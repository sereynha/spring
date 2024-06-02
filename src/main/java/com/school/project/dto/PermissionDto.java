package com.school.project.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PermissionDto {
    @NotNull( message = "Permission name can't be null")
    private String name;
}
