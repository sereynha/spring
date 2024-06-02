package com.school.project.service.impl;

import com.school.project.dto.CourseDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.CourseMapper;
import com.school.project.model.Course;
import com.school.project.repository.CourseRepository;
import com.school.project.service.CourseService;
import com.school.project.service.PageService;
import com.school.project.specifications.CourseFilter;
import com.school.project.specifications.CourseSpecifications;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public ResponseStatusDto create(CourseDto dto) {
        Course course = courseMapper.toEntity(dto);
        courseRepository.save(course);
        return new ResponseStatusDto("Create Course", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.CREATED.toString());
    }

    @Override
    public Course getById(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND,id));
    }


    @Override
    public Page<Course> getAll(int page, int limit, String sort, Map<String, String> params) {
        CourseFilter filter = new CourseFilter();
        if (params.containsKey("id")){
            String id = params.get("id");
            filter.setId(Long.parseLong(id));
        }
        if (params.containsKey("name")){
            String name = params.get("name");
            filter.setName(name);
        }
        if (params.containsKey("description")){
            String description = params.get("description");
            filter.setDescription(description);
        }
        CourseSpecifications specifications = new CourseSpecifications(filter);
        Pageable pageable = PageService.getPageable(page,limit,sort);
        return courseRepository.findAll(specifications, pageable);
    }

    @Override
    public ResponseStatusDto update(Long id, CourseDto dto) {
        Course found = this.getById(id);
        if(!Objects.equals(dto.getName(), "")){
            found.setName(dto.getName());
        }
        found.setDescription(dto.getDescription());
        found.setPrice(dto.getPrice());
        found.setLectures(dto.getLectures());
        found.setTotalHours(dto.getTotalHours());
        found.setImage(dto.getImage());
        courseRepository.save(found);
        return new ResponseStatusDto("Update Course", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public List<Course> findCoursesByIds(List<Long> id) {
        List<Course> courses = courseRepository.findAllById(id);
        if (courses.isEmpty())
            throw new NotFoundException(Constants.ERROR_CODE.COURSE_NOT_FOUND,id.get(0));
        return courses;
    }
}