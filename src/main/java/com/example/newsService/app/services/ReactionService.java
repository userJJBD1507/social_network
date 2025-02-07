package com.example.newsService.app.services;

import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.mappers.ReactionMapper;
import com.example.newsService.core.ReactionsCrud;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.reaction.entities.ReactionEntity;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.core.repositories.entity.EntityReactionRepository;
import com.example.newsService.core.repositories.entity.EntityUserReactionRepository;
import com.example.newsService.infra.repositories.JpaReactionRepository;
import com.example.newsService.infra.repositories.JpaUserReactionRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReactionService implements ReactionsCrud<ReactionEntity, UUID> {

    private final JpaReactionRepository jpaReactionRepository;
    private final ReactionMapper reactionMapper;

    private final EntityReactionRepository entityReactionRepository;

    private final EntityPostRepository entityPostRepository;

    private final JpaUserReactionRepository jpaUserReactionRepository;

    @Autowired
    private EntityUserReactionRepository userReactionRepository;
    @Autowired
    private S3StorageServiceImpl s3StorageService;

    @Override
    public void add(ReactionDTO dto) {
        if (dto == null) {
            log.error("ReactionDTO is null");
            throw new IllegalArgumentException("Reaction cannot be null");
        }
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");


        log.info("Adding reaction: {}", dto);

        ReactionEntity entity = reactionMapper.toEntity(dto);
        entity.setCreatedBy(username);
        jpaReactionRepository.addReaction(entity);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            log.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        ReactionEntity reaction = new ReactionEntity();
        reaction.setId(id);

        try {
            log.info("Deleting reaction with ID: {}", id);

            String fileReactionName = entityReactionRepository.findById(id).get().getUrl();

            userReactionRepository.delete(id);
            jpaReactionRepository.deleteReaction(reaction);

            s3StorageService.delete(fileReactionName);
        } catch (EntityNotFoundException e) {
            log.error("Reaction not found with ID: {}", id);
            throw e;
        }
    }


    public void replyToPost(String description, UUID postId) {
        log.info("Received request to reply to post with ID: {} and description: {}", postId, description);

        if (description == null || description.isEmpty()) {
            log.error("Description cannot be null or empty");
            throw new IllegalArgumentException("Description cannot be null or empty");
        }

        if (postId == null) {
            log.error("Post ID cannot be null");
            throw new IllegalArgumentException("Post ID cannot be null");
        }

        ReactionEntity reactionEntity = entityReactionRepository.findByDescription(description);
        if (reactionEntity == null) {
            log.warn("Reaction with description '{}' not found", description);
            throw new EntityNotFoundException("Reaction with description " + description + " not found");
        }

        PostEntity postEntity = entityPostRepository.findById(postId).orElse(null);
        if (postEntity == null) {
            log.warn("Post with ID '{}' not found", postId);
            throw new EntityNotFoundException("Post with ID " + postId + " not found");
        }

        UserReactionEntity userReaction = new UserReactionEntity();
        userReaction.setPost(postEntity);


        userReaction.setUserId(postEntity.getUserId());
        userReaction.setReaction(reactionEntity);

        jpaUserReactionRepository.addUserReaction(userReaction);
        log.info("User reaction added for post ID: {} with user ID: {} and reaction: {}", postId, postEntity.getUserId(), description);
    }

}