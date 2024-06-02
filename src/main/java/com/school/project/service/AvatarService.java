package com.school.project.service;

import com.school.project.dto.AvatarDto;
import com.school.project.dto.ResponseStatusDto;

public interface AvatarService {
    ResponseStatusDto create(AvatarDto dto);
    AvatarDto getOne(Long id);
    ResponseStatusDto delete(Long id);
    ResponseStatusDto update(Long id, AvatarDto dto);
}
