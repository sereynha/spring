package com.school.project.service;

import com.school.project.dto.AvatarDto;
import com.school.project.mapper.AvatarMapper;
import com.school.project.model.Avatar;
import com.school.project.model.User;
import com.school.project.repository.AvatarRepository;
import com.school.project.service.impl.AvatarServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AvatarServiceTest {
    @Mock
    private AvatarRepository avatarRepository;
    @Mock
    private AvatarMapper avatarMapper;

    private AvatarService avatarService;
    private Avatar avatar;
    private AvatarDto avatarDto;
    private  User user;

    @BeforeEach
    public void setUp() {
        avatarService = new AvatarServiceImpl(avatarRepository,avatarMapper);

        user = new User();
        user.setId(1L);
        user.setFirstName("user");
        avatar = new Avatar();
        avatar.setId(1L);
        avatar.setUrl("https://test.com/avatar.jpg");
        avatar.setUser(user);
        avatarDto = new AvatarDto();
        avatarDto.setUrl("https://test.com/avatar.jpg");
    }

    @Test
    public void tearCreate(){
        //when
        when(avatarMapper.toEntity(any(AvatarDto.class))).thenReturn(avatar);
        avatarService.create(avatarDto);
        //then
        verify(avatarMapper, times(1)).toEntity(avatarDto);
        verify(avatarRepository, times(1)).save(avatar);
    }

    @Test
    public void testGetOne() {
        // given
        Long id = 1L;

        // when
        when(avatarRepository.findById(id)).thenReturn(Optional.of(avatar));
        when(avatarMapper.toDto(avatar)).thenReturn(avatarDto);
        AvatarDto result = avatarService.getOne(id);

        // then
        verify(avatarRepository, times(1)).findById(id);
        verify(avatarMapper, times(1)).toDto(avatar);
        assertEquals(avatarDto, result);
        assertEquals(avatar.getId(),id);
        assertEquals(avatar.getUrl(),avatarDto.getUrl());
        assertEquals(avatar.getUser(),user);
    }

    @Test
    public void testDelete(){
        // given
        Long id = 1L;

        // when
        when(avatarRepository.findById(id)).thenReturn(Optional.of(avatar));
        doNothing().when(avatarRepository).deleteById(id);
        avatarService.delete(id);

        // Then
        verify(avatarRepository, times(1)).findById(id);
        verify(avatarRepository, times(1)).deleteById(id);
    }

    @Test
    public void testUpdate(){
        // given
        Long id = 1L;

        // when
        when(avatarRepository.findById(id)).thenReturn(Optional.of(avatar));
        avatarService.update(id, avatarDto);

        //then
        verify(avatarRepository, times(1)).findById(id);
        verify(avatarRepository, times(1)).save(avatar);
        assertThat(avatar.getUrl()).isEqualTo(avatarDto.getUrl());

    }
}
