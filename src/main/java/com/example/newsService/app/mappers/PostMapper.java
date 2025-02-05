package com.example.newsService.app.mappers;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public static PostDTO toDto(PostEntity entity) {
        if (entity == null) {
            return null;
        }
        return PostDTO.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .content(entity.getContent())
                .likeQuantity(entity.getLikeQuantity())
                .commentQuantity(entity.getCommentQuantity())
                .isPrivate(entity.isPrivate())
                .userId(entity.getUserId())
                .createdAt(new java.sql.Date(entity.getCreatedAt().getTime()))
                .updatedAt(new java.sql.Date(entity.getUpdatedAt().getTime()))
                .commentIds(entity.getComments() != null ?
                        entity.getComments().stream()
                                .map(PostEntity::getId)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .parentPostId(entity.getParentPost() != null ? entity.getParentPost().getId() : null)
                .userReactionIds(entity.getUserReactions() != null ?
                        entity.getUserReactions().stream()
                                .map(UserReactionEntity::getId)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .mediafileIds(entity.getMediafiles() != null ?
                        entity.getMediafiles().stream()
                                .map(MediafileEntity::getId)
                                .collect(Collectors.toList()) : new ArrayList<>())
                .build();
    }

    public static PostEntity toEntity(PostDTO dto) {
        if (dto == null) {
            return null;
        }
        PostEntity entity = new PostEntity();
        entity.setId(dto.getId());
        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setLikeQuantity(dto.getLikeQuantity());
        entity.setCommentQuantity(dto.getCommentQuantity());
        entity.setPrivate(dto.isPrivate());
        entity.setUserId(dto.getUserId());
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());

        if (dto.getCommentIds() != null && !dto.getCommentIds().isEmpty()) {
            List<PostEntity> comments = dto.getCommentIds().stream()
                    .map(commentId -> {
                        PostEntity commentEntity = new PostEntity();
                        commentEntity.setId(commentId);
                        commentEntity.setParentPost(entity);
                        return commentEntity;
                    })
                    .collect(Collectors.toList());
            entity.setComments(comments);
        }

        if (dto.getParentPostId() != null) {
            PostEntity parentPost = new PostEntity();
            parentPost.setId(dto.getParentPostId());
            entity.setParentPost(parentPost);
        }

        if (dto.getUserReactionIds() != null && !dto.getUserReactionIds().isEmpty()) {
            List<UserReactionEntity> userReactions = dto.getUserReactionIds().stream()
                    .map(reactionId -> {
                        UserReactionEntity userReactionEntity = new UserReactionEntity();
                        userReactionEntity.setId(reactionId);
                        userReactionEntity.setPost(entity);
                        return userReactionEntity;
                    })
                    .collect(Collectors.toList());
            entity.setUserReactions(userReactions);
        }

        if (dto.getMediafileIds() != null && !dto.getMediafileIds().isEmpty()) {
            List<MediafileEntity> mediafiles = dto.getMediafileIds().stream()
                    .map(mediafileId -> {
                        MediafileEntity mediafileEntity = new MediafileEntity();
                        mediafileEntity.setId(mediafileId);
                        mediafileEntity.setPost(entity);
                        return mediafileEntity;
                    })
                    .collect(Collectors.toList());
            entity.setMediafiles(mediafiles);
        }

        return entity;
    }
}