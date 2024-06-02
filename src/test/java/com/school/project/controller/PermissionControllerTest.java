package com.school.project.controller;

import com.school.project.dto.PageDto;
import com.school.project.dto.PermissionDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.model.Permission;
import com.school.project.service.PermissionService;
import com.school.project.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PermissionControllerTest {

    private PermissionService permissionService;
    private PermissionController permissionController;
    private PermissionDto permissionDto;

    @BeforeEach
    void setUp() {
        permissionService = mock(PermissionService.class);
        permissionController = new PermissionController(permissionService);

        permissionDto = new PermissionDto();
        permissionDto.setName("user:read");
    }

    @Test
    void testCreate() {
        // give
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Create Permission","SUCCESS","201 Created");

        // when
        when(permissionService.create(permissionDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = permissionController.create(permissionDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Create Permission", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("201 Created", response.getBody().getStatusCode());
        verify(permissionService, times(1)).create(permissionDto);
    }

    @Test
    void testGetAll() {
        // give
        PageImpl<Permission> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(1, 5), 0);

        // when
        when(permissionService.getAll(anyInt(), anyInt(), anyString())).thenReturn(page);
        ResponseEntity<PageDto> response = permissionController.getAll(1, 5, "name:asc");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, Objects.requireNonNull(response.getBody()).getList().size());
        verify(permissionService, times(1)).getAll(1, 5, "name:asc");
    }

    @Test
    void testUpdate() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Permission","SUCCESS","200 OK");

        // when
        when(permissionService.update(id, permissionDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = permissionController.update(id, permissionDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Permission", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals(Constants.MESSAGE.SUCCESS_MESSAGE, response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(permissionService, times(1)).update(id, permissionDto);
    }
}
