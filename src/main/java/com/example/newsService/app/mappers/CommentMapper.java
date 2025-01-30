package com.example.newsService.app.mappers;

import com.example.newsService.app.DTO.CommentDTO;
import com.example.newsService.core.comment.entities.CommentEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentMapper {

    private final MediafileCommentMapper mediafileCommentMapper;

    public CommentDTO toDto(CommentEntity comment) {
        return CommentDTO.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .likeQuantity(comment.getLikeQuantity())
                .user_id(comment.getUser_id())
                .mediafiles(comment.getMediafileComments().stream()
                        .map(mediafileCommentMapper::toDto).toList())
                .postId(comment.getPost().getId())
                .build();
    }

    public CommentEntity toEntity(CommentDTO comment) {
        return new CommentEntity(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getLikeQuantity(),
                comment.getUser_id());
    }
}