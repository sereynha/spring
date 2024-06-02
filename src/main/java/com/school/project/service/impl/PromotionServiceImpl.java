package com.school.project.service.impl;

import com.school.project.dto.PromotionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PromotionMapper;
import com.school.project.model.Promotion;
import com.school.project.repository.PromotionRepository;
import com.school.project.service.PageService;
import com.school.project.service.PromotionService;
import com.school.project.specifications.PromotionFilter;
import com.school.project.specifications.PromotionSpecifications;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;


@Service
@Transactional
@RequiredArgsConstructor
public class PromotionServiceImpl implements PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    @Override
    public ResponseStatusDto create(PromotionDto dto) {
        if (promotionRepository.existsBySlugLikeAndIsActiveTrue(dto.getSlug()))
            throw new DuplicatedException(Constants.ERROR_CODE.SLUG_ALREADY_EXISTED,dto.getSlug());
        Promotion promotion = promotionMapper.toEntity(dto);
        promotion.setIsActive(true);
        promotionRepository.save(promotion);
        return new ResponseStatusDto("Create Promotion", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.CREATED.toString());
    }

    @Override
    public Page<Promotion> getAll(int page, int limit, String sort , String name, LocalDateTime startDate, LocalDateTime endDate) {
        PromotionFilter filter = new PromotionFilter();
        filter.setName(name);
        filter.setStartDate(startDate);
        filter.setEndDate(endDate);

        Specification<Promotion> specification = new PromotionSpecifications(filter);
        Pageable pageable = PageService.getPageable(page, limit, sort);
        return promotionRepository.findAll(specification, pageable);
    }

    @Override
    public Promotion getById(Long id) {
        return promotionRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.PROMOTION_NOT_FOUND,id));
    }

    @Override
    public ResponseStatusDto update(Long id,boolean activation) {
        Promotion promotion = getById(id);
        promotion.setIsActive(activation);
        return new ResponseStatusDto("Create Promotion", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

}