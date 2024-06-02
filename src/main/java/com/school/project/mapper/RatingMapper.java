package com.school.project.mapper;

import com.school.project.dto.RatingDto;
import com.school.project.model.Rating;
import com.school.project.service.CourseService;
import org.mapstruct.*;

@Mapper(componentModel = "spring",uses = {CourseService.class})
public interface RatingMapper {
    @Mapping(target = "course", source = "courseId")
    Rating toEntity(RatingDto ratingDto);

    @Mapping(target = "courseId", source = "course.id")
    RatingDto toDto(Rating rating);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Rating partialUpdate(RatingDto ratingDto, @MappingTarget Rating rating);
}