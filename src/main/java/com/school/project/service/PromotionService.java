package com.school.project.service;

import com.school.project.dto.PromotionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Promotion;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public interface PromotionService {
    ResponseStatusDto create(PromotionDto dto);
    Page<Promotion> getAll(int page, int limit, String sort, String name, LocalDateTime startDate, LocalDateTime endDate);
    Promotion getById(Long id);
    ResponseStatusDto update(Long id,boolean activation);
}
