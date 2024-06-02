package com.school.project.specifications;

import com.school.project.model.Category;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

@Data
public class CategorySpecifications implements Specification<Category> {

    private final CategoryFilter filter;

    List<Predicate> predicates = new ArrayList<>();

    @Override
    public Predicate toPredicate( Root<Category> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        if(filter.getName() != null){
            Predicate name = criteriaBuilder.like(criteriaBuilder.upper(root.get("name")),"%"+filter.getName().toUpperCase() + "%");
            predicates.add(name);
        }
        if (filter.getId() != null){
            Predicate id = root.get("id").in(filter.getId());
            predicates.add(id);
        }
        Predicate[] predicatesList = predicates.toArray(Predicate[]::new);
        return criteriaBuilder.and(predicatesList);
    }
}
