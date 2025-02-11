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
import com.example.newsService.core.reaction.entities.ReactionEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.repositories.entity.EntityReactionRepository;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.repositories.JpaReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class JpaReactionRepositoryTest {

    @Mock
    private EntityReactionRepository entityReactionRepository;

    @InjectMocks
    private JpaReactionRepository jpaReactionRepository;

    @Captor
    ArgumentCaptor<ReactionEntity> reactionCaptor;

    @Test
    void addReaction_ShouldSaveReaction() {
        // Arrange
        ReactionEntity reactionEntity = mock(ReactionEntity.class);

        // Act
        jpaReactionRepository.addReaction(reactionEntity);

        // Assert
        verify(entityReactionRepository).save(reactionCaptor.capture());
        assertEquals(reactionEntity, reactionCaptor.getValue());
    }

    @Test
    void deleteReaction_ShouldDeleteReaction_WhenExists() {
        // Arrange
        ReactionEntity reactionEntity = mock(ReactionEntity.class);
        when(entityReactionRepository.existsById(reactionEntity.getId())).thenReturn(true);
        doNothing().when(entityReactionRepository).delete(reactionEntity);

        // Act
        jpaReactionRepository.deleteReaction(reactionEntity);

        // Assert
        verify(entityReactionRepository).delete(reactionCaptor.capture());
        assertEquals(reactionEntity, reactionCaptor.getValue());
    }

    @Test
    void deleteReaction_ShouldThrowEntityNotFoundException_WhenReactionNotFound() {
        // Arrange
        ReactionEntity reactionEntity = mock(ReactionEntity.class);
        when(entityReactionRepository.existsById(reactionEntity.getId())).thenReturn(false);

        // Act & Assert
        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            jpaReactionRepository.deleteReaction(reactionEntity);
        });

        assertEquals("Reaction not found", thrown.getMessage());
    }
}
