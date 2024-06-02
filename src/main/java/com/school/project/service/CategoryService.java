package com.school.project.service;

import com.school.project.dto.CategoryDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Category;
import org.springframework.data.domain.Page;

import java.util.Map;

public interface CategoryService {
    ResponseStatusDto create(CategoryDto dto);
    Page<Category> getAll(int page, int limit, String sort, Map<String, String> params);
    ResponseStatusDto update( Long id,CategoryDto dto);
    Category getById(Long id);
}
