package com.example.newsService.app.mappers;

import org.springframework.stereotype.Component;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.core.reaction.entities.ReactionEntity;

@Component
public class ReactionMapper {

    public static ReactionDTO toDto(ReactionEntity entity) {
        if (entity == null) {
            return null;
        }

        return ReactionDTO.builder()
                .reaction(entity.getReaction())
                .build();
    }

    public static ReactionEntity toEntity(ReactionDTO dto) {
        if (dto == null) {
            return null;
        }

        ReactionEntity entity = new ReactionEntity();
        entity.setReaction(dto.getReaction());
        return entity;
    }
}