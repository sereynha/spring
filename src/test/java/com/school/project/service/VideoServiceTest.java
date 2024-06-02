package com.school.project.service;

import com.school.project.dto.VideoDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.VideoMapper;
import com.school.project.model.Course;
import com.school.project.model.Video;
import com.school.project.repository.CourseRepository;
import com.school.project.repository.VideoRepository;
import com.school.project.service.impl.VideoServiceImpl;
import com.school.project.specifications.VideoFilter;
import com.school.project.specifications.VideoSpecifications;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private VideoMapper videoMapper;

    @Captor
    private ArgumentCaptor<Video> captor;

    private VideoService videoService;
    private  Course course;
    private Video video;
    private VideoDto videoDto;

    @BeforeEach
    void setUp() {
        videoService = new VideoServiceImpl(videoRepository,courseRepository,videoMapper);

        course = new Course();
        course.setId(1L);
        course.setName("Spring Boot");
        course.setLectures(20L);
        course.setPrice(BigDecimal.valueOf(69));
        course.setTotalHours( BigDecimal.valueOf(12));

        video = new Video();
        video.setId(1L);
        video.setPublished(true);
        video.setSlug("springboot");
        video.setTitle("Spring Boot");
        video.setLinkUrl("https://test.com/video");
        video.setImageCover("https://test.com/image.jpg");
        video.setCourse(course);

        videoDto = new VideoDto();
        videoDto.setPublished(true);
        videoDto.setCourseId(1L);
        videoDto.setSlug("springboot");
        videoDto.setTitle("Spring Boot");
        videoDto.setLinkUrl("http://link.url");
        videoDto.setImageCover("http://image.cover");

    }

    @Test
    void testUploadMultiVideo() throws IOException {
        // given
        VideoDto dto = new VideoDto("slug-1", "title-1", "http://link.url", "http://image.cover", true, 101L);
        Video video = new Video();
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("video");
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("RowNumber");
        headerRow.createCell(1).setCellValue("CourseId");
        headerRow.createCell(2).setCellValue("Slug");
        headerRow.createCell(3).setCellValue("Title");
        headerRow.createCell(4).setCellValue("LinkURL");
        headerRow.createCell(5).setCellValue("ImageCover");
        headerRow.createCell(6).setCellValue("IsPublished");

        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(1);
        dataRow.createCell(1).setCellValue(101);
        dataRow.createCell(2).setCellValue("slug-1");
        dataRow.createCell(3).setCellValue("title-1");
        dataRow.createCell(4).setCellValue("http://link.url");
        dataRow.createCell(5).setCellValue("http://image.cover");
        dataRow.createCell(6).setCellValue(true);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());

        // when
        MultipartFile file = mock(MultipartFile.class);
        when(file.getInputStream()).thenReturn(inputStream);

        when(videoMapper.toEntity(dto)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);

        Map<Integer, String> result = videoService.uploadMultiVideo(file);

        // then
        assertTrue(result.isEmpty());
        verify(videoRepository, times(1)).save(video);
    }

    @Test
    void testCreate() {
        // when
        when(videoMapper.toEntity(videoDto)).thenReturn(video);
        when(videoRepository.save(video)).thenReturn(video);
        videoService.create(videoDto);

        // then
        verify(videoRepository, times(1)).save(video);
        verify(videoMapper, times(1)).toEntity(videoDto);
    }

    @Test
    void testUpdate() {
        // give
        Long id = 1L;

        // when
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        when(videoRepository.save(video)).thenReturn(video);
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        videoService.update(id, videoDto);

        // then
        verify(videoRepository, times(1)).findById(id);
        verify(videoRepository).save(captor.capture());
        assertEquals(id, captor.getValue().getId());
        assertEquals(video.isPublished(), captor.getValue().isPublished());
    }

    @Test
    void testGetRelatedCourseStoreFront() {
        // given
        Long id = 1L;

        // when
        when(courseRepository.findById(id)).thenReturn(Optional.of(course));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        Page<Video> pageResult = new PageImpl<>(Collections.singletonList(video));
        when(videoRepository.findAllByCourse(course, pageable)).thenReturn(pageResult);
        when(videoMapper.toDto(video)).thenReturn(videoDto);
        videoService.getRelatedCourseStoreFront(id, 1, 10);

        // then
        verify(courseRepository).findById(id);
        verify(videoRepository).findAllByCourse(course, pageable);
        verify(videoMapper).toDto(video);
    }

    @Test
    void testGetVideoMultiQuery() {
        // give
        VideoFilter filter = new VideoFilter();
        filter.setTitle("testTitle");
        filter.setSlug("testSlug");
        VideoSpecifications specifications = new VideoSpecifications(filter);
        Page<Video> pageResult = new PageImpl<>(Collections.singletonList(video));
        Pageable pageable = PageRequest.of(0, 10, Sort.by("title").ascending());

        // when
        when(videoRepository.findAll(specifications, pageable)).thenReturn(pageResult);
        Page<Video> result = videoService.getVideoMultiQuery(1,10,"title:asc","testTitle","testSlug");

        // then
        verify(videoRepository, times(1)).findAll(specifications, pageable);
        assertEquals(result,pageResult);
    }

    @Test
    void testGetOne() {
        //  give
        Long id = 1L;

        // when
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        Video foundVideo = videoService.getOne(id);

        // then
        verify(videoRepository,times(1)).findById(id);
        assertEquals(video.getId(),foundVideo.getId());
        assertEquals(video.getSlug(),foundVideo.getSlug());
        assertEquals(video.isPublished(),foundVideo.isPublished());
        assertEquals(video.getCourse(),foundVideo.getCourse());
        assertEquals(video.getLinkUrl(),foundVideo.getLinkUrl());
        assertEquals(video.getTitle(),foundVideo.getTitle());
        assertEquals(video.getImageCover(),foundVideo.getImageCover());
        assertNotNull(foundVideo);
    }

    @Test
    void testDelete() {
        //  give
        Long id = 1L;

        // when
        when(videoRepository.findById(id)).thenReturn(Optional.of(video));
        videoService.delete(id);

        // then
        verify(videoRepository, times(1)).delete(video);
    }


    @Test
    void testDeleteByCourse() {
        //  give
        Long id = 1L;
        List<Video> videos = Collections.singletonList(video);

        // when
        when(videoRepository.findAllByCourseId(id)).thenReturn(videos);
         videoService.deleteByCourse(id);

         // then
        verify(videoRepository, times(1)).deleteAll(videos);
    }

    @Test
    void testDeleteThrowNotFoundException() {
        //  give
        Long id = 1L;

        //when
        when(videoRepository.findAllByCourseId(id)).thenReturn(Collections.emptyList());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> videoService.deleteByCourse(id));

        //then
        assertEquals("VIDEO_NOT_FOUND With id = 1 not found", exception.getMessage());
    }


}