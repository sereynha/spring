package com.school.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Data
public class PromotionDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Spring Boot")
    @NotNull(message = "Promotion name can't  be null")
    @Size(max = 64, message = "Name should be 8 characters to 64 characters")
    private String name;
    @Schema(example = "Summer sale 2024")
    private String slug;
    @Schema(example = "Spring Boot JPA ")
    private String description;
    @Schema(example = "20")
    private Long discountPercentage;
    @Schema(example = "3")
    private Long amountCourse;
    @Schema(example = "59.9")
    private BigDecimal priceAmount;
    @Schema(example = "#{new java.util.Date()}")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime startDate;
    @Schema(example = "#{new java.util.Date()}")
    private @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  LocalDateTime endDate;
    private List<Long> courseId;
}
