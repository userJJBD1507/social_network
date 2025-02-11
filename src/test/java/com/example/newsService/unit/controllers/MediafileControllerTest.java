package com.example.newsService.unit.controllers;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;
import com.example.newsService.presentation.api.MediafileController;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class MediafileControllerTest {

    @Mock
    private MediafileService mediafileService;

    @Mock
    private S3StorageServiceImpl s3StorageService;

    @Mock
    private JpaMediafileRepository jpaMediafileRepository;

    @Mock
    private EntityMediafileRepository entityMediafileRepository;

    @InjectMocks
    private MediafileController mediafileController;

    private static final String USERNAME = "testUser";
    @Captor
    ArgumentCaptor<MediafileDTO> mediafileDTOCaptor;
    @BeforeEach
    void setUpSecurityContext() {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn(USERNAME);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void addMediafile_ShouldReturnCreatedStatus_WhenFileIsUploaded() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
        String postId = UUID.randomUUID().toString();
        String generatedUrl = "https://mock-url.com";

        when(s3StorageService.upload(file)).thenReturn(generatedUrl);

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(MediaFileType.PHOTO)
                .url(generatedUrl)
                .postId(UUID.fromString(postId))
                .build();

        // Act
        ResponseEntity<Void> response = mediafileController.addMediafile(file, postId);

        // Assert
        
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
    

    @Test
    void getMediafile_ShouldReturnOk_WhenFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        MediafileDTO mediafileDTO = new MediafileDTO();
        when(mediafileService.get(id)).thenReturn(Optional.of(mediafileDTO));

        // Act
        ResponseEntity<MediafileDTO> response = mediafileController.getMediafile(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mediafileDTO, response.getBody());
    }

    @Test
    void getMediafile_ShouldReturnNotFound_WhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(mediafileService.get(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<MediafileDTO> response = mediafileController.getMediafile(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void updateMediafile_ShouldReturnOk_WhenUpdatedSuccessfully() throws Exception {
        // Arrange
        UUID mediafileId = UUID.randomUUID();
        MediafileEntity mediafileEntity = mock(MediafileEntity.class);
        when(mediafileEntity.getCreatedBy()).thenReturn("user");
    
        when(entityMediafileRepository.findById(mediafileId)).thenReturn(Optional.of(mediafileEntity));
    
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("user");
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    

        MultipartFile file = mock(MultipartFile.class);
        when(file.getContentType()).thenReturn("image/jpeg");
    
        // Act
        ResponseEntity<Void> response = mediafileController.updateMediafile(file, UUID.randomUUID().toString(), mediafileId);
    
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    
    

    @Test
    void updateMediafile_ShouldReturnNotFound_WhenMediafileDoesNotExist() throws Exception {
        // Arrange
        MultipartFile file = mock(MultipartFile.class);
        String postId = UUID.randomUUID().toString();
        UUID id = UUID.randomUUID();
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = mediafileController.updateMediafile(file, postId, id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteMediafile_ShouldReturnNoContent_WhenDeletedSuccessfully() {
        // Arrange
        UUID id = UUID.randomUUID();
        MediafileEntity mediafileEntity = mock(MediafileEntity.class);
        when(mediafileEntity.getUrl()).thenReturn("existing-url");
        when(mediafileEntity.getCreatedBy()).thenReturn("user");
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.of(mediafileEntity));
        
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("user");
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    
        // Act
        ResponseEntity<Void> response = mediafileController.deleteMediafile(id);
    
        // Assert
        verify(mediafileService).delete(eq(id));
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteMediafile_ShouldReturnNotFound_WhenMediafileDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = mediafileController.deleteMediafile(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteMediafile_ShouldReturnForbidden_WhenNotAuthorized() {
        // Arrange
        UUID id = UUID.randomUUID();
        MediafileEntity mediafileEntity = mock(MediafileEntity.class);
        when(mediafileEntity.getUrl()).thenReturn("existing-url");
        when(mediafileEntity.getCreatedBy()).thenReturn("otherUser");
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.of(mediafileEntity));
        
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn("differentUser");
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    
        // Act
        ResponseEntity<Void> response = mediafileController.deleteMediafile(id);
    
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
