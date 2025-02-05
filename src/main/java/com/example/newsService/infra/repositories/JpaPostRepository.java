package com.example.newsService.infra.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.PostRepository;
import com.example.newsService.core.repositories.entity.EntityPostRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
public class JpaPostRepository implements PostRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaPostRepository.class);

    @Autowired
    private EntityPostRepository entityPostRepository;

    @Override
    public void addPost(PostEntity post) {
        logger.info("Adding post: {}", post);
        entityPostRepository.save(post);
    }

    @Override
    public PostEntity getPost(UUID id) {
        logger.info("Fetching post with ID: {}", id);
        return entityPostRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Post not found with ID: {}", id);
                    return new EntityNotFoundException("Post not found");
                });
    }

    @Override
    public void updatePost(PostEntity post) {
        logger.info("Updating post: {}", post);
        entityPostRepository.save(post);
    }

    @Override
    public void deletePost(UUID id) {
        try {
            logger.info("Deleting post with ID: {}", id);
            entityPostRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Post not found with ID: {}", id);
            throw new EntityNotFoundException("Post not found");
        }
    }

    @Override
    public List<PostEntity> getAllPosts() {
        logger.info("Fetching all posts");
        return entityPostRepository.findAll();
    }
}