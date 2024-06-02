package com.school.project.service.impl;

import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.RoleDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.mapper.RoleMapper;
import com.school.project.model.Permission;
import com.school.project.model.Role;
import com.school.project.repository.PermissionRepository;
import com.school.project.repository.RoleRepository;
import com.school.project.service.PageService;
import com.school.project.service.RoleService;
import com.school.project.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final PermissionRepository permissionRepository;
    private final RoleMapper roleMapper;
    @Override
    public Role create(RoleDto dto) {
        validateDuplicateName(dto.getName(),null);
        var role = this.roleMapper.toEntity(dto);
        List<Permission> foundList = this.permissionRepository.findAllById(dto.getPermissionsId());
        Set<Permission> found = Set.copyOf(foundList);
        role.setPermissions(found);
        role = this.repository.save(role);
        return role;
    }
    @Override
    public Page<Role> getAll(int page, int limit, String sort) {
        Pageable pageable = PageService.getPageable(page,limit,sort);
        return repository.findAll(pageable);
    }
    @Override
    public ResponseStatusDto update(Long id, RoleDto dto) {
        validateDuplicateName(dto.getName(),id);
        var role = this.roleMapper.toEntity(dto);
        List<Permission> foundList = this.permissionRepository.findAllById(dto.getPermissionsId());
        Set<Permission> found = Set.copyOf(foundList);
        role.setPermissions(found);
        this.repository.save(role);
        return new ResponseStatusDto("Update Role", Constants.MESSAGE.SUCCESS_MESSAGE, HttpStatus.OK.toString());
    }
    private boolean checkExistedName(String name, Long id) {
        return repository.findExistedName(name,id) != null;
    }
    private void validateDuplicateName(String name, Long id) {
        if (checkExistedName(name, id)) {
            throw new DuplicatedException(Constants.ERROR_CODE.NAME_ALREADY_EXITED,name);
        }
    }
}
