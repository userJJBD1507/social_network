package com.example.newsService.app.services;

import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.mappers.PostMapper;
import com.example.newsService.core.CrudService;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.infra.repositories.JpaPostRepository;
import com.example.newsService.infra.services.KeycloakService;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;


@Service
@RequiredArgsConstructor
@Slf4j
public class PostService implements CrudService<PostDTO, UUID> {


    private final JpaPostRepository jpaPostRepository;


    private final PostMapper postMapper;
    @Autowired
    private KeycloakService keycloakService;
    @Override
    public PostDTO add(PostDTO dto) {
        if (dto == null) {
            log.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String userKeycloakId = keycloakService.getUserIdByUsername(username);
        UUID userUuid = UUID.fromString(userKeycloakId);
        
        dto.setUserId(userUuid);

        log.info("Adding post with title: {}", dto.getTitle());
        PostEntity entity = postMapper.toEntity(dto);

        entity.setCreatedBy(username);
        jpaPostRepository.addPost(entity);
        return postMapper.toDto(entity);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            log.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            log.info("Deleting post with ID: {}", id);
            jpaPostRepository.deletePost(id);
        } catch (EntityNotFoundException e) {
            log.error("Post not found with ID: {}", id);
            throw e;
        }
    }

    @Override
    public Optional<PostDTO> get(UUID id) {
        if (id == null) {
            log.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            log.info("Fetching post with ID: {}", id);
            PostEntity post = jpaPostRepository.getPost(id);
            return Optional.of(postMapper.toDto(post));
        } catch (EntityNotFoundException e) {
            log.error("Post not found with ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public PostDTO update(UUID id, PostDTO dto) {
        if (dto == null) {
            log.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        if (dto.getUserId() == null) {
            log.error("PostDTO userId is null");
            throw new IllegalArgumentException("UserId cannot be null");
        }

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        String userKeycloakId = keycloakService.getUserIdByUsername(username);
        UUID userUuid = UUID.fromString(userKeycloakId);

        log.info("Updating post with title: {}", dto.getTitle());
        PostEntity existingEntity = jpaPostRepository.getPost(id);
        if (existingEntity != null) {
            existingEntity.setTitle(dto.getTitle());
            existingEntity.setContent(dto.getContent());
            existingEntity.setPrivate(dto.isPrivate());
            existingEntity.setUserId(userUuid);
            existingEntity.setUpdatedBy(username);
        }
        jpaPostRepository.updatePost(existingEntity);
        return postMapper.toDto(existingEntity);
    }


    public PostDTO addPostAsReply(UUID parentPostId, PostDTO dto) {
        if (dto == null) {
            log.error("PostDTO is null");
            throw new IllegalArgumentException("PostDTO cannot be null");
        }
        log.info("Adding post with title: {}", dto.getTitle());
        PostEntity parentPost = jpaPostRepository.getPost(parentPostId);

        PostEntity entity = postMapper.toEntity(dto);
        entity.setParentPost(parentPost);
        jpaPostRepository.addPost(entity);
        return postMapper.toDto(entity);
    }
}