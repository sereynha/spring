package com.school.project.service;

import com.school.project.dto.CourseDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Course;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface CourseService {
    ResponseStatusDto create(CourseDto dto);
    Course getById(Long id);
    Page<Course> getAll(int page, int limit, String sort, Map<String, String> params);
    ResponseStatusDto update(Long id,CourseDto dto);
    List<Course> findCoursesByIds(List<Long> id);
}
