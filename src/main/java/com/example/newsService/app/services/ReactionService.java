package com.example.newsService.app.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.mappers.ReactionMapper;
import com.example.newsService.core.ReactionsCrud;
import com.example.newsService.core.UserReaction;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.reaction.entities.ReactionEntity;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.core.repositories.entity.EntityReactionRepository;
import com.example.newsService.infra.repositories.JpaReactionRepository;
import com.example.newsService.infra.repositories.JpaUserReactionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class ReactionService implements ReactionsCrud<ReactionEntity, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(ReactionService.class);

    private final JpaReactionRepository jpaReactionRepository;
    private final ReactionMapper reactionMapper;

    private final EntityReactionRepository entityReactionRepository;
    @Autowired
    private EntityPostRepository entityPostRepository;
    @Autowired
    private JpaUserReactionRepository jpaUserReactionRepository;

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

    //ФИКС, НЕ СОХРАНЯЕТ
    public void replyToPost(String description, UUID postId) {
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("Description cannot be null or empty");
        }
    
        if (postId == null) {
            throw new IllegalArgumentException("Post ID cannot be null");
        }
        ReactionEntity reactionEntity = entityReactionRepository.findByDescription(description);
        if (reactionEntity == null) {
            throw new EntityNotFoundException("Reaction with description " + description + " not found");
        }
        PostEntity postEntity = entityPostRepository.findById(postId).orElse(null);
        if (postEntity == null) {
            throw new EntityNotFoundException("Post with ID " + postId + " not found");
        }
    
        UserReactionEntity userReaction = new UserReactionEntity();
        userReaction.setId(UUID.randomUUID());
        userReaction.setPost(postEntity);
        userReaction.setReaction(reactionEntity);
    
        jpaUserReactionRepository.addUserReaction(userReaction);
    }
}