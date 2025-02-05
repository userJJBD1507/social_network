package com.example.newsService.app.services;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.newsService.core.CrudService;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.infra.repositories.JpaUserReactionRepository;

import jakarta.persistence.EntityNotFoundException;

// @Service
// public class UserReactionService implements ReactionsCrud<UserReactionEntity, UUID> {

//     private static final Logger logger = LoggerFactory.getLogger(UserReactionService.class);

//     @Autowired
//     private JpaUserReactionRepository jpaUserReactionRepository;

//     @Override
//     public void add(UserReactionEntity entity) {
//         if (entity == null) {
//             logger.error("UserReactionEntity is null");
//             throw new IllegalArgumentException("User reaction cannot be null");
//         }
//         logger.info("Adding user reaction: {}", entity);
//         jpaUserReactionRepository.addUserReaction(entity);
//     }

//     @Override
//     public void delete(UUID id) {
//         if (id == null) {
//             logger.error("ID is null");
//             throw new IllegalArgumentException("ID cannot be null");
//         }
//         UserReactionEntity userReaction = new UserReactionEntity();
//         userReaction.setId(id);

//         try {
//             logger.info("Deleting user reaction with ID: {}", id);
//             jpaUserReactionRepository.deleteUserReaction(userReaction);
//         } catch (EntityNotFoundException e) {
//             logger.error("User reaction not found with ID: {}", id);
//             throw e;
//         }
//     }
// }