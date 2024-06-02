package com.school.project.controller;

import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.VideoDto;
import com.school.project.model.Video;
import com.school.project.service.VideoService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class VideoControllerTest {

    private VideoController videoController;
    private VideoService videoService;
    private VideoDto videoDto;
    private Video video;

    @BeforeEach
    void setUp() {
        videoService = mock(VideoService.class);
        videoController = new VideoController(videoService);

        video = new Video();
        video.setId(1L);
        video.setPublished(true);
        video.setSlug("springboot");
        video.setTitle("Spring Boot");
        video.setLinkUrl("https://test.com/video");
        video.setImageCover("https://test.com/image.jpg");

        videoDto = new VideoDto();
        videoDto.setPublished(true);
        videoDto.setCourseId(1L);
        videoDto.setSlug("springboot");
        videoDto.setTitle("Spring Boot");
        videoDto.setLinkUrl("http://link.url");
        videoDto.setImageCover("http://image.cover");
    }

    @Test
    void testCreate() {
        // when
        when(videoService.create(videoDto)).thenReturn(video);
        ResponseEntity<Video> response = videoController.create(videoDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(videoService, times(1)).create(videoDto);

    }

    @Test
    void testUpdate() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Video","SUCCESS","200 OK");

        // when
        when(videoService.update(id, videoDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = videoController.update(id, videoDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Video", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(videoService, times(1)).update(id, videoDto);
    }

    @Test
    void testGetAllVideoMultiQuery() {
        // given
        PageImpl<Video> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(videoService.getVideoMultiQuery(anyInt(), anyInt(), anyString(), anyString(), anyString()))
                .thenReturn(page);
        ResponseEntity<Page<Video>> response = videoController.getAllVideoMultiQuery(1, 10, "springboot" ,"title:asc", "Spring Boot");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().getSize());
        assertEquals(0, response.getBody().getTotalPages());
        assertEquals(0, response.getBody().getTotalElements());
        verify(videoService, times(1)).getVideoMultiQuery(1, 10 ,"title:asc", "Spring Boot","springboot");
    }

    @Test
    void getRelatedCourseStoreFront(){
        // given
        PageImpl<VideoDto> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 10), 0);

        // when
        when(videoService.getRelatedCourseStoreFront(anyLong(),anyInt(), anyInt()))
                .thenReturn(page);
        ResponseEntity<Page<VideoDto>> response = videoController.getRelatedCourseStoreFront(1L,1, 10);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(10, response.getBody().getSize());
        verify(videoService, times(1)).getRelatedCourseStoreFront(1L,1, 10);
    }

    @Test
    void getOne(){
        //when
        when(videoService.getOne(1L)).thenReturn(video);
        ResponseEntity<Video> response = videoController.getOne(1L);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(video, response.getBody());
        verify(videoService, times(1)).getOne(1L);
    }

    @Test
    void getDelete(){
        //given
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Delete Video","SUCCESS","200 OK");

        //when
        when(videoService.delete(1L)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = videoController.delete(1L);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete Video", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(videoService, times(1)).delete(id);
    }

    @Test
    void getDeleteByCourse(){
        //given
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Delete Video","SUCCESS","200 OK");

        //when
        when(videoService.deleteByCourse(1L)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = videoController.deleteByCourse(1L);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete Video", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(videoService, times(1)).deleteByCourse(id);
    }

    @Test
    void testUploadMultiVideo() {
        // given
        MultipartFile file = mock(MultipartFile.class);
        Map<Integer, String> errorMap = Collections.emptyMap();

        // when
        when(videoService.uploadMultiVideo(file)).thenReturn(errorMap);
        ResponseEntity<?> response = videoController.uploadMultiVideo(file);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(errorMap, response.getBody());
        verify(videoService, times(1)).uploadMultiVideo(file);
    }
}
