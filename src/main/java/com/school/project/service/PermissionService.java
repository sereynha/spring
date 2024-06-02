package com.school.project.service;

import com.school.project.dto.PermissionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Permission;
import org.springframework.data.domain.Page;

public interface PermissionService {
    ResponseStatusDto create(PermissionDto dto);
    Page<Permission> getAll(int page, int limit, String sort);
    ResponseStatusDto update(Long id,PermissionDto dto);
}
