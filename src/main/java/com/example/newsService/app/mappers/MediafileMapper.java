package com.example.newsService.app.mappers;

import org.springframework.stereotype.Component;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.core.mediafile.entities.MediafileEntity;

@Component
public class MediafileMapper {

    public MediafileDTO toDTO(MediafileEntity mediafileEntity) {
        if (mediafileEntity == null) {
            return null;
        }

        return MediafileDTO.builder()
                .type(mediafileEntity.getType())
                .build();
    }

    public MediafileEntity toEntity(MediafileDTO mediafileDTO) {
        if (mediafileDTO == null) {
            return null;
        }

        MediafileEntity mediafileEntity = new MediafileEntity();
        mediafileEntity.setType(mediafileDTO.getType());

        return mediafileEntity;
    }
}