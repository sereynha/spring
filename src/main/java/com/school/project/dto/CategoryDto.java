package com.school.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoryDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "IT & Software", minLength = 1)
    @NotNull( message = "Category name can't be null")
    @NotEmpty(message = "Name is cannot be NotEmpty")
    @Size(min = 1,message = "Category name can't be null")
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "IT & Software")
    private String description;

}
