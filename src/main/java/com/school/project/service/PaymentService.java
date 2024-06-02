package com.school.project.service;

import com.school.project.dto.PaymentDto;
import com.school.project.model.Payment;
import org.springframework.data.domain.Page;

public interface PaymentService {
    Payment create(PaymentDto dto);
    Page<Payment> getAll(int page, int limit,String sort);
}
