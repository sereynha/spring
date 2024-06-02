package com.school.project.controller;

import com.school.project.dto.CourseDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Course;
import com.school.project.service.CourseService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

class CourseControllerTest {

    private CourseService courseService;
    private CourseController courseController;
    private CourseDto courseDto ;
    private Course course;

    @BeforeEach
    void setUp() {
        courseService = mock(CourseService.class);
        courseController = new CourseController(courseService);

        courseDto = new CourseDto();
        courseDto.setName("Spring Boot");
        courseDto.setPrice(BigDecimal.valueOf(69));
        courseDto.setDescription("Java Spring Boot");
        courseDto.setTotalHours(BigDecimal.valueOf(12));
        courseDto.setLectures(1L);
        courseDto.setImage("https://test.com/java_spring.jpg");
        courseDto.setCategoryId(1L);

        course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setDescription("Java Spring Boot");
        course.setPrice(BigDecimal.valueOf(69));
    }

    @Test
    void testCreate() {
        //give
        ResponseStatusDto responseStatusDto = new ResponseStatusDto();
        responseStatusDto.setStatusCode("201 Created");
        responseStatusDto.setMessage("Create Course");
        responseStatusDto.setTitle("SUCCESS");

        // when
        when(courseService.create(courseDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = courseController.create(courseDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(responseStatusDto.getTitle(), Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(responseStatusDto.getMessage(), response.getBody().getMessage());
        assertEquals(responseStatusDto.getStatusCode(), response.getBody().getStatusCode());
        verify(courseService, times(1)).create(courseDto);
    }

    @Test
    void testFindOne() {
        // give
        Long id = 1L;

        // when
        when(courseService.getById(anyLong())).thenReturn(course);
        ResponseEntity<Course> response = courseController.findOne(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(course, response.getBody());
        verify(courseService, times(1)).getById(id);
    }

    @Test
    void testGetCourse() {
        // give
        Map<String, String> params = new HashMap<>();
        params.put("name", "Math");
        PageImpl<Course> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(courseService.getAll(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);
        ResponseEntity<PageDto> response = courseController.getCourse(1, 10, "name:asc", params);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getList().size());
        verify(courseService, times(1)).getAll(1, 10, "name:asc", params);
    }

    @Test
    void testUpdate() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Course","SUCCESS","200 OK");

        // when
        when(courseService.update(id, courseDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = courseController.update(id, courseDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Course", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(courseService, times(1)).update(id, courseDto);
    }

    @Test
    void findManyById(){
        // given
        List<Long> ids = Arrays.asList(1L, 2L);
        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Course 2");
        List<Course> courses = Arrays.asList(course, course2);

        // when
        when(courseService.findCoursesByIds(ids)).thenReturn(courses);
        ResponseEntity<List<Course>> response = courseController.findManyById(ids);

        // then
        assertEquals(2,response.getBody().size());
        verify(courseService, times(1)).findCoursesByIds(ids);
    }
}
