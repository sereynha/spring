package com.school.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VideoDto {
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED,example = "Summer sale 2024")
    private String slug;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "Introduction")
    @NotNull(message = "Video title can't  be null")
    String title;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "https://youtub.com/introduction.jpg")
    @NotNull(message = "Video url must be not null")
    @NotBlank
    String linkUrl;
    String imageCover;
    boolean isPublished;
    @Schema(requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "Course id can't  be null")
    private Long courseId;
}