package com.school.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.models.media.FileSchema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Spring Boot", minLength = 1)
    @NotNull(message = "Course name can't  be null")
    @Size(min = 10, max = 64, message = "Name should be 8 characters to 64 characters")
    private String name;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "0.1")
    @NotNull(message = "Price can't be null")
    @DecimalMin(value = "0.1", message = "Price must be greater than 0")
    private BigDecimal price;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1",minLength = 1)
    @NotNull(message = "Hours can't be null")
    @Size(min = 1, message = "Course  should be 1 hours")
    private BigDecimal totalHours;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    @Size(min = 1,message = "Lectures should be 1 lectures long minimum")
    private Long lectures;

    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "test.jpg",type = FileSchema.BIND_TYPE_AND_TYPES)
    private String image;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Spring Boot JPA ")
    @NotNull(message = "Description name can't  be null")
    @Size(min = 10, message = "Description should be 10 characters  long minimum ")
    private String description;

    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Course of category can't  be null")
    private Long categoryId;
}
