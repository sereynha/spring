package com.school.project.mapper;

import com.school.project.dto.AvatarDto;
import com.school.project.model.Avatar;
import org.mapstruct.*;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface AvatarMapper {
    Avatar toEntity(AvatarDto avatarDto);

    AvatarDto toDto(Avatar avatar);

}