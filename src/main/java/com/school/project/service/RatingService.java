package com.school.project.service;

import com.school.project.dto.RatingDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Rating;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;

public interface RatingService {
    Rating create(RatingDto dto);
    Page<RatingDto> getAllRatingByCourseId(Long courseId,int page,int limit);
    Page<RatingDto> getAll(String message, LocalDateTime createFrom, LocalDateTime createTo,int page, int limit);
    ResponseStatusDto delete(Long id);
    Double calculateAverageStar(Long courseId);
}
