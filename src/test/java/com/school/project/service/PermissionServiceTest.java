package com.school.project.service;

import com.school.project.dto.PermissionDto;
import com.school.project.exception.NotFoundException;
import com.school.project.mapper.PermissionMapper;
import com.school.project.model.Permission;
import com.school.project.repository.PermissionRepository;
import com.school.project.service.impl.PermissionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PermissionMapper permissionMapper;

    private PermissionService permissionService;

    @BeforeEach
    public void setUp() {
        permissionService = new PermissionServiceImpl(permissionRepository,permissionMapper);
    }

    @Test
    public void testCreate(){
        //given
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("user:read");
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("user:read");

        //when
        when(permissionMapper.toPermission(permissionDto)).thenReturn(permission);
        when(permissionRepository.save(permission)).thenReturn(permission);
        permissionService.create(permissionDto);

        //then
        verify(permissionMapper,times(1)).toPermission(permissionDto);
        verify(permissionRepository,times(1)).save(permission);
    }

    @Test
    public void testGetAll(){
        //given
        Page<Permission> permissions = new PageImpl<>(Collections.emptyList());

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(permissionRepository.findAll(pageable)).thenReturn(permissions);
        Page<Permission> result = permissionService.getAll(1,10,"name:asc");

        // then
        verify(permissionRepository, times(1)).findAll(eq(pageable));
        assertThat(result).isEqualTo(permissions);
    }

    @Test
    public void testUpdate() {
        //given
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("user:read");
        Permission permission = new Permission();
        permission.setId(1L);
        permission.setName("user:read");

        //when
        when(permissionRepository.findById(1L)).thenReturn(Optional.of(permission));
        permissionService.update(1L, permissionDto);

        //then
        verify(permissionRepository, times(1)).findById(1L);
        verify(permissionRepository, times(1)).save(permission);
        assertThat(permission.getName()).isEqualTo(permissionDto.getName());

    }

    @Test
    public void testUpdateGetByIdThrow() {
        //given
        PermissionDto permissionDto = new PermissionDto();
        permissionDto.setName("user:read");

        //when
        when(permissionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> permissionService.update(1L,permissionDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Permission With id = 1 not found");
    }

}
