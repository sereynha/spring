package com.school.project.controller;

import com.school.project.dto.PageDto;
import com.school.project.dto.PromotionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Promotion;
import com.school.project.service.PromotionService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PromotionControllerTest {

    private PromotionController promotionController;
    private PromotionService promotionService;
    private PromotionDto promotionDto;
    private Promotion promotion;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    void setUp() {
        promotionService = mock(PromotionService.class);
        promotionController = new PromotionController(promotionService);
        startDate = LocalDateTime.parse("2024-05-20T11:49:08.971");
        endDate = LocalDateTime.parse("2024-05-20T11:49:08.971");

        promotion = new Promotion();
        promotion.setName("Summer sale 2024");
        promotion.setSlug("test-slug");
        promotion.setStartDate(startDate);
        promotion.setEndDate(endDate);

        promotionDto = new PromotionDto();
        promotionDto.setName("Summer sale 2024");
        promotionDto.setSlug("test-slug");
        promotionDto.setStartDate(startDate);
        promotionDto.setEndDate(endDate);
    }

    @Test
    void testCreate() {
        //give
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Create Course","SUCCESS","201 Created");

        // when
        when(promotionService.create(promotionDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = promotionController.create(promotionDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Create Course", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("201 Created", response.getBody().getStatusCode());
        verify(promotionService, times(1)).create(promotionDto);
    }

    @Test
    void testGetAll(){
        // given
        PageImpl<Promotion> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(promotionService.getAll(1, 10, "name:asc","Summer sale 2024",startDate,endDate)).thenReturn(page);
        ResponseEntity<PageDto> response = promotionController.getAll(1, 10, "name:asc","Summer sale 2024",startDate,endDate);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getList().size());
        verify(promotionService, times(1)).getAll(1, 10, "name:asc","Summer sale 2024",startDate,endDate);
    }

    @Test
    void testGetByOne(){
        // give
        Long id = 1L;

        // when
        when(promotionService.getById(anyLong())).thenReturn(promotion);
        ResponseEntity<Promotion> response = promotionController.getByOne(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(promotion, response.getBody());
        verify(promotionService, times(1)).getById(id);
    }

    @Test
    void testUpdate(){
        // give
        Long id = 1L;
        boolean activation = true;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Promotion","SUCCESS","200 OK");

        // when
        when(promotionService.update(id, activation)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = promotionController.update(id, activation);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Promotion", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(promotionService, times(1)).update(id, activation);
    }

}
