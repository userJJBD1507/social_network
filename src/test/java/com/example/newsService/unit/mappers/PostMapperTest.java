package com.example.newsService.unit.mappers;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.mappers.MediafilePostMapper;
import com.example.newsService.app.mappers.PostMapper;
import com.example.newsService.core.mediafile.entities.post.MediafilePost;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.utils.MediaFileType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PostMapperTest {

    @Mock
    private MediafilePostMapper mediafilePostMapper;

    @InjectMocks
    private PostMapper postMapper;

    private PostDTO postDTO;
    private PostEntity postEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        List<MediafileDTO> mediafiles = List.of(
                new MediafileDTO(
                        UUID.randomUUID(),
                        "url",
                        "user123",
                        MediaFileType.PHOTO),
                new MediafileDTO(
                        UUID.randomUUID(),
                        "url",
                        "user123",
                        MediaFileType.PHOTO)
        );

        postDTO = new PostDTO();
        postDTO.setId(UUID.randomUUID());
        postDTO.setContent("Test Content");
        postDTO.setTitle("Test Title");
        postDTO.setCreatedAt(LocalDateTime.now());
        postDTO.setUpdatedAt(LocalDateTime.now());
        postDTO.setLikeQuantity(0L);
        postDTO.setCommentQuantity(0L);
        postDTO.setPrivate(false);
        postDTO.setUserId("user123");
        postDTO.setMediafiles(mediafiles);

        postEntity = new PostEntity();
        postEntity.setId(postDTO.getId());
        postEntity.setContent(postDTO.getContent());
        postEntity.setTitle(postDTO.getTitle());
        postEntity.setCreatedAt(postDTO.getCreatedAt());
        postEntity.setUpdatedAt(postDTO.getUpdatedAt());
        postEntity.setLikeQuantity(postDTO.getLikeQuantity());
        postEntity.setCommentQuantity(postDTO.getCommentQuantity());
        postEntity.setPrivate(postDTO.isPrivate());
        postEntity.setUserId(postDTO.getUserId());
    }

    @Test
    public void testToEntity() {

        PostEntity result = postMapper.toEntity(postDTO);

        assertEquals(postEntity.getId(), result.getId());
        assertEquals(postEntity.getTitle(), result.getTitle());
        assertEquals(postEntity.getContent(), result.getContent());
        assertEquals(postEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(postEntity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(postEntity.getLikeQuantity(), result.getLikeQuantity());
        assertEquals(postEntity.getCommentQuantity(), result.getCommentQuantity());
        assertEquals(postEntity.isPrivate(), result.isPrivate());
        assertEquals(postEntity.getUserId(), result.getUserId());
    }

    @Test
    public void testToDto_WithMediafiles() {
        List<MediafilePost> mediafile = List.of(
                new MediafilePost(UUID.randomUUID(), "url1", "user123", MediaFileType.PHOTO),
                new MediafilePost(UUID.randomUUID(), "url2", "user123", MediaFileType.PHOTO)
        );

        postEntity.setMediafilePosts(mediafile);

        when(mediafilePostMapper.toDto(any())).thenReturn(new MediafileDTO());

        PostDTO result = postMapper.toDto(postEntity);

        assertEquals(postEntity.getId(), result.getId());
        assertEquals(postEntity.getTitle(), result.getTitle());
        assertEquals(postEntity.getContent(), result.getContent());
        assertEquals(postEntity.getCreatedAt(), result.getCreatedAt());
        assertEquals(postEntity.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(postEntity.getLikeQuantity(), result.getLikeQuantity());
        assertEquals(postEntity.getCommentQuantity(), result.getCommentQuantity());
        assertEquals(postEntity.isPrivate(), result.isPrivate());
        assertEquals(postEntity.getUserId(), result.getUserId());

        verify(mediafilePostMapper, times(mediafile.size())).toDto(any());
    }
}