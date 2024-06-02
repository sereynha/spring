package com.school.project.service.impl;

import com.school.project.dto.AvatarDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.AvatarMapper;
import com.school.project.model.Avatar;
import com.school.project.repository.AvatarRepository;
import com.school.project.service.AvatarService;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvatarServiceImpl implements AvatarService {

    private final AvatarRepository avatarRepository;

    private final AvatarMapper avatarMapper;
    @Override
    public ResponseStatusDto create(AvatarDto dto) {
        Avatar found = avatarMapper.toEntity(dto);
        avatarRepository.save(found);
        return new ResponseStatusDto("Create Avatar", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.CREATED.toString());
    }
    @Override
    public AvatarDto getOne(Long id) {
        Avatar found = avatarRepository.findById(id).orElseThrow(() -> new NotFoundException("Avatar",id));
        return avatarMapper.toDto(found);
    }

    @Override
    public ResponseStatusDto delete(Long id) {
        Avatar found = avatarRepository.findById(id).orElseThrow(() -> new NotFoundException("Avatar",id));
        avatarRepository.deleteById(found.getId());
        return new ResponseStatusDto("Delete Avatar", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }

    @Override
    public ResponseStatusDto update(Long id, AvatarDto dto) {
        Avatar found = avatarRepository.findById(id).orElseThrow(() -> new NotFoundException("Avatar",id));
        found.setUrl(dto.getUrl());
        avatarRepository.save(found);
        return new ResponseStatusDto("Update Avatar", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }
}
