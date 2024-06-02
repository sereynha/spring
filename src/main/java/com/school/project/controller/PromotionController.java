package com.school.project.controller;

import com.school.project.dto.ErrorDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.PromotionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Promotion;
import com.school.project.service.PromotionService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("promotions")
@RequiredArgsConstructor
@Tag(name = "Promotion", description = "Promotion items")
public class PromotionController {

    private final PromotionService promotionService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping
    public ResponseEntity<PageDto> getAll(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sort", required = false, defaultValue = "name:asc") String sort,
            @RequestParam(name = "PromotionName", defaultValue = "") String name,
            @RequestParam(value = "startDate", defaultValue = "#{new java.util.Date()}", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(value = "endDate", defaultValue = "#{new java.util.Date()}", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ){
        return ResponseEntity.ok(new PageDto(promotionService.getAll(page,limit,sort,name,startDate,endDate)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseStatusDto> create(
            @Valid @RequestBody PromotionDto dto
    ){
        return ResponseEntity.ok(promotionService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("/id={id}")
    public ResponseEntity<Promotion> getByOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(promotionService.getById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResponseStatusDto> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody boolean activation
    ){
        return ResponseEntity.ok(promotionService.update(id,activation));
    }
}
