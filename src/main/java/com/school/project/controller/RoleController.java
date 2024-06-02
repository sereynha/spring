package com.school.project.controller;

import com.school.project.dto.ErrorDto;
import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.RoleDto;
import com.school.project.model.Role;
import com.school.project.service.RoleService;
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
@RequestMapping("api/roles")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Role", description = "Role items")
public class RoleController {

    private final RoleService roleService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    public ResponseEntity<Role> create(
            @RequestBody RoleDto dto
    ){
        return ResponseEntity.ok(roleService.create(dto));
    }

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
            @RequestParam(name = "sort", required = false, defaultValue = "name:asc") String sort
    ){
        return ResponseEntity.ok(new PageDto(roleService.getAll(page,limit,sort)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("{id}")
    public ResponseEntity<ResponseStatusDto> update(
            @PathVariable("id") Long id,
            @RequestBody RoleDto dto
    ){
        return ResponseEntity.ok(roleService.update(id,dto));
    }
}
