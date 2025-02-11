package com.example.newsService.unit.services.entities;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.mappers.ReactionMapper;
import com.example.newsService.app.services.ReactionService;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.reaction.entities.ReactionEntity;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.core.repositories.entity.EntityReactionRepository;
import com.example.newsService.core.repositories.entity.EntityUserReactionRepository;
import com.example.newsService.infra.repositories.JpaReactionRepository;
import com.example.newsService.infra.repositories.JpaUserReactionRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import jakarta.persistence.EntityNotFoundException;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;


public class ReactionServiceTest {

    @Mock
    private JpaReactionRepository jpaReactionRepository;
    
    @Mock
    private ReactionMapper reactionMapper;
    
    @Mock
    private EntityReactionRepository entityReactionRepository;
    
    @Mock
    private EntityPostRepository entityPostRepository;
    
    @Mock
    private JpaUserReactionRepository jpaUserReactionRepository;
    
    @Mock
    private EntityUserReactionRepository userReactionRepository;
    
    @Mock
    private S3StorageServiceImpl s3StorageService;
    
    @InjectMocks
    private ReactionService reactionService;

    private ReactionDTO reactionDTO;
    private UUID reactionId;
    private UUID postId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reactionDTO = new ReactionDTO();
        reactionDTO.setDescription("Test Reaction");

        reactionId = UUID.randomUUID();
        postId = UUID.randomUUID();
    }

    @Test
    void addReactionNullTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            reactionService.add(null);
        });
    }

    @Test
    void deleteReactionNotFoundTest() {
        when(entityReactionRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> {
            reactionService.delete(reactionId);
        });
    }

    @Test
    void replyToPostTest() {
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);

        when(entityReactionRepository.findByDescription(anyString())).thenReturn(new ReactionEntity());
        when(entityPostRepository.findById(any(UUID.class))).thenReturn(Optional.of(postEntity));

        reactionService.replyToPost("Test description", postId);

        verify(jpaUserReactionRepository, times(1)).addUserReaction(any(UserReactionEntity.class));
    }

    @Test
    void replyToPostNullDescriptionTest() {
        assertThrows(IllegalArgumentException.class, () -> {
            reactionService.replyToPost("", postId);
        });
    }

    @Test
    void replyToPostPostNotFoundTest() {
        when(entityPostRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            reactionService.replyToPost("Test description", postId);
        });
    }
}