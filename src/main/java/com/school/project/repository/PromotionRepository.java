package com.school.project.repository;

import com.school.project.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long>, JpaSpecificationExecutor<Promotion> {
    @Query("select (count(p) > 0) from Promotion p where p.slug like %:slug% and p.isActive = true")
    boolean existsBySlugLikeAndIsActiveTrue(String slug);
}
