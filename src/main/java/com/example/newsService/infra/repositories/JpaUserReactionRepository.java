package com.example.newsService.infra.repositories;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.UserReactionRepository;
import com.example.newsService.core.repositories.entity.EntityUserReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
public class JpaUserReactionRepository implements UserReactionRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaUserReactionRepository.class);

    @Autowired
    private EntityUserReactionRepository entityUserReactionRepository;

    @Override
    public void addUserReaction(UserReactionEntity userReaction) {
        logger.info("Adding user reaction: {}", userReaction);
        entityUserReactionRepository.save(userReaction);
    }

    @Override
    public void deleteUserReaction(UserReactionEntity userReaction) {
        if (!entityUserReactionRepository.existsById(userReaction.getId())) {
            logger.error("User reaction not found with ID: {}", userReaction.getId());
            throw new EntityNotFoundException("User reaction not found");
        }
        logger.info("Deleting user reaction with ID: {}", userReaction.getId());
        entityUserReactionRepository.delete(userReaction);
    }
}