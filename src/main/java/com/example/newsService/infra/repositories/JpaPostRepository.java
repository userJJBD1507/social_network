package com.example.newsService.infra.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.PostRepository;
import com.example.newsService.core.repositories.entity.EntityPostRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class JpaPostRepository implements PostRepository {

    private final EntityPostRepository entityPostRepository;

    @Override
    public void addPost(PostEntity post) {
        log.info("Adding post: {}", post);
        entityPostRepository.save(post);
    }

    @Override
    public PostEntity getPost(UUID id) {
        log.info("Fetching post with ID: {}", id);
        return entityPostRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Post not found with ID: {}", id);
                    return new EntityNotFoundException("Post not found");
                });
    }

    @Override
    public void updatePost(PostEntity post) {
        log.info("Updating post: {}", post);
        entityPostRepository.save(post);
    }

    @Override
    public void deletePost(UUID id) {
        try {
            log.info("Deleting post with ID: {}", id);
            entityPostRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Post not found with ID: {}", id);
            throw new EntityNotFoundException("Post not found");
        }
    }

    @Override
    public List<PostEntity> getAllPosts() {
        log.info("Fetching all posts");
        return entityPostRepository.findAll();
    }
}