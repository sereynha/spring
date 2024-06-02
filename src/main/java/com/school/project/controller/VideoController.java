package com.school.project.controller;

import com.school.project.dto.*;
import com.school.project.model.Video;
import com.school.project.service.VideoService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
@Tag(name = "Video", description = "Video items")
public class VideoController {

    private final VideoService videoService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("/video")
    public ResponseEntity<Video> create(
            @RequestBody VideoDto dto
    ){
        return ResponseEntity.ok(videoService.create(dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PutMapping("video/{id}")
    public ResponseEntity<ResponseStatusDto> update(
            @PathVariable("id") Long id, @RequestBody  VideoDto dto
    ){
        return ResponseEntity.ok(videoService.update(id,dto));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("video/slug={slug}")
    public ResponseEntity<Page<Video>> getAllVideoMultiQuery(
            @RequestParam(name = "Page", defaultValue = "1") int page,
            @RequestParam(name = "Limit", defaultValue = "10") int limit,
            @PathVariable("slug") String slug,
            @RequestParam(name = "sort", required = false, defaultValue = "title:asc") String sort,
            @RequestParam(required = false,name = "Title", defaultValue = "s") String title
    ){
        return ResponseEntity.ok(videoService.getVideoMultiQuery(page,limit,sort,title,slug));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("course/{id}/video")
    public ResponseEntity<Page<VideoDto>> getRelatedCourseStoreFront(
            @PathVariable("id") Long courseId,
            @RequestParam(name = "Page", defaultValue = "1") int page,
            @RequestParam(name = "Limit", defaultValue = "10") int limit
    ){
        return ResponseEntity.ok(videoService.getRelatedCourseStoreFront(courseId,page,limit));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @PostMapping("video/uploadMultiVideo")
    public ResponseEntity<?> uploadMultiVideo(@RequestParam("file") MultipartFile file) {
        Map<Integer, String> errorMap = videoService.uploadMultiVideo(file);
        return ResponseEntity.ok(errorMap);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @GetMapping("video/{id}")
    public ResponseEntity<Video> getOne(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(videoService.getOne(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("video/{id}")
    public ResponseEntity<ResponseStatusDto> delete(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(videoService.delete(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create Successful"),
            @ApiResponse(responseCode = "204", description = "Update Successful Is No Content"),
            @ApiResponse(responseCode = "404", description = "Not Found", content = @Content(schema = @Schema(implementation = ErrorDto.class))),
            @ApiResponse(responseCode = "400", description = "Bad Request", content = @Content(schema = @Schema(implementation = ErrorDto.class)))
    })
    @DeleteMapping("course/{id}/video")
    public ResponseEntity<ResponseStatusDto> deleteByCourse(
            @PathVariable("id") Long id
    ){
        return ResponseEntity.ok(videoService.deleteByCourse(id));
    }

}

