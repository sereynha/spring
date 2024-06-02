package com.school.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "video")
@Setter
@Getter
@NoArgsConstructor
public class Video extends AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String slug;
    @Column(length = 64,nullable = false)
    private String title;
    @Column(nullable = false)
    private String linkUrl;
    private String imageCover;
    private boolean isPublished;
    @ManyToOne
    @JoinColumn(name = "courseId",nullable = false, referencedColumnName = "id")
    private Course course;

}
