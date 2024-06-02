package com.school.project.service.impl;

import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.VideoDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.VideoMapper;
import com.school.project.model.Course;
import com.school.project.model.Video;
import com.school.project.repository.CourseRepository;
import com.school.project.repository.VideoRepository;
import com.school.project.service.PageService;
import com.school.project.service.VideoService;
import com.school.project.specifications.VideoFilter;
import com.school.project.specifications.VideoSpecifications;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class VideoServiceImpl implements VideoService {

    private final VideoRepository videoRepository;
    private final CourseRepository courseRepository;
    private final VideoMapper videoMapper;

    @Override
    public Map<Integer, String> uploadMultiVideo(MultipartFile file) {
        Map<Integer, String> map = new HashedMap<>();
        try {
            Workbook workbook = new XSSFWorkbook(file.getInputStream());
            Sheet sheet = workbook.getSheet("video");
            Iterator<Row> rowIterator = sheet.iterator();
            if (rowIterator.hasNext()) {
                rowIterator.next();
            }
            while (rowIterator.hasNext()){
                Integer rowNumber = null;
                try{
                    Row row = rowIterator.next();
                    int cellIndex = 0;
                    Cell cellNo = row.getCell(cellIndex++);
                    if (cellNo != null) {
                        rowNumber = (int) cellNo.getNumericCellValue();
                    }
                    Cell cellCourseId = row.getCell(cellIndex++);
                    Long courseId = null;
                    if (cellCourseId != null) {
                        courseId = (long) cellCourseId.getNumericCellValue();
                    }
                    Cell cellSlug = row.getCell(cellIndex++);
                    String slug = cellSlug != null ? cellSlug.getStringCellValue() : null;
                    Cell cellTitle = row.getCell(cellIndex++);
                    String title = cellTitle != null ? cellTitle.getStringCellValue() : null;
                    Cell cellLinkUrl = row.getCell(cellIndex++);
                    String linkUrl = cellLinkUrl != null ? cellLinkUrl.getStringCellValue() : null;
                    Cell cellImageCover = row.getCell(cellIndex++);
                    String imageCover = cellImageCover != null ? cellImageCover.getStringCellValue() : null;
                    Cell cellIsPublished = row.getCell(cellIndex++);
                    boolean isPublished = cellIsPublished != null && cellIsPublished.getBooleanCellValue();
                    VideoDto videoDto = new VideoDto(slug, title, linkUrl, imageCover, isPublished, courseId);
                    this.create(videoDto);
                }catch(Exception e) {
                    map.put(rowNumber, e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return map;
    }

    @Override
    public Video create(VideoDto dto) {
        validateIfVideoWithUrl(dto.getLinkUrl(),dto.getSlug());
         var video = videoMapper.toEntity(dto);
        return videoRepository.save(video);
    }

    @Override
    public ResponseStatusDto update(Long id, VideoDto dto) {
        Video video = this.getOne(id);
        if(dto.getLinkUrl() != null)
            video.setLinkUrl(dto.getLinkUrl());
        if(dto.getTitle() != null)
            video.setTitle(dto.getTitle());
        if (dto.getImageCover() != null)
            video.setImageCover(dto.getImageCover());
        if (video.isPublished() != dto.isPublished())
            video.setPublished(dto.isPublished());
        if (dto.getCourseId() != null){
            Course course = courseRepository.findById(dto.getCourseId())
                    .orElseThrow(() -> new NotFoundException("Course",dto.getCourseId()));
            video.setCourse(course);
        }
        videoRepository.save(video);
        return new ResponseStatusDto("Update Video", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public Page<VideoDto> getRelatedCourseStoreFront(Long courseId, int page, int limit) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course",courseId));
        Pageable pageable = PageService.getPageable(page, limit, String.valueOf(Sort.by("id").descending()));
        Page<Video> videos = videoRepository.findAllByCourse(course,pageable);
        List<VideoDto> videoDto = videos.stream()
                .filter(Video::isPublished)
                .map(
                        videoMapper::toDto
                ).toList();
        return new PageImpl<>(videoDto);
    }

    @Override
    public Page<Video> getVideoMultiQuery(int page, int limit,String sort, String title,String slug) {
        Pageable pageable = PageService.getPageable(page, limit,sort);
        VideoFilter filter = new VideoFilter();
        filter.setTitle(title);
        filter.setSlug(slug);

        Specification<Video> specification = new VideoSpecifications(filter);
        return videoRepository.findAll(specification, pageable);
    }

    @Override
    public Video getOne(Long id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(Constants.ERROR_CODE.VIDEO_NOT_FOUND,id));
    }

    @Override
    public ResponseStatusDto delete(Long id) {
        Video video = this.getOne(id);
        videoRepository.delete(video);
        return new ResponseStatusDto("Delete Video", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public ResponseStatusDto deleteByCourse(Long id) {
        List<Video> videos = videoRepository.findAllByCourseId(id);
        if (videos.isEmpty()) {
            throw new NotFoundException(Constants.ERROR_CODE.VIDEO_NOT_FOUND, id);
        }
        videoRepository.deleteAll(videos);
        return new ResponseStatusDto("Delete Video By Course", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString()) ;
    }


    private void validateIfVideoWithUrl(String url,String slug){
        if (isVideoWithUrlAvailable(url))
            throw new DuplicatedException(Constants.ERROR_CODE.URL_ALREADY_EXISTED, url);
        if (isVideoWithSlugAvailable(slug))
            throw new DuplicatedException(Constants.ERROR_CODE.SLUG_ALREADY_EXISTED, slug);
    }
    private boolean isVideoWithUrlAvailable(String url) {
        return videoRepository.findByLinkUrlAndIsPublishedTrue(url).isPresent();
    }
    private boolean isVideoWithSlugAvailable(String slug) {
        return videoRepository.findBySlugAndIsPublishedTrue(slug).isPresent();
    }

}
