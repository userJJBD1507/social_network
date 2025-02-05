package com.example.newsService.app.mappers;

import com.example.newsService.core.repositories.entity.EntityPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.core.mediafile.entities.MediafileEntity;

@Component
@RequiredArgsConstructor
public class MediafileMapper {

    private final EntityPostRepository postRepository;

    public MediafileDTO toDTO(MediafileEntity mediafileEntity) {
        if (mediafileEntity == null) {
            return null;
        }

        return MediafileDTO.builder()
                .id(mediafileEntity.getId())
                .type(mediafileEntity.getType())
                .url(mediafileEntity.getUrl())
                .postId(mediafileEntity.getPost().getId())
                .build();
    }

    public MediafileEntity toEntity(MediafileDTO mediafileDTO) {
        if (mediafileDTO == null) {
            return null;
        }

        MediafileEntity mediafileEntity = new MediafileEntity();
        mediafileEntity.setType(mediafileDTO.getType());
        // mediafileEntity.setId(mediafileDTO.getId());
        mediafileEntity.setUrl(mediafileDTO.getUrl());
        mediafileEntity.setPost(postRepository.findById(mediafileDTO.getPostId()).orElse(null));

        return mediafileEntity;
    }
}