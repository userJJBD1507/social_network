package com.example.newsService.unit.repositories;

import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.infra.repositories.JpaPostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class JpaPostRepositoryTest {

    @Mock
    private EntityPostRepository repository;

    @InjectMocks
    private JpaPostRepository postRepository;

    private PostEntity post;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        post = new PostEntity(
                UUID.randomUUID(),
                "Test Title",
                "Test Content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L,
                false,
                "user123"
        );
    }

    @Test
    void testAddPost() {

        when(repository.save(post)).thenReturn(post);

        postRepository.addPost(post);

        verify(repository, times(1)).save(post);
    }

    @Test
    void testGetPost() {

        when(repository.findById(post.getId())).thenReturn(Optional.of(post));

        PostEntity foundPost = postRepository.getPost(post.getId());

        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getContent()).isEqualTo("Test Content");

        verify(repository, times(1)).findById(post.getId());
    }

    @Test
    void testUpdatePost() {
        when(repository.save(post)).thenReturn(post);

        // Обновляем данные поста
        post.setTitle("Updated Title");
        postRepository.updatePost(post);

        verify(repository, times(1)).save(post);
    }

    @Test
    void testDeletePost() {

        UUID postId = post.getId();

        when(repository.existsById(postId)).thenReturn(true);

        doNothing().when(repository).deleteById(postId);

        postRepository.deletePost(postId);

        verify(repository, times(1)).existsById(postId);
        verify(repository, times(1)).deleteById(postId);
    }

    @Test
    void testGetAllPosts() {
        PostEntity post1 = new PostEntity(
                UUID.randomUUID(),
                "Title 1",
                "Content 1",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L,
                false,
                "user123"
        );
        PostEntity post2 = new PostEntity(
                UUID.randomUUID(),
                "Title 2",
                "Content 2",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                0L,
                false,
                "user456"
        );

        when(repository.findAll()).thenReturn(List.of(post1, post2));

        List<PostEntity> posts = postRepository.getAllPosts();

        assertThat(posts).hasSize(2);
        assertThat(posts).extracting(PostEntity::getTitle).containsExactlyInAnyOrder("Title 1", "Title 2");

        verify(repository, times(1)).findAll();
    }
}