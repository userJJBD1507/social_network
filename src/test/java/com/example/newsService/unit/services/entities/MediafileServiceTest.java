package com.example.newsService.unit.services.entities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.mappers.MediafileMapper;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class MediafileServiceTest {

    @Mock
    private JpaMediafileRepository jpaMediafileRepository;

    @Mock
    private MediafileMapper mediafileMapper;

    @Mock
    private S3StorageServiceImpl s3StorageService;

    @InjectMocks
    private MediafileService mediafileService;

    private MediafileDTO mediafileDTO;
    private MediafileEntity mediafileEntity;
    private UUID mediafileId;

    @BeforeEach
    void setUp() {
        
        mediafileId = UUID.randomUUID();
        mediafileDTO = new MediafileDTO();
        mediafileDTO.setType(MediaFileType.PHOTO);
        mediafileDTO.setUrl("test-url");

        mediafileEntity = new MediafileEntity();
        mediafileEntity.setId(mediafileId);
        mediafileEntity.setType(MediaFileType.PHOTO);
        mediafileEntity.setUrl("test-url");
    }

    @Test
    void testAdd_ShouldSaveMediafile() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);

        when(mediafileMapper.toEntity(mediafileDTO)).thenReturn(mediafileEntity);

        mediafileService.add(mediafileDTO);

        verify(jpaMediafileRepository, times(1)).addMediafile(mediafileEntity);
    }

    @Test
    void testDelete_ShouldThrowExceptionWhenNotFound() {
        when(jpaMediafileRepository.getMediafile(mediafileId)).thenThrow(new EntityNotFoundException("Mediafile not found"));

        assertThrows(EntityNotFoundException.class, () -> mediafileService.delete(mediafileId));
    }

    @Test
    void testGet_ShouldReturnMediafileDTO() {
        when(jpaMediafileRepository.getMediafile(mediafileId)).thenReturn(mediafileEntity);
        when(mediafileMapper.toDTO(mediafileEntity)).thenReturn(mediafileDTO);

        Optional<MediafileDTO> result = mediafileService.get(mediafileId);

        assertTrue(result.isPresent());
        assertEquals(MediaFileType.PHOTO, result.get().getType());
    }

    @Test
    void testGet_ShouldReturnEmptyWhenNotFound() {
        when(jpaMediafileRepository.getMediafile(mediafileId)).thenThrow(new EntityNotFoundException("Mediafile not found"));

        Optional<MediafileDTO> result = mediafileService.get(mediafileId);

        assertFalse(result.isPresent());
    }

    @Test
    void testUpdate_ShouldUpdateMediafile() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);

        when(jpaMediafileRepository.getMediafile(mediafileId)).thenReturn(mediafileEntity);
        when(mediafileMapper.toEntity(mediafileDTO)).thenReturn(mediafileEntity);

        mediafileService.update(mediafileId, mediafileDTO);

        verify(jpaMediafileRepository, times(1)).updateMediafile(mediafileEntity);
    }

    @Test
    void testUpdate_ShouldThrowExceptionWhenNotFound() {
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("testuser");
        SecurityContextHolder.getContext().setAuthentication(mock(Authentication.class));
        when(SecurityContextHolder.getContext().getAuthentication().getPrincipal()).thenReturn(jwt);


        assertThrows(NullPointerException.class, () -> mediafileService.update(mediafileId, mediafileDTO));
    }
}