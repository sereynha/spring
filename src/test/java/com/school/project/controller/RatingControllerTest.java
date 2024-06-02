package com.school.project.controller;

import com.school.project.dto.RatingDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Rating;
import com.school.project.service.RatingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RatingControllerTest {

    private RatingController ratingController;
    private RatingService ratingService;
    private RatingDto ratingDto;
    private Rating rating;
    private LocalDateTime createForm;
    private LocalDateTime createTo;

    @BeforeEach
    void setUp() {
        ratingService = mock(RatingService.class);
        ratingController = new RatingController(ratingService);

        ratingDto = new RatingDto();
        ratingDto.setCourseId(1L);
        ratingDto.setRatingStar(4);
        ratingDto.setContent("Great course");

        rating = new Rating();
        rating.setId(1L);
        rating.setRatingStar(4);
        rating.setContent("Great course");

        createForm = LocalDateTime.now().minusDays(1);
        createTo = LocalDateTime.now();
    }

    @Test
    void testCreate() {
        // when
        when(ratingService.create(ratingDto)).thenReturn(rating);
        ResponseEntity<Rating> response = ratingController.create(ratingDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(rating, response.getBody());
        verify(ratingService, times(1)).create(ratingDto);
    }

    @Test
    void testDelete() {
        // given
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Delete Rating", "SUCCESS", "200 OK");

        // when
        when(ratingService.delete(id)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = ratingController.delete(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete Rating", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(ratingService, times(1)).delete(id);
    }

    @Test
    void testGetByCourseId() {
        // given
        PageImpl<RatingDto> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(ratingService.getAllRatingByCourseId(anyLong(), anyInt(), anyInt())).thenReturn(page);
        ResponseEntity<Page<RatingDto>> response = ratingController.getByCourseId(1L, 1, 10);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, Objects.requireNonNull(response.getBody()).getSize());
        assertEquals(0, response.getBody().getTotalPages());
        verify(ratingService, times(1)).getAllRatingByCourseId(1L, 1, 10);
    }

    @Test
    void testGetAll() {
        // given
        PageImpl<RatingDto> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(ratingService.getAll(anyString(), any(LocalDateTime.class), any(LocalDateTime.class), anyInt(), anyInt())).thenReturn(page);
        ResponseEntity<Page<RatingDto>> response = ratingController.getAll(1, 10, "good", createForm, createTo);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, Objects.requireNonNull(response.getBody()).getSize());
        assertEquals(0, response.getBody().getTotalPages());
        verify(ratingService, times(1)).getAll("good", createForm, createTo, 1, 10);
    }

    @Test
    void testGetAverageStarOfProduct() {
        // given
        Double averageStar = 4.5;

        // when
        when(ratingService.calculateAverageStar(1L)).thenReturn(averageStar);
        Double response = ratingController.getAverageStarOfProduct(1L);

        // then
        assertEquals(averageStar, response);
        verify(ratingService, times(1)).calculateAverageStar(1L);
    }
}