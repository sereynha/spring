package com.school.project.mapper;

import com.school.project.dto.PermissionDto;
import com.school.project.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    PermissionMapper Instant = Mappers.getMapper(PermissionMapper.class);
    Permission toPermission(PermissionDto dto);
}
