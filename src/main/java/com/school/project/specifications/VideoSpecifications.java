package com.school.project.specifications;

import com.school.project.model.Video;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.Data;
import org.springframework.data.jpa.domain.Specification;

@Data
public class VideoSpecifications implements Specification<Video> {
     private final VideoFilter filter;
    @Override
    public Predicate toPredicate(Root<Video> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
        Predicate predicate = criteriaBuilder.conjunction();

        if (filter.getSlug() != null && !filter.getSlug().isEmpty()) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("slug"), "%" + filter.getSlug() + "%"));
        }

        if (filter.getTitle() != null && !filter.getTitle().isEmpty()) {
            predicate = criteriaBuilder.and(predicate, criteriaBuilder.like(root.get("title"), "%" + filter.getTitle() + "%"));
        }

        return predicate;
    }
}
