package com.example.newsService.app.mappers;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.core.mediafile.entities.post.MediafilePost;
import org.springframework.stereotype.Component;

@Component
public class MediafilePostMapper {

    public MediafileDTO toDto(MediafilePost mediafilePost) {
        return MediafileDTO.builder()
                .id(mediafilePost.getId())
                .url(mediafilePost.getUrl())
                .userId(mediafilePost.getUserId())
                .type(mediafilePost.getType())
                .build();
    }

    public MediafilePost toEntity(MediafileDTO mediafileDTO) {
        return new MediafilePost(
                mediafileDTO.getId(),
                mediafileDTO.getUrl(),
                mediafileDTO.getUserId(),
                mediafileDTO.getType());
    }

}
