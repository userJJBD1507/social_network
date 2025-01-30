package com.example.newsService.unit.mappers;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.mappers.MediafilePostMapper;
import com.example.newsService.core.mediafile.entities.post.MediafilePost;
import com.example.newsService.core.utils.MediaFileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MediafilePostMapperTest {

    private MediafilePostMapper mediafilePostMapper;
    private MediafilePost mediafilePost;

    @BeforeEach
    void setUp() {
        mediafilePostMapper = new MediafilePostMapper();

        mediafilePost = new MediafilePost(
                UUID.randomUUID(),
                "http://example.com/image.jpg",
                "user123",
                MediaFileType.PHOTO
        );
    }

    @Test
    public void testToDto() {

        MediafileDTO result = mediafilePostMapper.toDto(mediafilePost);

        // Assert
        assertEquals(mediafilePost.getId(), result.getId());
        assertEquals(mediafilePost.getUrl(), result.getUrl());
        assertEquals(mediafilePost.getUserId(), result.getUserId());
        assertEquals(mediafilePost.getType(), result.getType());
    }

    @Test
    public void testToEntity() {
        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .id(mediafilePost.getId())
                .url(mediafilePost.getUrl())
                .userId(mediafilePost.getUserId())
                .type(mediafilePost.getType())
                .build();

        MediafilePost result = mediafilePostMapper.toEntity(mediafileDTO);

        assertEquals(mediafileDTO.getId(), result.getId());
        assertEquals(mediafileDTO.getUrl(), result.getUrl());
        assertEquals(mediafileDTO.getUserId(), result.getUserId());
        assertEquals(mediafileDTO.getType(), result.getType());
    }
}
