package com.school.project.mapper;

import com.school.project.dto.PaymentDto;
import com.school.project.model.Course;
import com.school.project.model.Payment;
import com.school.project.model.Promotion;
import com.school.project.service.CourseService;
import com.school.project.service.PromotionService;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",uses = {CourseService.class})
public interface PaymentMapper {
    @Mapping(target = "course", source = "coursesId")
    Payment toEntity(PaymentDto paymentDto);

}