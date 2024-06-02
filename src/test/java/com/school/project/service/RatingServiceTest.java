package com.school.project.service;

import com.school.project.dto.RatingDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.RatingMapper;
import com.school.project.model.Course;
import com.school.project.model.Rating;
import com.school.project.repository.RatingRepository;
import com.school.project.service.impl.RatingServiceImpl;
import com.school.project.specifications.RatingFilter;
import com.school.project.specifications.RatingSpecifications;
import com.school.project.utils.AuthenticationUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @Mock
    private RatingMapper ratingMapper;


    private RatingService ratingService;
    private Course course;
    private RatingDto ratingDto;
    private Rating rating;
    private LocalDateTime createForm;
    private LocalDateTime createTo;

    @BeforeEach
    void setUp() {
        ratingService = new RatingServiceImpl(ratingRepository,ratingMapper);

        course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setPrice(BigDecimal.valueOf(69));
        course.setDescription("Java Spring Boot");
        course.setTotalHours(BigDecimal.valueOf(12));
        course.setLectures(1L);
        course.setImage("https://test.com/java_spring.jpg");

        rating = new Rating();
        rating.setId(1L);
        rating.setRatingStar(4);
        rating.setContent("good");
        rating.setCourse(course);
        rating.setCreatedBy("test@gmial.com");

        ratingDto = new RatingDto();
        ratingDto.setCourseId(1L);
        ratingDto.setRatingStar(4);
        ratingDto.setContent("good");

        createForm = LocalDateTime.now().minusDays(1);
        createTo = LocalDateTime.now();
    }

    @Test
    void testCreateSuccess() {
        String email = "test@gmial.com";
        try (MockedStatic<AuthenticationUtils> utilities = Mockito.mockStatic(AuthenticationUtils.class)) {
            utilities.when(AuthenticationUtils::extractUser).thenReturn(email);

            when(ratingRepository.existsByCreatedByAndCourseId(email, 1L)).thenReturn(false);
            when(ratingMapper.toEntity(ratingDto)).thenReturn(rating);
            when(ratingRepository.save(rating)).thenReturn(rating);

            Rating createdRating = ratingService.create(ratingDto);

            assertNotNull(createdRating);

            assertEquals(rating.getCourse(), createdRating.getCourse());
            verify(ratingRepository, times(1)).existsByCreatedByAndCourseId("test@gmial.com", 1L);

            verify(ratingMapper, times(1)).toEntity(ratingDto);
            verify(ratingRepository, times(1)).save(rating);
        }
    }

    @Test
    public void testCreateExistingRating() {
        RatingDto dto = new RatingDto();
        dto.setCourseId(1L);
        Rating existingRating = new Rating();
        try (MockedStatic<AuthenticationUtils> utilities = Mockito.mockStatic(AuthenticationUtils.class)) {
            utilities.when(AuthenticationUtils::extractUser).thenReturn("test@gmial.com");

            String username = AuthenticationUtils.extractUser();
            when(AuthenticationUtils.extractUser()).thenReturn("test@gmial.com");
            when(ratingRepository.existsByCreatedByAndCourseId("test@gmial.com", dto.getCourseId())).thenReturn(true);
            when(ratingRepository.findByCourseId(dto.getCourseId())).thenReturn(existingRating);
            when(ratingMapper.partialUpdate(dto, existingRating)).thenReturn(existingRating);
            when(ratingRepository.save(existingRating)).thenReturn(existingRating);

            Rating result = ratingService.create(dto);

            assertNotNull(result);
            assertEquals("test@gmial.com",username);
            verify(ratingRepository, times(1)).save(existingRating);
        }
    }

    @Test
    void testCreateResourceAlreadyExists() {
        String email = "test@gmial.com";
        try (MockedStatic<AuthenticationUtils> utilities = Mockito.mockStatic(AuthenticationUtils.class)) {
            utilities.when(AuthenticationUtils::extractUser).thenReturn(email);

            when(ratingRepository.existsByCreatedByAndCourseId(email, 1L)).thenReturn(true);

            assertThrows(NotFoundException.class, () -> ratingService.create(ratingDto));
            verify(ratingRepository, times(1)).existsByCreatedByAndCourseId(email, 1L);
            verify(ratingMapper, never()).toEntity(ratingDto);
            verify(ratingRepository, never()).save(rating);
        }
    }

    @Test
    void testGetAllRatingByCourseId() {
        long courseId = 1L;
        int page = 1;
        int limit = 10;
        List<Rating> ratingList = new ArrayList<>();
        ratingList.add(rating);
        Page<Rating> ratingPage = new PageImpl<>(ratingList);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());

        when(ratingRepository.findAllByCourseId(courseId, pageable)).thenReturn(ratingPage);
        when(ratingMapper.toDto(any(Rating.class))).thenReturn(new RatingDto());

        Page<RatingDto> result = ratingService.getAllRatingByCourseId(courseId, page, limit);

        assertEquals(ratingList.size(), result.getContent().size());
        assertEquals(rating.getId(),1L);
        assertEquals(rating.getRatingStar(),ratingDto.getRatingStar());
        assertEquals(rating.getContent(),ratingDto.getContent());
        assertEquals(rating.getContent(),ratingDto.getContent());
        verify(ratingRepository, times(1)).findAllByCourseId(eq(courseId), any(Pageable.class));
        verify(ratingMapper, times(ratingList.size())).toDto(any(Rating.class));
    }

    @Test
    void testGetAll() {
        int page = 1;
        int limit = 10;
        RatingFilter filter = new RatingFilter();
        filter.setMessage("good");
        filter.setCreateFrom(createForm);
        filter.setCreateTo(createTo);

        List<Rating> ratingList = new ArrayList<>();
        ratingList.add(rating);
        List<RatingDto> ratingDtos = List.of(ratingDto);
        Page<Rating> ratingPage = new PageImpl<>(ratingList);
        Specification<Rating> spec = new RatingSpecifications(filter);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("createAt").descending());

        when(ratingRepository.findAll(spec, pageable)).thenReturn(ratingPage);
        when(ratingMapper.toDto(any(Rating.class))).thenReturn(ratingDto);

        Page<RatingDto> result = ratingService.getAll("good", createForm, createTo,page, limit);

        verify(ratingRepository, times(1)).findAll(spec, pageable);
        verify(ratingMapper, times(1)).toDto(rating);
        assertEquals(ratingDtos, result.getContent());
    }

    @Test
    void deleteRating() {
        Long ratingId = 1L;
        Rating rating = new Rating();
        rating.setId(ratingId);

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(rating));

        ratingService.delete(ratingId);

        verify(ratingRepository, times(1)).delete(rating);
    }

    @Test
    void deleteRatingThrowsNotFoundException() {
        Long ratingId = 1L;

        when(ratingRepository.findById(ratingId)).thenReturn(Optional.empty());

        NotFoundException exception = assertThrows(NotFoundException.class, () -> ratingService.delete(ratingId));

        assertEquals("RATING_NOT_FOUND With id = 1 not found", exception.getMessage());
    }

    @Test
    void calculateAverageStare() {
        List<Object[]> totalStarsAndRatings =List.of(new Object[][]{new Object[]{5, 1}});

        when(ratingRepository.getTotalStarsAndTotalRatings(course.getId())).thenReturn(totalStarsAndRatings);

        Double result = ratingService.calculateAverageStar(course.getId());

        assertEquals(5.0, result);
    }

    @Test
    void calculateAverageStarReturnsZero() {
        List<Object[]> totalStarsAndRatings = List.of(new Object[][]{new Object[]{null, null}});

        when(ratingRepository.getTotalStarsAndTotalRatings(course.getId())).thenReturn(totalStarsAndRatings);

        Double averageStars = ratingService.calculateAverageStar(course.getId());

        assertEquals(0.0, averageStars);
    }

}

