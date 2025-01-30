package com.example.newsService.unit.repositories;

import com.example.newsService.core.comment.entities.CommentEntity;
import com.example.newsService.core.repositories.entity.EntityCommentRepository;
import com.example.newsService.infra.repositories.JpaCommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

public class JpaCommentRepositoryTest {

    @Mock
    private EntityCommentRepository repository;

    @InjectMocks
    private JpaCommentRepository jpaCommentRepository;

    private CommentEntity comment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        comment = new CommentEntity(
                UUID.randomUUID(),
                "Test Content",
                LocalDateTime.now(),
                LocalDateTime.now(),
                0L,
                "user123"
        );
    }

    @Test
    void testAddComment() {
        when(repository.save(comment)).thenReturn(comment);

        jpaCommentRepository.addComment(comment);

        verify(repository, times(1)).save(comment);
    }

    @Test
    void testGetComment() {
        when(repository.findById(comment.getId())).thenReturn(Optional.of(comment));

        CommentEntity foundComment = jpaCommentRepository.getComment(comment.getId());

        assertThat(foundComment).isNotNull();
        assertThat(foundComment.getContent()).isEqualTo("Test Content");

        verify(repository, times(1)).findById(comment.getId());
    }

    @Test
    void testDeleteComment() {
        when(repository.existsById(comment.getId())).thenReturn(true);

        doNothing().when(repository).deleteById(comment.getId());

        jpaCommentRepository.deleteComment(comment.getId());

        verify(repository, times(1)).existsById(comment.getId());
        verify(repository, times(1)).deleteById(comment.getId());
    }

    @Test
    void testUpdateComment() {
        when(repository.save(comment)).thenReturn(comment);

        comment.setContent("Updated Content");
        jpaCommentRepository.updateComment(comment);

        verify(repository, times(1)).save(comment);
    }

}
