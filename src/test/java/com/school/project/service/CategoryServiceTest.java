package com.school.project.service;

import com.school.project.dto.CategoryDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.CategoryMapper;
import com.school.project.model.Category;
import com.school.project.repository.CategoryRepository;
import com.school.project.service.impl.CategoryServiceImpl;
import com.school.project.specifications.CategoryFilter;
import com.school.project.specifications.CategorySpecifications;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceTest {
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;
    private CategoryService categoryService;

    private Map<String, String> params;
    private Category category;
    private CategoryDto categoryDto;

    @BeforeEach
    public void setUp() {
        categoryService = new CategoryServiceImpl(categoryRepository,categoryMapper);
        params = new HashMap<>();

        category = new Category();
        category.setId(1L);
        category.setName("IT & Software");
        category.setDescription("IT & Software Technology");
        categoryDto = new CategoryDto();
        categoryDto.setName("IT & Software");
        categoryDto.setDescription("IT & Software Technology");
    }

    @Test
    public void createCategoryTest() {
        // when
        when(categoryMapper.toEntity(categoryDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        categoryService.create(categoryDto);
        when(categoryRepository.findExistedName(anyString(), eq(null))).thenReturn(category);

        // then
        verify(categoryMapper, times(1)).toEntity(categoryDto);
        verify(categoryRepository, times(1)).save(category);
        assertEquals(category.getName(),categoryDto.getName());
        assertEquals(category.getDescription(),categoryDto.getDescription());
        assertThrows(DuplicatedException.class, () -> categoryService.create(categoryDto));
    }

    @Test
    public void testGetAll() {
        // give
        params.put("name", "IT & Software");
        params.put("id", "1");
        CategoryFilter filter = new CategoryFilter();
        filter.setName("IT & Software");
        filter.setId(1L);
        CategorySpecifications specifications = new CategorySpecifications(filter);
        Page<Category> expectedPage = new PageImpl<>(Collections.emptyList());

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(categoryRepository.findAll(specifications, pageable)).thenReturn(expectedPage);
        Page<Category> resultPage = categoryService.getAll(1, 10, "name:asc", params);

        // then
        verify(categoryRepository, times(1)).findAll(specifications, pageable);
        assertThat(resultPage).isEqualTo(expectedPage);
    }

    @Test
    public void testGetById() {
        // give
        Long id = 1L;

        //when
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        Category result = categoryService.getById(id);

        // then
        assertEquals(category.getId(),result.getId());
        assertEquals(category.getName(),result.getName());
        assertEquals(category.getDescription(),result.getDescription());
        verify(categoryRepository, times(1)).findById(id);
        assertThat(result).isEqualTo(category);
    }
    @Test
    public void testGetByIdThrow() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> categoryService.getById(1L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Category With id = 1 not found");
    }

    @Test
    public void testUpdate() {
        // give
        Long id = 1L;

        //when
        when(categoryRepository.findById(id)).thenReturn(Optional.of(category));
        categoryService.update(id, categoryDto);
        when(categoryRepository.findExistedName(categoryDto.getName(), id)).thenReturn(category);

        // then
        verify(categoryRepository, times(1)).findById(id);
        assertThrows(DuplicatedException.class, () -> categoryService.update(id,categoryDto));
    }
}