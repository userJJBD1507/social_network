package com.example.newsService.unit.repositories;

import com.example.newsService.core.like.entities.post.PostLikes;
import com.example.newsService.core.repositories.entity.EntityPostLikesRepository;
import com.example.newsService.infra.repositories.JpaPostLikesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class JpaPostLikesRepositoryTest {

    @Mock
    private EntityPostLikesRepository repository;

    @InjectMocks
    private JpaPostLikesRepository postLikesRepository;

    private PostLikes postLikes;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        postLikes = new PostLikes(
                UUID.randomUUID(),
                "user123"
        );
    }

    @Test
    void testAddPostLikes() {
        when(repository.save(postLikes)).thenReturn(postLikes);

        postLikesRepository.addPostLike(postLikes);

        verify(repository, times(1)).save(postLikes);
    }

    @Test
    void testGetPostLikes() {
        when(repository.findById(postLikes.getId()))
                .thenReturn(Optional.of(postLikes));

        postLikesRepository.getPostLike(postLikes.getId());

        verify(repository, times(1)).findById(postLikes.getId());
    }

    @Test
    void testDeletePostLikes() {
        when(repository.existsById(postLikes.getId())).thenReturn(true);

        doNothing().when(repository).deleteById(postLikes.getId());

        postLikesRepository.deletePostLike(postLikes.getId());

        verify(repository, times(1)).existsById(postLikes.getId());

        verify(repository, times(1)).deleteById(postLikes.getId());
    }
}