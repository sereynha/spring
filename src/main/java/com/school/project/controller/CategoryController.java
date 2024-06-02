package com.school.project.controller;

import com.school.project.dto.CategoryDto;
import com.school.project.dto.ErrorDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Category;
import com.school.project.service.CategoryService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("category")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Category", description = "Category items")
public class CategoryController {
    private final CategoryService categoryService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping()
    public ResponseEntity<ResponseStatusDto> create(
            @RequestBody CategoryDto dto
    ){
        return ResponseEntity.ok(categoryService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping
    public ResponseEntity<PageDto> getCategory(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sort", required = false, defaultValue = "name:asc") String sort,
            @RequestParam Map<String,String> params
    ){
        Page<Category> data =categoryService.getAll(page,limit,sort,params);
        PageDto found = new PageDto(data);
        return ResponseEntity.ok(found);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<Category> findOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("{id}")
    public ResponseEntity<ResponseStatusDto> update(
            @PathVariable("id") Long id,
            @RequestBody CategoryDto categoryDto
    ){
        return ResponseEntity.ok(categoryService.update(id,categoryDto));
    }

}
