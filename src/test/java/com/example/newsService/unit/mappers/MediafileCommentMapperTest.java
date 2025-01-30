package com.example.newsService.unit.mappers;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.mappers.MediafileCommentMapper;
import com.example.newsService.core.mediafile.entities.comment.MediafileComment;
import com.example.newsService.core.utils.MediaFileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediafileCommentMapperTest {

    private MediafileCommentMapper mediafileCommentMapper;
    private MediafileComment mediafileComment;

    @BeforeEach
    void setUp() {
        mediafileCommentMapper = new MediafileCommentMapper();

        mediafileComment = new MediafileComment(
                UUID.randomUUID(),
                "http://example.com/comment_image.jpg",
                "user123",
                MediaFileType.PHOTO
        );
    }

    @Test
    public void testToDto() {
        // Act
        MediafileDTO result = mediafileCommentMapper.toDto(mediafileComment);

        // Assert
        assertEquals(mediafileComment.getId(), result.getId());
        assertEquals(mediafileComment.getUrl(), result.getUrl());
        assertEquals(mediafileComment.getUserId(), result.getUserId());
        assertEquals(mediafileComment.getType(), result.getType());
    }

    @Test
    public void testToEntity() {

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .id(mediafileComment.getId())
                .url(mediafileComment.getUrl())
                .userId(mediafileComment.getUserId())
                .type(mediafileComment.getType())
                .build();

        MediafileComment result = mediafileCommentMapper.toEntity(mediafileDTO);
    }
}