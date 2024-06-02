package com.school.project.controller;

import com.school.project.dto.PageDto;
import com.school.project.dto.PaymentDto;
import com.school.project.model.Payment;
import com.school.project.model.enumeration.EnumPaymentMethod;
import com.school.project.model.enumeration.EnumPaymentStatus;
import com.school.project.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {
    private PaymentController paymentController;
    private PaymentService paymentService;
    private PaymentDto paymentDto;
    private Payment payment;

    @BeforeEach
    void setUp() {
        paymentService = mock(PaymentService.class);
        paymentController = new PaymentController(paymentService);

        paymentDto = new PaymentDto();
        paymentDto.setPaymentMethod(EnumPaymentMethod.BANKING);
        paymentDto.setPaymentStatus(EnumPaymentStatus.PENDING);
        paymentDto.setTotalPayment(BigDecimal.valueOf(90));
        paymentDto.setCoursesId(List.of(1L, 2L));
        paymentDto.setPromotionsId(1L);


        payment = new Payment();
        payment.setPaymentMethod(EnumPaymentMethod.BANKING);
        payment.setPaymentStatus(EnumPaymentStatus.PENDING);
        payment.setTotalPayment(BigDecimal.valueOf(90));
        payment.setId(1L);
    }

    @Test
    void testCreate() {

        // when
        when(paymentService.create(paymentDto)).thenReturn(payment);
        ResponseEntity<Payment> response = paymentController.create(paymentDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService, times(1)).create(paymentDto);
    }

    @Test
    void testGetByCourseId(){
        // give
        PageImpl<Payment> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(paymentService.getAll(anyInt(), anyInt(), anyString())).thenReturn(page);
        ResponseEntity<PageDto> response = paymentController.getByCourseId(1, 10, "createAt:asc");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0,response.getBody().getList().size());
        verify(paymentService, times(1)).getAll(1, 10, "createAt:asc");
    }
}
