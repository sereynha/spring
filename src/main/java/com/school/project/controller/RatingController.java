package com.school.project.controller;

import com.school.project.dto.ErrorDto;
import com.school.project.dto.RatingDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Rating;
import com.school.project.service.RatingService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("rating")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Rating", description = "Rating items")
public class RatingController {
    private final RatingService ratingService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping()
    public ResponseEntity<Rating> create(
            @RequestBody RatingDto dto
    ){
        return ResponseEntity.ok(ratingService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseStatusDto> delete(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(ratingService.delete(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/courses/{courseId}")
    public ResponseEntity<Page<RatingDto>> getByCourseId(
            @PathVariable Long courseId,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit

    ){
        return ResponseEntity.ok(ratingService.getAllRatingByCourseId(courseId,page,limit));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping
    public ResponseEntity<Page<RatingDto>> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "message", defaultValue = "", required = false) String message,
            @RequestParam(value = "createFrom", defaultValue = "#{new java.util.Date()}", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
            @RequestParam(value = "createTo", defaultValue = "#{new java.util.Date()}", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo
    ){
        return ResponseEntity.ok(ratingService.getAll(message,createdFrom,createdTo,page,limit));
    }

    @GetMapping("/course/{courseId}/average-star")
    public Double getAverageStarOfProduct(@PathVariable Long courseId) {
        return ratingService.calculateAverageStar(courseId);
    }
}
