package com.school.project.mapper;

import com.school.project.dto.RoleDto;
import com.school.project.model.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    Role toEntity(RoleDto roleDto);

    RoleDto toDto(Role role);

}