package com.school.project.controller;

import com.school.project.dto.AvatarDto;
import com.school.project.dto.ErrorDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Avatar;
import com.school.project.service.AvatarService;
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
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("avatar")
@RequiredArgsConstructor
@Tag(name = "Avatar", description = "Avatar items")
public class AvatarController {
    private final AvatarService avatarService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping
    public ResponseEntity<ResponseStatusDto> create(
            @RequestBody AvatarDto dto
    ){
        ResponseStatusDto avatar = avatarService.create(dto);
        return ResponseEntity.ok(avatar);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @GetMapping
    public ResponseEntity<AvatarDto> getAvatar(Long id){
        return ResponseEntity.ok(avatarService.getOne(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok"),
            @ApiResponse(responseCode = "201", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))})
    @DeleteMapping("{id}")
    public ResponseEntity<ResponseStatusDto> delete(
            @PathVariable long id
    ){
        return ResponseEntity.ok(avatarService.delete(id));
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
            @RequestBody AvatarDto dto
    ){
        return ResponseEntity.ok(avatarService.update(id,dto));
    }

}
