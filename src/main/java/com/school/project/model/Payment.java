package com.school.project.model;

import com.school.project.model.enumeration.EnumPaymentMethod;
import com.school.project.model.enumeration.EnumPaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Set;


@Entity
@Table(name = "payment")
@Setter
@Getter
@NoArgsConstructor
public class Payment extends  AbstractAuditEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal totalPayment;
    @Enumerated(EnumType.STRING)
    private EnumPaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private EnumPaymentStatus paymentStatus;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Course> course;
}
