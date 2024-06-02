package com.school.project.service.impl;

import com.school.project.dto.PaymentDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PaymentMapper;
import com.school.project.model.Course;
import com.school.project.model.Payment;
import com.school.project.model.Promotion;
import com.school.project.repository.CourseRepository;
import com.school.project.repository.PaymentRepository;
import com.school.project.repository.PromotionRepository;
import com.school.project.service.PageService;
import com.school.project.service.PaymentService;
import com.school.project.utils.Constants;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CourseRepository courseRepository;
    private final PromotionRepository promotionRepository;
    private final PaymentMapper paymentMapper;


    @Override
    public Payment create(PaymentDto dto) {
        List<Course> courses = courseRepository.findAllById(dto.getCoursesId());
        Promotion promotions = promotionRepository.findById(dto.getPromotionsId()).orElseThrow(() -> new NotFoundException("Promotion",dto.getPromotionsId()));
        List<Long> promotionCourseId = promotions.getCourse().stream().map(Course::getId).toList();
        BigDecimal totalPayment = BigDecimal.ZERO;

        for (Course course : courses) {
            if (paymentRepository.existsByCourseId(course.getId()))
                throw new DuplicatedException(Constants.ERROR_CODE.COURSE_ALREADY_EXISTED, course.getName());
            if (dto.getCoursesId().contains(course.getId())){
                BigDecimal newPrice = course.getPrice();
                if (promotionCourseId.contains(course.getId())) {
                    if (promotions.getAmountCourse() != null) {
                        newPrice = promotions.getPriceAmount();

                    } else if (promotions.getDiscountPercentage() != null) {
                        newPrice = course.getPrice().multiply(BigDecimal.valueOf(100 - promotions.getDiscountPercentage()))
                                .divide(BigDecimal.valueOf(100));
                    }
                    totalPayment = totalPayment.add(newPrice);
                    course.setIsActive(true);
                    break;
                }
            }

        }

        dto.setTotalPayment(totalPayment);
        Payment payment = paymentMapper.toEntity(dto);
        return paymentRepository.save(payment);
    }

    @Override
    public Page<Payment> getAll(int page, int limit, String sort) {
        Pageable pageable = PageService.getPageable(page,limit, sort);
        return paymentRepository.findAll(pageable);
    }
}
