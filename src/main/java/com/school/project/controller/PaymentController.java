package com.school.project.controller;

import com.school.project.dto.ErrorDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.PaymentDto;
import com.school.project.model.Payment;
import com.school.project.service.PaymentService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Payment", description = "Payment items")
public class PaymentController {
    private final PaymentService paymentService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("payment")
    public ResponseEntity<Payment> create(
            @RequestBody PaymentDto dto
            ){
        return ResponseEntity.ok(paymentService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("payment")
    public ResponseEntity<PageDto> getByCourseId(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "10") int limit,
            @RequestParam(name = "sort", required = false, defaultValue = "createAt:asc") String sort

    ){
        return ResponseEntity.ok(new PageDto(paymentService.getAll(page,limit,sort)));
    }

}
