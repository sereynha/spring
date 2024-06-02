package com.school.project.controller;

import com.school.project.dto.AvatarDto;
import com.school.project.dto.ResponseStatusDto;
import com.school.project.service.AvatarService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AvatarControllerTest {

    private AvatarService avatarService;
    private AvatarController avatarController;
    private AvatarDto avatarDto;

    @BeforeEach
    void setUp() {
        avatarService = mock(AvatarService.class);
        avatarController = new AvatarController(avatarService);

        avatarDto = new AvatarDto();
        avatarDto.setUrl("https://test.com/avatar.jpg");
    }

    @Test
    void testCreate() {
        // given
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Create Avatar","SUCCESS","201 Created");

        // when
        when(avatarService.create(avatarDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = avatarController.create(avatarDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Create Avatar", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals("201 Created", response.getBody().getStatusCode());
        verify(avatarService, times(1)).create(avatarDto);
    }

    @Test
    void testGetAvatar() {
        // give
        Long id = 1L;

        // when
        when(avatarService.getOne(id)).thenReturn(avatarDto);
        ResponseEntity<?> response = avatarController.getAvatar(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(avatarDto, response.getBody());
        verify(avatarService, times(1)).getOne(id);
    }

    @Test
    void testDelete() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Delete Avatar","SUCCESS","200 OK");

        // when
        when(avatarService.delete(id)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = avatarController.delete(id);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Delete Avatar", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(avatarService, times(1)).delete(id);
    }

    @Test
    void testUpdate() {
        // give
        Long id = 1L;
        ResponseStatusDto responseStatusDto = new ResponseStatusDto("Update Avatar","SUCCESS","200 OK");


        // when
        when(avatarService.update(id, avatarDto)).thenReturn(responseStatusDto);
        ResponseEntity<ResponseStatusDto> response = avatarController.update(id, avatarDto);

        // when
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Update Avatar", Objects.requireNonNull(response.getBody()).getTitle());
        assertEquals("SUCCESS", response.getBody().getMessage());
        assertEquals("200 OK", response.getBody().getStatusCode());
        verify(avatarService, times(1)).update(id, avatarDto);
    }
}
