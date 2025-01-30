package com.example.newsService.unit.mappers;

import com.example.newsService.app.DTO.CommentDTO;
import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.mappers.CommentMapper;
import com.example.newsService.app.mappers.MediafileCommentMapper;
import com.example.newsService.core.comment.entities.CommentEntity;
import com.example.newsService.core.mediafile.entities.comment.MediafileComment;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.utils.MediaFileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CommentMapperTest {

    @Mock
    private MediafileCommentMapper mediafileCommentMapper;

    @InjectMocks
    private CommentMapper commentMapper;

    private CommentDTO commentDTO;
    private CommentEntity commentEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<MediafileComment> mediafiles = List.of(
                new MediafileComment(
                        UUID.randomUUID(),
                        "url",
                        "user123",
                        MediaFileType.PHOTO),
                new MediafileComment(
                        UUID.randomUUID(),
                        "url",
                        "user123",
                        MediaFileType.PHOTO)
        );

        commentDTO = CommentDTO.builder()
                .id(UUID.randomUUID())
                .content("Test comment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .likeQuantity(10L)
                .user_id("user123")
                .postId(UUID.randomUUID())
                .mediafiles(Collections.emptyList())
                .build();

        commentEntity = new CommentEntity();
        commentEntity.setMediafileComments(mediafiles);
        commentEntity.setId(commentDTO.getId());
        commentEntity.setContent(commentDTO.getContent());
        commentEntity.setCreatedAt(commentDTO.getCreatedAt());
        commentEntity.setUpdatedAt(commentDTO.getUpdatedAt());
        commentEntity.setLikeQuantity(commentDTO.getLikeQuantity());
        commentEntity.setUser_id(commentDTO.getUser_id());

        PostEntity postEntity = new PostEntity();
        postEntity.setId(commentDTO.getPostId());
        commentEntity.setPost(postEntity);
    }

    @Test
    public void testToDto() {

        when(mediafileCommentMapper.toDto(any())).thenReturn(new MediafileDTO());


        CommentDTO result = commentMapper.toDto(commentEntity);

        assertEquals(commentEntity.getId(), result.getId());
        assertEquals(commentEntity.getContent(), result.getContent());
        assertEquals(commentEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(commentEntity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(commentEntity.getLikeQuantity(), result.getLikeQuantity());
        assertEquals(commentEntity.getUser_id(), result.getUser_id());
        assertEquals(commentEntity.getPost().getId(), result.getPostId());

        verify(mediafileCommentMapper, times(commentEntity.getMediafileComments().size())).toDto(any());
    }

    @Test
    public void testToEntity() {

        CommentEntity result = commentMapper.toEntity(commentDTO);

        assertEquals(commentDTO.getId(), result.getId());
        assertEquals(commentDTO.getContent(), result.getContent());
        assertEquals(commentDTO.getCreatedAt(), result.getCreatedAt());
        assertEquals(commentDTO.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(commentDTO.getLikeQuantity(), result.getLikeQuantity());
        assertEquals(commentDTO.getUser_id(), result.getUser_id());

    }
}
