package com.school.project.service.impl;

import com.school.project.dto.RatingDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.RatingMapper;
import com.school.project.model.Rating;
import com.school.project.repository.RatingRepository;
import com.school.project.service.PageService;
import com.school.project.service.RatingService;
import com.school.project.specifications.RatingFilter;
import com.school.project.specifications.RatingSpecifications;
import com.school.project.utils.AuthenticationUtils;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final RatingMapper ratingMapper;

    @Override
    public Rating create(RatingDto dto) {
        String userAuth = AuthenticationUtils.extractUser();
        log.info("Auth User CreateBy: {}", userAuth);
        Rating rating;
        if (ratingRepository.existsByCreatedByAndCourseId(userAuth,dto.getCourseId())) {
            Rating existingRating = ratingRepository.findByCourseId(dto.getCourseId());
            if (existingRating == null) {
                throw new NotFoundException(Constants.ERROR_CODE.RESOURCE_ALREADY_EXISTED ,dto.getCourseId());
            }
            rating = ratingMapper.partialUpdate(dto, existingRating);
        } else {
            rating  = ratingMapper.toEntity(dto);
        }
        return ratingRepository.save(rating);
    }

    @Override
    public Page<RatingDto> getAllRatingByCourseId(Long courseId,int page,int limit) {
        Pageable pageable = PageService.getPageable(page,limit, String.valueOf(Sort.by("createAt")));
        Page<Rating> ratings = ratingRepository.findAllByCourseId(courseId,pageable);
        List<RatingDto> ratingDto = ratings.getContent()
                .stream().map(
                        ratingMapper::toDto
                ).toList();
        return  new PageImpl<>(ratingDto);
    }

    @Override
    public Page<RatingDto> getAll( String message, LocalDateTime createFrom, LocalDateTime createTo,int page, int limit) {
        RatingFilter filter = new RatingFilter();
        filter.setMessage(message);
        filter.setCreateTo(createTo);
        filter.setCreateFrom(createFrom);

        Specification<Rating> specification = new RatingSpecifications(filter);
        Pageable pageable = PageService.getPageable(page, limit, String.valueOf(Sort.by("createAt").descending()));
        Page<Rating> ratings = ratingRepository.findAll(specification, pageable);
        List<RatingDto> ratingDto = ratings.getContent()
                .stream().map(
                        ratingMapper::toDto
                ).toList();
        return new PageImpl<>(ratingDto);
    }

    @Override
    public ResponseStatusDto delete(Long id) {
        var rating = ratingRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.RATING_NOT_FOUND, id));
        ratingRepository.delete(rating);
        return new ResponseStatusDto("Delete Rating", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public Double calculateAverageStar(Long courseId) {
        List<Object[]> totalStarsAndRatings = ratingRepository.getTotalStarsAndTotalRatings(courseId);
        if (ObjectUtils.isEmpty(totalStarsAndRatings.get(0)[0]))
            return 0.0;
        int totalStars = (Integer.parseInt(totalStarsAndRatings.get(0)[0].toString()));
        int totalRatings = (Integer.parseInt(totalStarsAndRatings.get(0)[1].toString()));

        Double averageStars = (totalStars * 1.0) / totalRatings;
        log.info("Average Star: {}", averageStars);
        return averageStars;
    }
}
