package com.example.newsService.infra.repositories;

import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.PostRepository;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaPostRepository implements PostRepository {

    private final static Logger logger = LoggerFactory.getLogger(JpaPostRepository.class);

    private final EntityPostRepository repository;

    @Override
    public void addPost(PostEntity post) {
        logger.info("Saving post: {}", post);
        repository.save(post);
    }

    @Override
    public PostEntity getPost(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Post not found: {}", id);
                    return new RuntimeException("Post not found");
                });
    }

    @Override
    public void updatePost(PostEntity post) {
        logger.info("Updating post: {}", post);
        repository.save(post);
    }

    @Override
    public void deletePost(UUID id) {
        if (!repository.existsById(id)) {
            logger.error("Post not found: {}", id);
            throw new RuntimeException("Post not found");
        }
        logger.info("Deleting post: {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<PostEntity> getAllPosts() {
        logger.info("Getting all posts");
        return repository.findAll();
    }
}
