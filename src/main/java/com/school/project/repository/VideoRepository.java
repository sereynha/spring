package com.school.project.repository;

import com.school.project.model.Course;
import com.school.project.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoRepository extends JpaRepository< Video, Long>, JpaSpecificationExecutor<Video> {
    Optional<Video> findByLinkUrlAndIsPublishedTrue(String linkUrl);
    Optional<Video> findBySlugAndIsPublishedTrue(String slug);
    Page<Video> findAllByCourse(Course course, Pageable pageable);
    List<Video> findAllByCourseId(Long id);
}
