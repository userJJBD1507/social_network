package com.example.newsService.infra.repositories;

import com.example.newsService.core.like.entities.post.PostLikes;
import com.example.newsService.core.repositories.PostLikesRepository;
import com.example.newsService.core.repositories.entity.EntityPostLikesRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class JpaPostLikesRepository implements PostLikesRepository {

    private final static Logger logger = LoggerFactory.getLogger(JpaPostLikesRepository.class);

    private final EntityPostLikesRepository repository;

    @Override
    public void addPostLike(PostLikes postLikes) {
        logger.info("Saving post like: {}", postLikes);
        repository.save(postLikes);
    }

    @Override
    public PostLikes getPostLike(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Post like not found: {}", id);
                    return new RuntimeException("Post like not found");
                });
    }

    @Override
    public void deletePostLike(UUID id) {
        if (!repository.existsById(id)) {
            logger.error("Post like not found: {}", id);
            throw new RuntimeException("Post like not found");
        }
        logger.info("Deleting post like: {}", id);
        repository.deleteById(id);
    }
}
