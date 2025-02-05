package com.example.newsService.infra.repositories;

import com.example.newsService.core.reaction.entities.ReactionEntity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.newsService.core.repositories.ReactionRepository;
import com.example.newsService.core.repositories.entity.EntityReactionRepository;

@Component
@RequiredArgsConstructor
public class JpaReactionRepository implements ReactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaReactionRepository.class);

    private final EntityReactionRepository entityReactionRepository;


    @Override
    public void addReaction(ReactionEntity reaction) {
        logger.info("Adding reaction: {}", reaction);
        entityReactionRepository.save(reaction);
    }

    @Override
    public void deleteReaction(ReactionEntity reaction) {
        if (!entityReactionRepository.existsById(reaction.getId())) {
            logger.error("Reaction not found with ID: {}", reaction.getId());
            throw new EntityNotFoundException("Reaction not found");
        }
        logger.info("Deleting reaction with ID: {}", reaction.getId());
        entityReactionRepository.delete(reaction);
    }
}


