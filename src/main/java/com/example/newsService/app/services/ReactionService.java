package com.example.newsService.app.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.mappers.ReactionMapper;
import com.example.newsService.core.ReactionsCrud;
import com.example.newsService.core.reaction.entities.ReactionEntity;
import com.example.newsService.infra.repositories.JpaReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReactionService implements ReactionsCrud<ReactionEntity, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(ReactionService.class);

    private final JpaReactionRepository jpaReactionRepository;

    private final ReactionMapper reactionMapper;

    @Override
    public void add(ReactionDTO dto) {
        if (dto == null) {
            logger.error("ReactionDTO is null");
            throw new IllegalArgumentException("Reaction cannot be null");
        }
        logger.info("Adding reaction: {}", dto);

        ReactionEntity entity = reactionMapper.toEntity(dto);
        jpaReactionRepository.addReaction(entity);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            logger.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        ReactionEntity reaction = new ReactionEntity();
        reaction.setId(id);

        try {
            logger.info("Deleting reaction with ID: {}", id);
            jpaReactionRepository.deleteReaction(reaction);
        } catch (EntityNotFoundException e) {
            logger.error("Reaction not found with ID: {}", id);
            throw e;
        }
    }
}