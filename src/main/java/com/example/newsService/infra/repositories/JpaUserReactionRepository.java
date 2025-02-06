package com.example.newsService.infra.repositories;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.UserReactionRepository;
import com.example.newsService.core.repositories.entity.EntityUserReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaUserReactionRepository implements UserReactionRepository {

    private final EntityUserReactionRepository entityUserReactionRepository;

    @Override
    public void addUserReaction(UserReactionEntity userReaction) {
        log.info("Adding user reaction: {}", userReaction);
        entityUserReactionRepository.save(userReaction);
    }

    @Override
    public void deleteUserReaction(UserReactionEntity userReaction) {
        if (!entityUserReactionRepository.existsById(userReaction.getId())) {
            log.error("User reaction not found with ID: {}", userReaction.getId());
            throw new EntityNotFoundException("User reaction not found");
        }
        log.info("Deleting user reaction with ID: {}", userReaction.getId());
        entityUserReactionRepository.delete(userReaction);
    }
}