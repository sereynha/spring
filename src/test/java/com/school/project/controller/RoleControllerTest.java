package com.school.project.controller;

import com.school.project.dto.PageDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.dto.RoleDto;
import com.school.project.model.Role;
import com.school.project.service.RoleService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class RoleControllerTest {

    private RoleService roleService;
    private RoleController roleController;

    @BeforeEach
    void setUp() {
        roleService = mock(RoleService.class);
        roleController = new RoleController(roleService);
    }

    @Test
    void testGetAll() {
        // give
        PageImpl<Role> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 5), 0);

        // when
        when(roleService.getAll(anyInt(), anyInt(), anyString())).thenReturn(page);
        ResponseEntity<PageDto> response = roleController.getAll(1, 5, "name:asc");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getList().size());
        verify(roleService, times(1)).getAll(1, 5, "name:asc");
    }

    @Test
    void testUpdate() {
        // give
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Role","SUCCESS","200 OK");
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");
        roleDto.setPermissionsId(Set.of(1L, 2L));

        // when
        when(roleService.update(1L, roleDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = roleController.update(1L, roleDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Role", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(roleService, times(1)).update(1L, roleDto);
    }

    @Test
    void testCreate() {
        // give
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");
        roleDto.setPermissionsId(Set.of(1L, 2L));

        // when
        ResponseEntity<Role> response = roleController.create(roleDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(roleService, times(1)).create(roleDto);
    }

}

