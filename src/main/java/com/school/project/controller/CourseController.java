package com.school.project.controller;

import com.school.project.dto.CourseDto;
import com.school.project.dto.ErrorDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Course;
import com.school.project.service.CourseService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("course")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Course", description = "Course items")
public class CourseController {

    private final CourseService courseService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping()
    public ResponseEntity<ResponseStatusDto> create(
            @RequestBody CourseDto dto
    ){
        return ResponseEntity.ok(courseService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("{id}")
    public ResponseEntity<Course> findOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(courseService.getById(id));
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping
    public ResponseEntity<PageDto> getCourse(
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "limit", defaultValue = "5") int limit,
            @RequestParam(name = "sort", required = false, defaultValue = "name:asc") String sort,
            @RequestParam Map<String,String> params
    ){
        return ResponseEntity.ok(new PageDto(courseService.getAll(page,limit,sort,params)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("{id}")
    public ResponseEntity<ResponseStatusDto> update(
            @PathVariable("id") Long id, @RequestBody CourseDto dto
    ){
        return ResponseEntity.ok(courseService.update(id,dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("many_course/{id}")
    public ResponseEntity<List<Course>> findManyById(
            @PathVariable("id") List<Long> id
    ){
        return ResponseEntity.ok(courseService.findCoursesByIds(id));
    }
}
