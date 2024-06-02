package com.school.project.dto;

import lombok.Data;

import java.util.Set;


@Data
public class RoleDto {
    private String name;
    private String description;;
    private Set<Long> permissionsId;
}
