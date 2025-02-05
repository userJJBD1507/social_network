package com.example.newsService.app.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.mappers.PostMapper;
import com.example.newsService.core.CrudService;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.infra.repositories.JpaPostRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class PostService implements CrudService<PostDTO, UUID> {

    private static final Logger logger = LoggerFactory.getLogger(PostService.class);


    private final JpaPostRepository jpaPostRepository;


    private final PostMapper postMapper;

    @Override
    public PostDTO add(PostDTO dto) {
        if (dto == null) {
            logger.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        logger.info("Adding post with title: {}", dto.getTitle());
        PostEntity entity = postMapper.toEntity(dto);
        jpaPostRepository.addPost(entity);
        return postMapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            logger.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            logger.info("Deleting post with ID: {}", id);
            jpaPostRepository.deletePost(id);
        } catch (EntityNotFoundException e) {
            logger.error("Post not found with ID: {}", id);
            throw e;
        }
    }

    @Override
    public Optional<PostDTO> get(UUID id) {
        if (id == null) {
            logger.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            logger.info("Fetching post with ID: {}", id);
            PostEntity post = jpaPostRepository.getPost(id);
            return Optional.of(postMapper.toDto(post));
        } catch (EntityNotFoundException e) {
            logger.error("Post not found with ID: {}", id);
            return Optional.empty();
        }
    }
    @Override
    public PostDTO update(UUID id, PostDTO dto) {
        if (dto == null) {
            logger.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        if (dto.getUserId() == null) {
            logger.error("PostDTO userId is null");
            throw new IllegalArgumentException("UserId cannot be null");
        }
        logger.info("Updating post with title: {}", dto.getTitle());
        // PostEntity entity = postMapper.toEntity(dto);
        // entity.setId(id);
        PostEntity existingEntity = jpaPostRepository.getPost(id);
        if (existingEntity != null) {
            existingEntity.setTitle(dto.getTitle());
            existingEntity.setContent(dto.getContent());
            existingEntity.setPrivate(dto.isPrivate());
            existingEntity.setUserId(dto.getUserId());
            // entity.setCreatedAt(existingEntity.getCreatedAt());
            // entity.setUpdatedAt(new Date());
        }
        jpaPostRepository.updatePost(existingEntity);
        return postMapper.toDto(existingEntity);
    }




    public PostDTO addPostAsReply(UUID parentPostId, PostDTO dto) {
        if (dto == null) {
            logger.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        logger.info("Adding post with title: {}", dto.getTitle());
        PostEntity parentPost = jpaPostRepository.getPost(parentPostId);

        PostEntity entity = postMapper.toEntity(dto);
        entity.setParentPost(parentPost);
        jpaPostRepository.addPost(entity);
        return postMapper.toDto(entity);
    }
}