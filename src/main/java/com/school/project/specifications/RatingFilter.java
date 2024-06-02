package com.school.project.specifications;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RatingFilter {
    String message;
    LocalDateTime createFrom;
    LocalDateTime createTo;
}
