package com.school.project.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "course")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 64)
    private String name;

    private BigDecimal price;

    @Column(name = "total_hours")
    private BigDecimal totalHours;

    private Long lectures;
    private String image;
    private String description;

    @Column(columnDefinition = "boolean default false")
    private Boolean isActive;

    @ManyToOne
    @JoinColumn(name = "categoryId",nullable = false, referencedColumnName = "id")
    private Category category;

}
