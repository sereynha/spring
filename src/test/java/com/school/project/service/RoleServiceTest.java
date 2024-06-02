package com.school.project.service;

import com.school.project.dto.RoleDto;
import com.school.project.exception.DuplicatedException;
import com.school.project.mapper.RoleMapper;
import com.school.project.model.Permission;
import com.school.project.model.Role;
import com.school.project.repository.PermissionRepository;
import com.school.project.repository.RoleRepository;
import com.school.project.service.impl.RoleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private RoleMapper roleMapper;
    private RoleService roleService;


    @BeforeEach
    public void setUp() {
        roleService = new RoleServiceImpl(roleRepository,permissionRepository,roleMapper);
    }


    @Test
    public void testCreate() {
        // Given
        Permission permission = new Permission();
        permission.setId(1L);
        Set<Permission> setPermission = new HashSet<>();
        setPermission.add(permission);
        Long id = 1L;
        String name = "USER";
        String description = "USER Role";
        RoleDto roleDto = new RoleDto();
        roleDto.setName(name);
        roleDto.setPermissionsId(Set.of(1L));
        Role role = new Role(id,name,description,setPermission);

        // When
        when(roleMapper.toEntity(roleDto)).thenReturn(role);
        when(permissionRepository.findAllById(roleDto.getPermissionsId())).thenReturn(List.of(permission));
        when(roleRepository.save(role)).thenReturn(role);
        Role result = roleService.create(roleDto);
        when(roleRepository.findExistedName(anyString(), eq(null))).thenReturn(role);

        // Then
        verify(roleMapper, times(1)).toEntity(roleDto);
        verify(permissionRepository, times(1)).findAllById(roleDto.getPermissionsId());
        verify(roleRepository, times(1)).save(role);
        assertThat(result).isEqualTo(role);
        assertEquals(role.getId(),result.getId());
        assertEquals(role.getName(),result.getName());
        assertEquals(role.getDescription(),result.getDescription());
        assertEquals(setPermission,result.getPermissions());
        assertThrows(DuplicatedException.class, () -> roleService.create(roleDto));
    }

    @Test
    public void testGetAll() {
        //given
        Page<Role>  rolesPage = new PageImpl<>(Collections.emptyList());

        //when
        Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
        when(roleRepository.findAll(pageable)).thenReturn(rolesPage);
        Page<Role> result = roleService.getAll(1,10,"name:asc");

        // then
        verify(roleRepository, times(1)).findAll(eq(pageable));
        assertThat(result).isEqualTo(rolesPage);
    }

    @Test
    public void testUpdate() {
        //give
        Permission permission = new Permission();
        permission.setId(1L);
        Permission permissionTwo = new Permission();
        permissionTwo.setId(2L);
        Set<Permission> setPermission = new HashSet<>();
        setPermission.add(permission);
        RoleDto roleDto = new RoleDto();
        roleDto.setName("USER");
        roleDto.setPermissionsId(Set.of(1L, 2L));
        Role role = new Role();
        role.setId(1L);
        role.setName("USER");
        role.setPermissions(setPermission);

        //when
        when(roleMapper.toEntity(roleDto)).thenReturn(role);
        when(permissionRepository.findAllById(roleDto.getPermissionsId())).thenReturn(List.of(permission, permissionTwo));
        when(roleRepository.save(role)).thenReturn(role);
        roleService.update(1L, roleDto);
        when(roleRepository.findExistedName(roleDto.getName(), 1L)).thenReturn(role);

        //then
        verify(roleMapper, times(1)).toEntity(roleDto);
        verify(permissionRepository, times(1)).findAllById(roleDto.getPermissionsId());
        verify(roleRepository,times(1)).save(role);
        assertEquals(roleDto.getDescription(),role.getDescription());
        assertThrows(DuplicatedException.class, () -> roleService.update(1L,roleDto));
    }

}
