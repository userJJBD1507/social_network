package com.example.newsService.infra.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequiredArgsConstructor
@Slf4j
public class JpaMediafileRepository implements MediafileRepository {

    private final EntityMediafileRepository entityMediafileRepository;

    @Override
    public void addMediafile(MediafileEntity mediafile) {
        log.info("Adding mediafile: {}", mediafile);
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
        log.info("Fetching mediafile with ID: {}", id);
        return entityMediafileRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Mediafile not found with ID: {}", id);
                    return new EntityNotFoundException("Mediafile not found");
                });
    }
    @Override
    public void updateMediafile(MediafileEntity mediafile) {
        log.info("Updating mediafile: {}", mediafile);
        entityMediafileRepository.save(mediafile);
    }

    @Override
    public void deleteMediafile(UUID id) {
        try {
            log.info("Deleting mediafile with ID: {}", id);
            entityMediafileRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            log.error("Mediafile not found with ID: {}", id);
            throw new EntityNotFoundException("Mediafile not found");
        }
    }

    @Override
    public List<MediafileEntity> getAllMediafiles() {
        log.info("Fetching all mediafiles");
        return entityMediafileRepository.findAll();
    }
}