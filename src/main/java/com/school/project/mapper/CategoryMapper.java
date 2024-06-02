package com.school.project.mapper;

import com.school.project.dto.CategoryDto;
import com.school.project.model.Category;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    Category toEntity(CategoryDto categoryDto);

    CategoryDto toDto(Category category);

}