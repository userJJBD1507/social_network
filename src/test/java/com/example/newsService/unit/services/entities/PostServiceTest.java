package com.example.newsService.unit.services.entities;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.mappers.PostMapper;
import com.example.newsService.app.services.PostService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.infra.repositories.JpaPostRepository;
import com.example.newsService.infra.services.KeycloakService;

import jakarta.persistence.EntityNotFoundException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;


import java.util.Optional;
import java.util.UUID;


public class PostServiceTest {

    @Mock
    private JpaPostRepository jpaPostRepository;

    @Mock
    private PostMapper postMapper;

    @Mock
    private KeycloakService keycloakService;

    @Mock
    private Jwt jwt;

    @InjectMocks
    private PostService postService;

    private PostDTO postDTO;
    private PostEntity postEntity;
    private UUID postId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        postId = UUID.randomUUID();
        postDTO = new PostDTO("Title", "Content", false);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(jwt.getClaimAsString("preferred_username")).thenReturn("testUser");
        when(keycloakService.getUserIdByUsername("testUser")).thenReturn(postId.toString());
    }

    @Test
    void addPost_ShouldThrowIllegalArgumentException_WhenPostDTOIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.add(null);
        });

        assertEquals("PostDTO cannot be null", exception.getMessage());
    }

    @Test
    void deletePost_ShouldNotThrowException_WhenPostExists() {
        doNothing().when(jpaPostRepository).deletePost(postId);

        assertDoesNotThrow(() -> postService.delete(postId));

        verify(jpaPostRepository, times(1)).deletePost(postId);
    }

    @Test
    void deletePost_ShouldThrowEntityNotFoundException_WhenPostNotFound() {
        doThrow(new EntityNotFoundException("Post not found")).when(jpaPostRepository).deletePost(postId);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            postService.delete(postId);
        });

        assertEquals("Post not found", exception.getMessage());
    }


    @Test
    void getPost_ShouldReturnEmptyOptional_WhenPostNotFound() {
        when(jpaPostRepository.getPost(postId)).thenThrow(new EntityNotFoundException("Post not found"));

        Optional<PostDTO> result = postService.get(postId);

        assertFalse(result.isPresent());
    }


    @Test
    void updatePost_ShouldThrowIllegalArgumentException_WhenPostDTOIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            postService.update(postId, null);
        });

        assertEquals("PostDTO cannot be null", exception.getMessage());
    }
}
