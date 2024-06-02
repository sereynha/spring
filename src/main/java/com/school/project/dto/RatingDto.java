package com.school.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingDto {
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "good")
    private String content;
    @Schema(requiredMode = Schema.RequiredMode.NOT_REQUIRED, example = "1")
    private int ratingStar;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Course id can't  be null")
    private Long courseId;
}
