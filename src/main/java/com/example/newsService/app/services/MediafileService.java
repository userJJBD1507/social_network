package com.example.newsService.app.services;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.mappers.MediafileMapper;
import com.example.newsService.core.MediafileCrud;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.infra.repositories.JpaMediafileRepository;

import jakarta.persistence.EntityNotFoundException;

// @Service 
// public class MediafileService implements MediafileCrud<MediafileEntity, UUID> {

//     private static final Logger logger = LoggerFactory.getLogger(MediafileService.class);

//     @Autowired
//     private JpaMediafileRepository jpaMediafileRepository;

//     @Override
//     public MediafileEntity add(MediafileEntity entity) {
//         if (entity == null) {
//             logger.error("MediafileEntity is null");
//             throw new IllegalArgumentException("Mediafile cannot be null");
//         }
//         logger.info("Adding mediafile: {}", entity);
//         jpaMediafileRepository.addMediafile(entity);
//         return entity;
//     }

//     @Override
//     public void delete(UUID id) {
//         if (id == null) {
//             logger.error("ID is null");
//             throw new IllegalArgumentException("ID cannot be null");
//         }
//         try {
//             logger.info("Deleting mediafile with ID: {}", id);
//             jpaMediafileRepository.deleteMediafile(id);
//         } catch (EntityNotFoundException e) {
//             logger.error("Mediafile not found with ID: {}", id);
//             throw e;
//         }
//     }

//     @Override
//     public Optional<MediafileEntity> get(UUID id) {
//         if (id == null) {
//             logger.error("ID is null");
//             throw new IllegalArgumentException("ID cannot be null");
//         }
//         try {
//             logger.info("Fetching mediafile with ID: {}", id);
//             MediafileEntity mediafile = jpaMediafileRepository.getPost(id);
//             return Optional.of(mediafile);
//         } catch (EntityNotFoundException e) {
//             logger.error("Mediafile not found with ID: {}", id);
//             return Optional.empty();
//         }
//     }

//     @Override
//     public void update(MediafileEntity entity) {
//         if (entity == null) {
//             logger.error("MediafileEntity is null");
//             throw new IllegalArgumentException("Mediafile cannot be null");
//         }
//         if (entity.getId() == null) {
//             logger.error("Mediafile ID is null during update");
//             throw new IllegalArgumentException("Mediafile ID cannot be null");
//         }
//         try {
//             logger.info("Updating mediafile: {}", entity);
//             jpaMediafileRepository.updateMediafile(entity);
//         } catch (EntityNotFoundException e) {
//             logger.error("Mediafile not found for update: {}", entity);
//             throw e;
//         }
//     }
// }



@Service
@RequiredArgsConstructor
@Slf4j
public class MediafileService implements MediafileCrud<MediafileDTO, UUID> {



    private final JpaMediafileRepository jpaMediafileRepository;

    private final MediafileMapper mediafileMapper;

    @Override
    public void add(MediafileDTO dto) {
        if (dto == null) {
            log.error("MediafileDTO is null");
            throw new IllegalArgumentException("Mediafile cannot be null");
        }
        log.info("Adding mediafile: {}", dto);

        MediafileEntity entity = mediafileMapper.toEntity(dto);
        jpaMediafileRepository.addMediafile(entity);
    }

    @Override
    public void delete(UUID id) {
        if (id == null) {
            log.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            log.info("Deleting mediafile with ID: {}", id);
            jpaMediafileRepository.deleteMediafile(id);
        } catch (EntityNotFoundException e) {
            log.error("Mediafile not found with ID: {}", id);
            throw e;
        }
    }

    @Override
    public Optional<MediafileDTO> get(UUID id) {
        if (id == null) {
            log.error("ID is null");
            throw new IllegalArgumentException("ID cannot be null");
        }
        try {
            log.info("Fetching mediafile with ID: {}", id);
            // MediafileEntity mediafile = jpaMediafileRepository.getPost(id);
            MediafileEntity mediafile = jpaMediafileRepository.getMediafile(id);
            return Optional.of(mediafileMapper.toDTO(mediafile));
        } catch (EntityNotFoundException e) {
            log.error("Mediafile not found with ID: {}", id);
            return Optional.empty();
        }
    }

    @Override
    public void update(UUID id, MediafileDTO dto) {
        if (dto == null) {
            log.error("MediafileDTO is null");
            throw new IllegalArgumentException("Mediafile cannot be null");
        }
        if (dto.getType() == null) {
            log.error("Mediafile type is null during update");
            throw new IllegalArgumentException("Mediafile type cannot be null");
        }
        try {
            log.info("Updating mediafile: {}", dto);
            MediafileEntity entity = mediafileMapper.toEntity(dto);
            entity.setId(id);
            MediafileEntity existingEntity = jpaMediafileRepository.getMediafile(id);      
            if (existingEntity != null) {
                entity.setCreatedAt(existingEntity.getCreatedAt());
                entity.setUpdatedAt(new Date());
            }
            jpaMediafileRepository.updateMediafile(entity);
        } catch (EntityNotFoundException e) {
            log.error("Mediafile not found for update: {}", dto);
            throw e;
        }
    }
}