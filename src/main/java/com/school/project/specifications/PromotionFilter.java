package com.school.project.specifications;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Data
public class PromotionFilter {
    String name;
    LocalDateTime startDate;
    LocalDateTime endDate;
}