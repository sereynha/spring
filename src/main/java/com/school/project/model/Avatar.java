package com.school.project.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "avatar")
@Setter
@Getter
@NoArgsConstructor
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String url;

    @ManyToOne
    @JoinColumn(name = "user_id",referencedColumnName = "id")
    private User user;

}
