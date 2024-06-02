package com.school.project.service;

import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.RoleDto;
import com.school.project.model.Role;
import org.springframework.data.domain.Page;

public interface RoleService {
    Role create(RoleDto dto);
    Page<Role> getAll(int page, int limit, String sort);
    ResponseStatusDto update(Long id, RoleDto dto);
}
