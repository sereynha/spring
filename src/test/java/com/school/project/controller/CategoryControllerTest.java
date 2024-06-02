package com.school.project.controller;

import com.school.project.dto.CategoryDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Category;
import com.school.project.service.CategoryService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    private CategoryService categoryService;
    private CategoryController categoryController;
    private CategoryDto categoryDto;

    @BeforeEach
    void setUp() {
        categoryService = mock(CategoryService.class);
        categoryController = new CategoryController(categoryService);

        categoryDto = new CategoryDto();
        categoryDto.setName("IT & Software");
        categoryDto.setDescription("IT & Software Technology");
    }

    @Test
    void testCreate() {
        // give
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Create Category","SUCCESS","201 Created");

        //when
        when(categoryService.create(categoryDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = categoryController.create(categoryDto);

        //then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Create Category", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("201 Created", response.getBody().getStatusCode());
        verify(categoryService, times(1)).create(categoryDto);
    }

    @Test
    void testGetCategory() {
        //give
        Map<String, String> params = Map.of("name", "IT & Software");
        PageImpl<Category> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 5), 0);

        // when
        when(categoryService.getAll(anyInt(), anyInt(), anyString(), anyMap()))
                .thenReturn(page);
        ResponseEntity<PageDto> response = categoryController.getCategory(1, 5, "name:asc", params);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getList().size());
        verify(categoryService, times(1)).getAll(1, 5, "name:asc", params);
    }


    @Test
    void testFindOne() {
        // give
        Category category = new Category();
        category.setId(1L);
        category.setName("IT & Software");
        category.setDescription("IT & Software Technology");

        //when
        when(categoryService.getById(1L)).thenReturn(category);
        ResponseEntity<Category> response = categoryController.findOne(1L);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(category, response.getBody());
        verify(categoryService, times(1)).getById(1L);
    }
    @Test
    void testUpdate() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Category","SUCCESS","200 OK");

        // when
        when(categoryService.update(id, categoryDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = categoryController.update(id, categoryDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Category", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(categoryService, times(1)).update(id, categoryDto);
    }
}
