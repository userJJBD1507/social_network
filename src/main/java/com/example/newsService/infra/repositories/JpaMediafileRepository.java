package com.example.newsService.infra.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Component;

import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.repositories.MediafileRepository;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.UUID;

@Component
public class JpaMediafileRepository implements MediafileRepository {

    private static final Logger logger = LoggerFactory.getLogger(JpaMediafileRepository.class);

    @Autowired
    private EntityMediafileRepository entityMediafileRepository;

    @Override
    public void addMediafile(MediafileEntity mediafile) {
        logger.info("Adding mediafile: {}", mediafile);
        entityMediafileRepository.save(mediafile);
    }

    // @Override
    // public MediafileEntity getPost(UUID id) {
    //     logger.info("Fetching mediafile with ID: {}", id);
    //     return entityMediafileRepository.findById(id)
    //             .orElseThrow(() -> {
    //                 logger.error("Mediafile not found with ID: {}", id);
    //                 return new EntityNotFoundException("Mediafile not found");
    //             });
    // }
    @Override
    public MediafileEntity getMediafile(UUID id) {
        logger.info("Fetching mediafile with ID: {}", id);
        return entityMediafileRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Mediafile not found with ID: {}", id);
                    return new EntityNotFoundException("Mediafile not found");
                });
    }
    @Override
    public void updateMediafile(MediafileEntity mediafile) {
        logger.info("Updating mediafile: {}", mediafile);
        entityMediafileRepository.save(mediafile);
    }

    @Override
    public void deleteMediafile(UUID id) {
        try {
            logger.info("Deleting mediafile with ID: {}", id);
            entityMediafileRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            logger.error("Mediafile not found with ID: {}", id);
            throw new EntityNotFoundException("Mediafile not found");
        }
    }

    @Override
    public List<MediafileEntity> getAllMediafiles() {
        logger.info("Fetching all mediafiles");
        return entityMediafileRepository.findAll();
    }
}