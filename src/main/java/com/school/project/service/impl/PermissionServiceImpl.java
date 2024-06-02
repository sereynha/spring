package com.school.project.service.impl;

import com.school.project.dto.PermissionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PermissionMapper;
import com.school.project.model.Permission;
import com.school.project.repository.PermissionRepository;
import com.school.project.service.PageService;
import com.school.project.service.PermissionService;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {
    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Override
    public ResponseStatusDto create(PermissionDto dto) {
        var permission = permissionMapper.toPermission(dto);
        permissionRepository.save(permission);
        return new ResponseStatusDto("Create Permission", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.CREATED.toString());
    }

    @Override
    public Page<Permission> getAll(int page, int limit, String sort) {
        Pageable pageable = PageService.getPageable(page,limit,sort);
        return  permissionRepository.findAll(pageable);
    }

    @Override
    public ResponseStatusDto update(Long id, PermissionDto dto) {
        var found = permissionRepository.findById(id).orElseThrow(() -> new NotFoundException("Permission",id));
        found.setName(dto.getName());
        permissionRepository.save(found);
        return new ResponseStatusDto("Update Permission", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }
}
