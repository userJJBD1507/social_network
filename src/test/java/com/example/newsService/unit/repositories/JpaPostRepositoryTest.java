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
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.repositories.JpaPostRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JpaPostRepositoryTest {

    @Mock
    private EntityPostRepository entityPostRepository;

    @InjectMocks
    private JpaPostRepository jpaPostRepository;

    @Captor
    ArgumentCaptor<PostEntity> postCaptor;

    @Test
    void addPost_ShouldSavePost() {
        // Arrange
        PostEntity postEntity = mock(PostEntity.class);

        // Act
        jpaPostRepository.addPost(postEntity);

        // Assert
        verify(entityPostRepository).save(postCaptor.capture());
        assertEquals(postEntity, postCaptor.getValue());
    }

    @Test
    void getPost_ShouldReturnPost_WhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        PostEntity postEntity = new PostEntity();
        when(entityPostRepository.findById(id)).thenReturn(Optional.of(postEntity));

        // Act
        PostEntity result = jpaPostRepository.getPost(id);

        // Assert
        assertNotNull(result);
        assertEquals(postEntity, result);
    }

    @Test
    void getPost_ShouldThrowEntityNotFoundException_WhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(entityPostRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaPostRepository.getPost(id);
        });

        assertEquals("Post not found", thrown.getMessage());
    }

    @Test
    void updatePost_ShouldSaveUpdatedPost() {
        // Arrange
        PostEntity postEntity = mock(PostEntity.class);

        // Act
        jpaPostRepository.updatePost(postEntity);

        // Assert
        verify(entityPostRepository).save(postCaptor.capture());
        assertEquals(postEntity, postCaptor.getValue());
    }

    @Test
    void deletePost_ShouldDeletePost_WhenExists() {
        // Arrange
        UUID id = UUID.randomUUID();
        doNothing().when(entityPostRepository).deleteById(id);

        // Act
        jpaPostRepository.deletePost(id);

        // Assert
        verify(entityPostRepository).deleteById(id);
    }

    @Test
    void deletePost_ShouldThrowEntityNotFoundException_WhenNotFound() {
        // Arrange
        UUID id = UUID.randomUUID();
        doThrow(new EmptyResultDataAccessException(1)).when(entityPostRepository).deleteById(id);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaPostRepository.deletePost(id);
        });

        assertEquals("Post not found", thrown.getMessage());
    }

    @Test
    void getAllPosts_ShouldReturnListOfPosts() {
        // Arrange
        List<PostEntity> postEntities = new ArrayList<>();
        postEntities.add(new PostEntity());
        postEntities.add(new PostEntity());
        when(entityPostRepository.findAll()).thenReturn(postEntities);

        // Act
        List<PostEntity> result = jpaPostRepository.getAllPosts();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
    }
}
