package com.school.project.mapper;

import com.school.project.dto.PromotionDto;
import com.school.project.model.Course;
import com.school.project.model.Promotion;
import com.school.project.service.CategoryService;
import com.school.project.service.CourseService;
import org.mapstruct.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {CourseService.class})
public interface PromotionMapper {
    @Mapping(target = "course", source = "courseId")
    Promotion toEntity(PromotionDto promotionDto);

    @Mapping(target = "courseId",source = "course")
    PromotionDto toDto(Promotion promotion);

    default Set<Course> map(List<Long> courseIds, @Context CourseService courseService) {
        return courseIds.stream()
                .map(courseService::getById)
                .collect(Collectors.toSet());
    }

    default List<Long> map(Set<Course> courses) {
        return courses.stream()
                .map(Course::getId)
                .collect(Collectors.toList());
    }
}