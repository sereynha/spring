package com.school.project.service;

import com.school.project.dto.PaymentDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PaymentMapper;
import com.school.project.model.Course;
import com.school.project.model.Payment;
import com.school.project.model.Promotion;
import com.school.project.model.enumeration.EnumPaymentMethod;
import com.school.project.model.enumeration.EnumPaymentStatus;
import com.school.project.repository.CourseRepository;
import com.school.project.repository.PaymentRepository;
import com.school.project.repository.PromotionRepository;
import com.school.project.service.impl.PaymentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private PromotionRepository promotionRepository;

    @Mock
    private PaymentMapper paymentMapper;

    private PaymentService paymentService;

    private PaymentDto paymentDto;
    private Payment payment;
    private List<Course> courses;
    private Promotion promotion;

    @BeforeEach
    void setUp() {
        paymentService = new PaymentServiceImpl(paymentRepository,courseRepository,promotionRepository,paymentMapper);

        paymentDto = new PaymentDto();
        paymentDto.setPaymentMethod(EnumPaymentMethod.BANKING);
        paymentDto.setPaymentStatus(EnumPaymentStatus.PENDING);
        paymentDto.setTotalPayment(BigDecimal.valueOf(90));
        paymentDto.setCoursesId(List.of(1L, 2L));
        paymentDto.setPromotionsId(1L);

        Course course1 = new Course();
        course1.setId(1L);
        course1.setName("Spring Boot");
        course1.setPrice(BigDecimal.valueOf(100));
        course1.setIsActive(false);

        Course course2 = new Course();
        course2.setId(2L);
        course2.setName("Spring JPA");
        course2.setPrice(BigDecimal.valueOf(200));
        course2.setIsActive(false);

        courses = List.of(course1, course2);

        promotion = new Promotion();
        promotion.setId(1L);
        promotion.setCourse(Set.of(course1));
        promotion.setDiscountPercentage(10L);

        payment = new Payment();
        payment.setPaymentMethod(EnumPaymentMethod.BANKING);
        payment.setPaymentStatus(EnumPaymentStatus.PENDING);
        payment.setTotalPayment(BigDecimal.valueOf(90));
        payment.setId(1L);
    }

    @Test
    void createWithValidDataShouldCreatePayment() {
        // given
        List<Long> coursesId = courses.stream().map(Course::getId).toList();

        // when
        when(courseRepository.findAllById(coursesId)).thenReturn(courses);
        when(promotionRepository.findById(promotion.getId())).thenReturn(Optional.of(promotion));
        when(paymentMapper.toEntity(paymentDto)).thenReturn(payment);
        when(paymentRepository.existsByCourseId(1L)).thenReturn(false);
        when(paymentRepository.save(payment)).thenReturn(payment);

        Payment result = paymentService.create(paymentDto);

        // then
        assertNotNull(result);
        assertEquals(payment.getId(),result.getId());
        assertEquals(payment.getPaymentMethod(),result.getPaymentMethod());
        assertEquals(payment.getPaymentStatus(),result.getPaymentStatus());
        assertEquals(payment.getTotalPayment(),result.getTotalPayment());
        assertEquals(payment.getCourse(),result.getCourse());
        assertEquals(paymentDto.getPaymentMethod(),result.getPaymentMethod());
        assertEquals(paymentDto.getPaymentStatus(),result.getPaymentStatus());
        assertEquals(paymentDto.getTotalPayment(),result.getTotalPayment());
        verify(courseRepository, times(1)).findAllById(coursesId);
        verify(promotionRepository, times(1)).findById(1L);
        verify(paymentMapper, times(1)).toEntity(paymentDto);
        verify(paymentRepository, times(1)).existsByCourseId(1L);
        verify(paymentRepository, times(1)).save(payment);
    }

    @Test
    void createWithInvalidPromotionShouldThrowNotFoundException() {
        // given
        List<Long> coursesId = courses.stream().map(Course::getId).toList();

        // when
        when(courseRepository.findAllById(coursesId)).thenReturn(courses);
        when(promotionRepository.findById(any())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> {
            paymentService.create(paymentDto);
        });

        // then
        assertEquals("Promotion With id = 1 not found", exception.getMessage());
        verify(promotionRepository, times(1)).findById(any());
    }
    @Test
    void testCreatePaymentCourseAlreadyExisted() {
        // given
        Course course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setPrice(BigDecimal.valueOf(200));
        course.setIsActive(false);

        Promotion promotion = new Promotion();
        promotion.setId(1L);
        promotion.setCourse(Set.of(course));
        promotion.setDiscountPercentage(10L);

        // when
        when(courseRepository.findAllById(paymentDto.getCoursesId())).thenReturn(courses);
        when(promotionRepository.findById(paymentDto.getPromotionsId())).thenReturn(Optional.of(promotion));
        when(paymentRepository.existsByCourseId(anyLong())).thenReturn(true);
        DuplicatedException exception = assertThrows(DuplicatedException.class, () -> paymentService.create(paymentDto));

        // when
        assertEquals("COURSE_ALREADY_PAYMENT_EXISTED is Spring Boot", exception.getMessage());
    }
    @Test
    void getAllWithValidPageAndLimitShouldReturnPagedPayments() {
        // given
        int page = 1;
        int limit = 10;
        String sort = "createAt:asc";
        Pageable pageable = PageRequest.of(0, limit, Sort.by("createAt"));
        List<Payment> payments = Collections.singletonList(payment);
        Page<Payment> paymentPage = new PageImpl<>(payments);

        // when
        when(paymentRepository.findAll(pageable)).thenReturn(paymentPage);
        Page<Payment> result = paymentService.getAll(page, limit, sort);

        // then
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(1, result.getContent().size());
        verify(paymentRepository, times(1)).findAll(pageable);
    }
}
