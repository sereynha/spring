package com.school.project.mapper;

import com.school.project.dto.CourseDto;
import com.school.project.model.Course;
import com.school.project.service.CategoryService;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CategoryService.class})
public interface CourseMapper {
    @Mapping(target = "category", source = "categoryId")
    Course toEntity(CourseDto courseDto);

    @Mapping(target = "categoryId", source = "category.id")
    CourseDto toDto(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Course partialUpdate(CourseDto courseDto, @MappingTarget Course course);
}