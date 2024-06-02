package com.school.project.specifications;

import com.school.project.model.Rating;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class RatingSpecifications implements Specification<Rating> {
    private final RatingFilter filter;
    List<Predicate> predicates = new ArrayList<>();

    @Override
    public Predicate toPredicate(Root<Rating> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if (filter.getMessage() != null){
            Predicate message = criteriaBuilder.like(criteriaBuilder.lower(root.get("content")), "%" + filter.getMessage().toUpperCase() + "%");
            predicates.add(message);
        }
        if (filter.getCreateFrom() != null && filter.getCreateTo() == null){
            Predicate createFrom = criteriaBuilder.lessThanOrEqualTo(root.get("createAt"), filter.getCreateFrom());
            predicates.add(createFrom);
        }
        if (filter.getCreateTo() != null && filter.getCreateFrom() == null){
            Predicate createTo = criteriaBuilder.greaterThanOrEqualTo(root.get("createAt"), filter.getCreateTo());;
            predicates.add(createTo);
        }
        if (filter.getCreateTo() != null && filter.getCreateFrom() != null) {
            Predicate createBetween = criteriaBuilder.between(root.get("createAt"), filter.getCreateFrom(), filter.getCreateTo());
            predicates.add(createBetween);
        }
        Predicate[] predicatesList = predicates.toArray(Predicate[]::new);
        return criteriaBuilder.and(predicatesList);
    }
}
