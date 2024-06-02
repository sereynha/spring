package com.school.project.service;

import com.school.project.dto.PromotionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PromotionMapper;
import com.school.project.model.Course;
import com.school.project.model.Promotion;
import com.school.project.repository.PromotionRepository;
import com.school.project.service.impl.PromotionServiceImpl;
import com.school.project.specifications.PromotionFilter;
import com.school.project.specifications.PromotionSpecifications;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PromotionServiceTest {

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PromotionMapper promotionMapper;

    private PromotionService promotionService;
    private Promotion promotionOne;
    private Promotion promotionTwo;
    private Course course;
    private List<Promotion> promotionList;
    private PromotionDto promotionDto;
    private PromotionFilter filter;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @BeforeEach
    public void setUp() {
        promotionService = new PromotionServiceImpl(promotionRepository,promotionMapper);

        startDate = LocalDateTime.parse("2024-05-20T11:49:08.971");
        endDate = LocalDateTime.parse("2024-05-20T11:49:08.971");

        course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setPrice(BigDecimal.valueOf(100));
        course.setIsActive(false);

        promotionOne = new Promotion();
        promotionOne.setCourse(Set.of(course));
        promotionOne.setId(1L);
        promotionOne.setName("Summer sale 2024");
        promotionOne.setSlug("test-slug");
        promotionOne.setDescription("test-description");
        promotionOne.setDiscountPercentage(20L);
        promotionOne.setAmountCourse(2L);
        promotionOne.setPriceAmount(BigDecimal.valueOf(88.9));
        promotionOne.setIsActive(true);
        promotionOne.setStartDate(startDate);
        promotionOne.setEndDate(endDate);

        promotionTwo = new Promotion();
        promotionTwo.setCourse(Set.of(course));
        promotionTwo.setId(2L);
        promotionTwo.setName("Summer sale 2024");
        promotionTwo.setSlug("test-slug");
        promotionTwo.setDescription("test-description");
        promotionTwo.setDiscountPercentage(20L);
        promotionTwo.setAmountCourse(2L);
        promotionTwo.setPriceAmount(BigDecimal.valueOf(88.9));
        promotionTwo.setIsActive(true);
        promotionTwo.setStartDate(startDate);
        promotionTwo.setEndDate(endDate);

        promotionDto = new PromotionDto();
        promotionDto.setName("Summer sale 2024");
        promotionDto.setSlug("test-slug");
        promotionDto.setDescription("test-description");
        promotionDto.setDiscountPercentage(20L);
        promotionDto.setAmountCourse(2L);
        promotionDto.setPriceAmount(BigDecimal.valueOf(88.9));
        promotionDto.setStartDate(startDate);
        promotionDto.setEndDate(endDate);
        promotionDto.setCourseId(List.of(1L,2L));

        promotionList = Arrays.asList(promotionOne, promotionTwo);
    }

    @Test
    public void testCreatePromotionSuccess() {
        // when
        when(promotionRepository.existsBySlugLikeAndIsActiveTrue("test-slug")).thenReturn(false);
        when(promotionMapper.toEntity(promotionDto)).thenReturn(promotionOne);
        when(promotionRepository.save(promotionOne)).thenReturn(promotionOne);

        ResponseStatusDto response = promotionService.create(promotionDto);

        // then
        assertEquals("Create Promotion", response.getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getMessage());
        assertEquals(HttpStatus.CREATED.toString(), response.getStatusCode());
        assertEquals(promotionDto.getName(), promotionOne.getName());
        assertEquals(promotionDto.getSlug(), promotionOne.getSlug());
        assertEquals(promotionDto.getStartDate(), promotionOne.getStartDate());
        assertEquals(promotionDto.getAmountCourse(), promotionOne.getAmountCourse());
        assertEquals(promotionDto.getDiscountPercentage(), promotionOne.getDiscountPercentage());
        assertEquals(promotionDto.getDescription(), promotionOne.getDescription());
        assertEquals(promotionDto.getPriceAmount(), promotionOne.getPriceAmount());
        assertEquals(promotionDto.getEndDate(), promotionOne.getEndDate());
        verify(promotionRepository, times(1)).save(promotionOne);
        verify(promotionMapper,times(1)).toEntity(promotionDto);
    }

    @Test
    public void testCreatePromotionDuplicated() {
        // when
        when(promotionRepository.existsBySlugLikeAndIsActiveTrue("test-slug")).thenReturn(true);

        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> {
            promotionService.create(promotionDto);
        });

        // then
        verify(promotionRepository).existsBySlugLikeAndIsActiveTrue("test-slug");
        assertEquals("SLUG_ALREADY_EXISTED is test-slug", exception.getMessage());

    }

    @Test
    public void testGetAllPromotions() {
        // give
        filter = new PromotionFilter();
        filter.setName("Summer sale 2024");
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);
        PromotionSpecifications specifications = new PromotionSpecifications(filter);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        Page<Promotion> page = new PageImpl<>(promotionList);

        // when
        when(promotionRepository.findAll(specifications, pageable)).thenReturn(page);
        Page<Promotion> result = promotionService.getAll(1, 10, "name:asc", "Summer sale 2024", startDate, endDate);
        // then
        verify(promotionRepository, times(1)).findAll(specifications, pageable);
        assertNotNull(result);
        assertThat(result).isEqualTo(page);
    }

    @Test
    public void testGetByName() {
        // when
        when(promotionRepository.findById(promotionOne.getId())).thenReturn(Optional.of(promotionOne));
        Promotion result = promotionService.getById(promotionOne.getId());

        // then
        assertNotNull(result);
        assertEquals(promotionOne.getId(),result.getId());
        assertEquals(promotionOne, result);
        assertEquals(promotionOne.getName(),result.getName());
        assertEquals(promotionOne.getSlug(),result.getSlug());
        assertEquals(promotionOne.getDescription(),result.getDescription());
        assertEquals(promotionOne.getAmountCourse(),result.getAmountCourse());
        assertEquals(promotionOne.getPriceAmount(),result.getPriceAmount());
        assertEquals(promotionOne.getDiscountPercentage(),result.getDiscountPercentage());
        assertEquals(promotionOne.getIsActive(),result.getIsActive());
        assertEquals(promotionOne.getStartDate(),result.getStartDate());
        assertEquals(promotionOne.getEndDate(),result.getEndDate());
        assertEquals(promotionOne.getCourse(),result.getCourse());
        verify(promotionRepository, times(1)).findById(promotionOne.getId());
    }

    @Test
    public void testGetByNameNotFound() {
        // when
        when(promotionRepository.findById(promotionOne.getId())).thenReturn(Optional.empty());
        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            promotionService.getById(promotionOne.getId());
        });

        /// when
        assertEquals("PROMOTION_NOT_FOUND With id = 1 not found", thrown.getMessage());
        verify(promotionRepository, times(1)).findById(promotionOne.getId());
    }

    @Test
    void updatePromotionActivation() {
        when(promotionRepository.findById(1L)).thenReturn(Optional.of(promotionOne));

        ResponseStatusDto response = promotionService.update(1L, false);

        assertFalse(promotionOne.getIsActive());
        assertEquals(HttpStatus.OK.toString(), response.getStatusCode());
    }
}