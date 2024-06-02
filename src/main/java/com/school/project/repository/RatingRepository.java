package com.school.project.repository;

import com.school.project.model.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends JpaRepository< Rating, Long>, JpaSpecificationExecutor<Rating> {
    Page<Rating> findAllByCourseId(Long id, Pageable pageable);
    boolean existsByCreatedByAndCourseId(String createdBy, Long courseId);
    Rating findByCourseId(Long courseId);
    @Query(value = "SELECT SUM(r.ratingStar), COUNT(r) FROM Rating r Where r.course.id = :courseId")
    List<Object[]> getTotalStarsAndTotalRatings(@Param("courseId") long courseId);
}
