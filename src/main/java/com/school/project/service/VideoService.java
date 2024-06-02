package com.school.project.service;

import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.VideoDto;
import com.school.project.model.Video;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface VideoService {
    Map<Integer, String> uploadMultiVideo(MultipartFile file);
    Video create(VideoDto dto);
    ResponseStatusDto update(Long id, VideoDto dto);
    Page<VideoDto> getRelatedCourseStoreFront(Long id,int page, int limit);
    Page<Video> getVideoMultiQuery(int page, int limit,String sort, String title,String slug);
    Video getOne(Long id);
    ResponseStatusDto delete(Long id);
    ResponseStatusDto deleteByCourse(Long id);
 }
