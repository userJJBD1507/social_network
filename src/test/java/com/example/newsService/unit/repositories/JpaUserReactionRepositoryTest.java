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
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.repositories.entity.EntityUserReactionRepository;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.repositories.JpaUserReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JpaUserReactionRepositoryTest {

    @Mock
    private EntityUserReactionRepository entityUserReactionRepository;

    @InjectMocks
    private JpaUserReactionRepository jpaUserReactionRepository;

    @Captor
    ArgumentCaptor<UserReactionEntity> userReactionCaptor;

    @Test
    void addUserReaction_ShouldSaveUserReaction() {
        // Arrange
        UserReactionEntity userReactionEntity = mock(UserReactionEntity.class);

        // Act
        jpaUserReactionRepository.addUserReaction(userReactionEntity);

        // Assert
        verify(entityUserReactionRepository).save(userReactionCaptor.capture());
        assertEquals(userReactionEntity, userReactionCaptor.getValue());
    }

    @Test
    void deleteUserReaction_ShouldDeleteUserReaction_WhenExists() {
        // Arrange
        UserReactionEntity userReactionEntity = mock(UserReactionEntity.class);
        when(entityUserReactionRepository.existsById(userReactionEntity.getId())).thenReturn(true);
        doNothing().when(entityUserReactionRepository).delete(userReactionEntity);

        // Act
        jpaUserReactionRepository.deleteUserReaction(userReactionEntity);

        // Assert
        verify(entityUserReactionRepository).delete(userReactionCaptor.capture());
        assertEquals(userReactionEntity, userReactionCaptor.getValue());
    }

    @Test
    void deleteUserReaction_ShouldThrowEntityNotFoundException_WhenUserReactionNotFound() {
        // Arrange
        UserReactionEntity userReactionEntity = mock(UserReactionEntity.class);
        when(entityUserReactionRepository.existsById(userReactionEntity.getId())).thenReturn(false);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaUserReactionRepository.deleteUserReaction(userReactionEntity);
        });

        assertEquals("User reaction not found", thrown.getMessage());
    }
}
