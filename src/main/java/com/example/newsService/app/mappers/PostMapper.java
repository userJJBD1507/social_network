package com.example.newsService.app.mappers;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.core.post.entities.PostEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {

    private final MediafilePostMapper mediafilePostMapper;

    public PostDTO toDto(PostEntity post) {
        return PostDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .likeQuantity(post.getLikeQuantity())
                .commentQuantity(post.getCommentQuantity())
                .isPrivate(post.isPrivate())
                .userId(post.getUserId())
                .mediafiles(post.getMediafilePosts()
                        .stream()
                        .map(mediafilePostMapper::toDto).toList())
                .build();
    }

    public PostEntity toEntity(PostDTO post) {
        return new PostEntity(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                post.getLikeQuantity(),
                post.getCommentQuantity(),
                post.isPrivate(),
                post.getUserId());
    }

}
