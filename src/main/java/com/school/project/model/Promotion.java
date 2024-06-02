package com.school.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Set;

@Entity
@Table(name = "promotion")
@Data
@NoArgsConstructor
public class Promotion extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String slug;
    private String description;
    private Long discountPercentage;
    private Long amountCourse;
    private BigDecimal priceAmount;
    @Column(columnDefinition = "boolean default true")
    private Boolean isActive;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Course> course;
}
