package com.example.newsService.app.mappers;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.core.mediafile.entities.comment.MediafileComment;
import org.springframework.stereotype.Component;

@Component
public class MediafileCommentMapper {

    public MediafileDTO toDto(MediafileComment mediafileComment) {
        return MediafileDTO.builder()
                .id(mediafileComment.getId())
                .url(mediafileComment.getUrl())
                .userId(mediafileComment.getUserId())
                .type(mediafileComment.getType())
                .build();
    }

    public MediafileComment toEntity(MediafileDTO mediafileDTO) {
        return new MediafileComment(
                mediafileDTO.getId(),
                mediafileDTO.getUrl(),
                mediafileDTO.getUserId(),
                mediafileDTO.getType());
    }
}
