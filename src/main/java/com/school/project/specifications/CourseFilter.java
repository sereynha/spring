package com.school.project.specifications;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseFilter {
    private Long id;
    private String name;
    private BigDecimal price;
    private String description;
}
