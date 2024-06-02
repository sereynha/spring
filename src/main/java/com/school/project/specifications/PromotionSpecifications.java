package com.school.project.specifications;


import com.school.project.model.Promotion;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class PromotionSpecifications implements Specification<Promotion> {
    private final PromotionFilter filter;
    List<Predicate> predicates = new ArrayList<>();
    @Override
    public Predicate toPredicate( Root<Promotion> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                    "%" + filter.getName().toLowerCase() + "%");
            predicates.add(namePredicate);
        }
        if (filter.getStartDate() != null) {
            Predicate startDatePredicate = criteriaBuilder.greaterThanOrEqualTo(root.get("startDate"), filter.getStartDate());
            predicates.add(startDatePredicate);
        }
        if (filter.getEndDate() != null) {
            Predicate endDatePredicate = criteriaBuilder.lessThanOrEqualTo(root.get("endDate"), filter.getEndDate());
            predicates.add(endDatePredicate);
        }
        return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    }
}
