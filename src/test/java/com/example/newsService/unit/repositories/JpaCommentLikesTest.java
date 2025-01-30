package com.example.newsService.unit.repositories;

import com.example.newsService.core.like.entities.comment.CommentLikes;
import com.example.newsService.core.repositories.entity.EntityCommentLikesRepository;
import com.example.newsService.infra.repositories.JpaCommentLikesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class JpaCommentLikesTest {
    @Mock
    private EntityCommentLikesRepository repository;

    @InjectMocks
    private JpaCommentLikesRepository commentLikesRepository;

    private CommentLikes commentLikes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        commentLikes = new CommentLikes(
                UUID.randomUUID(),
                "user123"
        );
    }

    @Test
    void testAddCommentLikes() {
        when(repository.save(commentLikes)).thenReturn(commentLikes);

        commentLikesRepository.addCommentLike(commentLikes);

        verify(repository, times(1)).save(commentLikes);
    }

    @Test
    void testGetCommentLikes() {
        when(repository.findById(commentLikes.getId()))
                .thenReturn(Optional.of(commentLikes));

        commentLikesRepository.getCommentLike(commentLikes.getId());

        verify(repository, times(1)).findById(commentLikes.getId());
    }

    @Test
    void testDeleteCommentLikes() {
        when(repository.existsById(commentLikes.getId())).thenReturn(true);

        doNothing().when(repository).deleteById(commentLikes.getId());

        commentLikesRepository.deleteCommentLike(commentLikes.getId());

        verify(repository, times(1)).existsById(commentLikes.getId());

        verify(repository, times(1)).deleteById(commentLikes.getId());
    }
}

