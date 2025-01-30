package com.example.newsService.infra.repositories;

import com.example.newsService.core.like.entities.comment.CommentLikes;
import com.example.newsService.core.repositories.CommentLikesRepository;
import com.example.newsService.core.repositories.entity.EntityCommentLikesRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class JpaCommentLikesRepository implements CommentLikesRepository {

    private final static Logger logger = LoggerFactory.getLogger(JpaCommentLikesRepository.class);

    private final EntityCommentLikesRepository repository;

    @Autowired
    public JpaCommentLikesRepository(EntityCommentLikesRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addCommentLike(CommentLikes commentLikes) {
        logger.info("Saving comment like: {}", commentLikes);
        repository.save(commentLikes);
    }

    @Override
    public CommentLikes getCommentLike(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Comment like not found: {}", id);
                    return new RuntimeException("Comment like not found");
                });
    }

    @Override
    public void deleteCommentLike(UUID id) {
        if(!repository.existsById(id)) {
            logger.error("Comment like not found: {}", id);
            throw new RuntimeException("Comment like not found");
        }
        logger.info("Deleting comment like: {}", id);
        repository.deleteById(id);
    }
}
