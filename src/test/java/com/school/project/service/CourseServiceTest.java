package com.school.project.service;

import com.school.project.dto.CourseDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.CourseMapper;
import com.school.project.model.Category;
import com.school.project.model.Course;
import com.school.project.repository.CourseRepository;
import com.school.project.service.impl.CourseServiceImpl;
import com.school.project.specifications.CourseFilter;
import com.school.project.specifications.CourseSpecifications;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CourseServiceTest {
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseMapper courseMapper;
    @Captor
    private ArgumentCaptor<Course> captor;
    private CourseService courseService;

    private Map<String, String> params;
    private CourseDto courseDto;

    @BeforeEach
    public void setUp() {
        courseService = new CourseServiceImpl(courseRepository,courseMapper);
        params = new HashMap<>();

        courseDto = new CourseDto();
        courseDto.setName("Spring Boot");
        courseDto.setPrice(BigDecimal.valueOf(69));
        courseDto.setDescription("Java Spring Boot");
        courseDto.setTotalHours(BigDecimal.valueOf(12));
        courseDto.setLectures(1L);
        courseDto.setImage("https://test.com/java_spring.jpg");
        courseDto.setCategoryId(1L);
    }
    @Test
    public void testCreate() {
        // give
        Category category = new Category(1L,"IT & Software","IT & Software Technology");
        Course course = new Course(1L,"Spring Boot", BigDecimal.valueOf(69),BigDecimal.valueOf(12),1L,"https://test.com/java_spring.jpg", "Java Spring Boot",true,category);


        // when
        when(courseMapper.toEntity(courseDto)).thenReturn(course);
        when(courseRepository.save(course)).thenReturn(course);
        courseService.create(courseDto);

        // then
        verify(courseMapper, times(1)).toEntity(courseDto);
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testGetById() {
        // given
        Category category = new Category(1L,"IT & Software","IT & Software Technology");
        Long id = 1L;
        Long lectures = 1L;
        String name = "Spring Boot";
        BigDecimal price = BigDecimal.valueOf(69);
        BigDecimal hours = BigDecimal.valueOf(12);
        String description = "Java Spring Boot";
        String image = "https://test.com/java_spring.jpg";
        Course course = new Course(id,name, price,hours,lectures,image, description,true,category);


        // when
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        Course result = courseService.getById(id);

        // then
        assertEquals(id,result.getId());
        assertEquals("Spring Boot",result.getName());
        assertEquals(price,result.getPrice());
        assertEquals(lectures,result.getLectures());
        assertEquals(hours,result.getTotalHours());
        assertEquals(hours,result.getTotalHours());
        assertEquals(description,result.getDescription());
        assertEquals(image,result.getImage());
        assertEquals(category,result.getCategory());
        assertEquals(course.getIsActive(),result.getIsActive());
        assertEquals(courseDto.getCategoryId(),course.getId());
        verify(courseRepository, times(1)).findById(id);
    }

    @Test
    public void testGetByIdThrow() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> courseService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("COURSE_NOT_FOUND With id = 1 not found");
    }

    @Test
    public void getAll() {
        // give
        params.put("name","Spring Boot");
        params.put("id", "1");
        params.put("description","Java Spring Boot");
        CourseFilter filter = new CourseFilter();
        filter.setId(1L);
        filter.setName("Spring Boot");
        filter.setDescription("Java Spring Boot");
        CourseSpecifications specifications = new CourseSpecifications(filter);
        Page<Course> expectedPage = new PageImpl<>(Collections.emptyList());

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(courseRepository.findAll(specifications, pageable)).thenReturn(expectedPage);
        Page<Course> resultPage = courseService.getAll(1, 10, "name:asc", params);

        // then
        verify(courseRepository, times(1)).findAll(specifications, pageable);
        assertThat(resultPage).isEqualTo(expectedPage);
    }

    @Test
    public void testUpdate() {
        // give
        Category category = new Category(1L,"IT & Software","IT & Software Technology");
        Long id = 1L;
        Course course = new Course(1L,"Spring Boot", BigDecimal.valueOf(69),BigDecimal.valueOf(12),1L,"https://test.com/java_spring.jpg", "Java Spring Boot",true,category);

        //when
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        courseService.update(id, courseDto);

        // then
        verify(courseRepository, times(1)).findById(id);
        verify(courseRepository).save(captor.capture());
        assertEquals("Spring Boot", captor.getValue().getName());
        assertEquals(1L, captor.getValue().getId());
    }

    @Test
    public void testGetCourseByIds() {
        // given
        Category category = new Category(1L,"IT & Software","IT & Software Technology");
        Category categoryTwo = new Category(2L,"IT & Software","IT & Software Technology");
        List<Long> ids = Arrays.asList(1L, 2L);
        Course course = new Course(1L,"Spring Boot", BigDecimal.valueOf(69),BigDecimal.valueOf(12),1L,"https://test.com/java_spring.jpg", "Java Spring Boot",true,category);
        Course courseTwo = new Course(2L,"Spring Boot", BigDecimal.valueOf(69),BigDecimal.valueOf(12),1L,"https://test.com/java_spring.jpg", "Java Spring Boot",true,categoryTwo);
        List<Course> courses = Arrays.asList(course, courseTwo);

        // when
        when(courseRepository.findAllById(ids)).thenReturn(courses);
        List<Course> result = courseService.findCoursesByIds(ids);

        // then
        assertEquals(2, result.size());
        assertEquals(course.getName(),result.get(0).getName());
        assertEquals(courseTwo.getName(),result.get(1).getName());
        verify(courseRepository,times(1)).findAllById(ids);
    }

    @Test
    public void testGetCourseByIdsNotFound() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(courseRepository.findAllById(ids)).thenReturn(Collections.emptyList());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            courseService.findCoursesByIds(ids);
        });

        assertEquals("COURSE_NOT_FOUND With id = 1 not found", exception.getMessage());
    }
}
