package com.example.newsService.infra.repositories;

import com.example.newsService.core.comment.entities.CommentEntity;
import com.example.newsService.core.repositories.CommentRepository;
import com.example.newsService.core.repositories.entity.EntityCommentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class JpaCommentRepository implements CommentRepository {

    private final static Logger logger = LoggerFactory.getLogger(JpaCommentRepository.class);

    private final EntityCommentRepository repository;

    @Autowired
    public JpaCommentRepository(EntityCommentRepository repository) {
        this.repository = repository;
    }

    @Override
    public void addComment(CommentEntity comment) {
        logger.info("Saving comment: {}", comment);
        repository.save(comment);
    }

    @Override
    public CommentEntity getComment(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Comment not found: {}", id);
                    return new RuntimeException("Comment not found");
                });
    }

    @Override
    public void updateComment(CommentEntity comment) {
        logger.info("Updating comment: {}", comment);
        repository.save(comment);
    }

    @Override
    public void deleteComment(UUID id) {
        if(!repository.existsById(id)) {
            logger.error("Comment not found: {}", id);
            throw new RuntimeException("Comment not found");
        }
        logger.info("Deleting comment: {}", id);
        repository.deleteById(id);
    }

    @Override
    public List<CommentEntity> getAllComments() {
        logger.info("Getting all comments");
        return repository.findAll();
    }
}
