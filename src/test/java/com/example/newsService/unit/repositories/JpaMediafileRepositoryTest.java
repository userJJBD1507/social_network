package com.example.newsService.unit.repositories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;

import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.infra.repositories.JpaMediafileRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JpaMediafileRepositoryTest {

    @Mock
    private EntityMediafileRepository entityMediafileRepository;

    @InjectMocks
    private JpaMediafileRepository jpaMediafileRepository;

    @Captor
    ArgumentCaptor<MediafileEntity> mediafileCaptor;

    @Test
    void addMediafile_ShouldSaveMediafile() {
        // Arrange
        MediafileEntity mediafileEntity = mock(MediafileEntity.class);

        // Act
        jpaMediafileRepository.addMediafile(mediafileEntity);

        // Assert
        verify(entityMediafileRepository).save(mediafileCaptor.capture());
        assertEquals(mediafileEntity, mediafileCaptor.getValue());
    }

    @Test
    void getMediafile_ShouldReturnMediafile_WhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        MediafileEntity mediafileEntity = new MediafileEntity();
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.of(mediafileEntity));

        // Act
        MediafileEntity result = jpaMediafileRepository.getMediafile(id);

        // Assert
        assertNotNull(result);
        assertEquals(mediafileEntity, result);
    }

    @Test
    void getMediafile_ShouldThrowEntityNotFoundException_WhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(entityMediafileRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaMediafileRepository.getMediafile(id);
        });

        assertEquals("Mediafile not found", thrown.getMessage());
    }

    @Test
    void updateMediafile_ShouldSaveUpdatedMediafile() {
        // Arrange
        MediafileEntity mediafileEntity = mock(MediafileEntity.class);

        // Act
        jpaMediafileRepository.updateMediafile(mediafileEntity);

        // Assert
        verify(entityMediafileRepository).save(mediafileCaptor.capture());
        assertEquals(mediafileEntity, mediafileCaptor.getValue());
    }

    @Test
    void deleteMediafile_ShouldDeleteMediafile_WhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(entityMediafileRepository).deleteById(id);

        // Act
        jpaMediafileRepository.deleteMediafile(id);

        // Assert
        verify(entityMediafileRepository).deleteById(id);
    }

    @Test
    void deleteMediafile_ShouldThrowEntityNotFoundException_WhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new EmptyResultDataAccessException(1)).when(entityMediafileRepository).deleteById(id);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaMediafileRepository.deleteMediafile(id);
        });

        assertEquals("Mediafile not found", thrown.getMessage());
    }

    @Test
    void getAllMediafiles_ShouldReturnListOfMediafiles() {
        // Arrange
        List<MediafileEntity> mediafileEntities = new ArrayList<>();
        mediafileEntities.add(new MediafileEntity());
        mediafileEntities.add(new MediafileEntity());
        when(entityMediafileRepository.findAll()).thenReturn(mediafileEntities);

        // Act
        List<MediafileEntity> result = jpaMediafileRepository.getAllMediafiles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
