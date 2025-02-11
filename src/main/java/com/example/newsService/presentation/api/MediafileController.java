package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.S3StorageService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/mediafiles")
// public class MediafileController {

//     private final MediafileService mediafileService;

//     @PostMapping
//     public ResponseEntity<MediafileDTO> addMediafile(@RequestPart("file") MultipartFile file,
//                                                      @RequestParam("postId") String postId) {

//         // Генерация URL (заглушка)
//         String generatedUrl = "http://example.com/media/" + file.getOriginalFilename();

//         MediafileDTO mediafileDTO = MediafileDTO.builder()
//                 .type(file.getContentType()
//                         .equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
//                 .url(generatedUrl)
//                 .postId(UUID.fromString(postId))
//                 .build();


//         MediafileDTO savedMediafile = mediafileService.add(mediafileDTO);

//         return new ResponseEntity<>(savedMediafile, HttpStatus.CREATED);
//     }

//     @GetMapping("/get")
//     public ResponseEntity<MediafileDTO> getMediafile(@RequestParam("id") UUID id) {
//         Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
//         return mediafileDTO.map(ResponseEntity::ok)
//                 .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
//     }

//     @PutMapping("/update")
//     public ResponseEntity<MediafileDTO> updateMediafile(@RequestPart("file") MultipartFile file,
//                                                         @RequestParam("postId") String postId,
//                                                         @RequestParam("id") UUID id) {
//         // Генерация нового URL для файла
//         String generatedUrl = "http://example.com/media/" + file.getOriginalFilename();

//         MediafileDTO mediafileDTO = MediafileDTO.builder()
//                 .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
//                 .url(generatedUrl)
//                 .postId(UUID.fromString(postId))
//                 .id(id) // Передаем ID для обновления
//                 .build();

//         try {
//             MediafileDTO updatedMediafile = mediafileService.update(mediafileDTO);
//             return new ResponseEntity<>(updatedMediafile, HttpStatus.OK);
//         } catch (IllegalArgumentException e) {
//             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//         }
//     }

//     @DeleteMapping("/delete")
//     public ResponseEntity<Void> deleteMediafile(@RequestParam("id") UUID id) {
//         try {
//             mediafileService.delete(id);
//             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//         } catch (EntityNotFoundException e) {
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }
// }







// @RestController
// @RequiredArgsConstructor
// @RequestMapping("/mediafiles")
// @Slf4j
// public class MediafileController {

//     private final MediafileService mediafileService;
//     @Autowired
//     private S3StorageServiceImpl s3StorageService;
//     private final JpaMediafileRepository jpaMediafileRepository;
//     @PostMapping("/add")
//     public ResponseEntity<Void> addMediafile(@RequestPart("file") MultipartFile file,
//                                              @RequestParam("postId") String postId) {
//         log.info("Received request to add mediafile for post ID: {}", postId);

//         String generatedUrl = s3StorageService.upload(file);

//         MediafileDTO mediafileDTO = MediafileDTO.builder()
//                 .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
//                 .url(generatedUrl)
//                 .postId(UUID.fromString(postId))
//                 .build();

//         try {
//             mediafileService.add(mediafileDTO);
//             log.info("Mediafile added successfully with URL: {}", generatedUrl);
//             return new ResponseEntity<>(HttpStatus.CREATED);
//         } catch (IllegalArgumentException e) {
//             log.error("Error adding mediafile: {}", e.getMessage());
//             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//         }
//     }

//     @GetMapping("/get")
//     public ResponseEntity<MediafileDTO> getMediafile(@RequestParam("id") UUID id) {
//         log.info("Received request to get mediafile with ID: {}", id);
//         Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
//         return mediafileDTO.map(ResponseEntity::ok)
//                 .orElseGet(() -> {
//                     log.warn("Mediafile not found with ID: {}", id);
//                     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                 });
//     }

//     @PutMapping("/update")
//     public ResponseEntity<Void> updateMediafile(@RequestPart("file") MultipartFile file,
//                                                 @RequestParam("postId") String postId,
//                                                 @RequestParam("id") UUID id) throws Exception {
//         log.info("Received request to update mediafile with ID: {} for post ID: {}", id, postId);

//         MediafileEntity mediafileEntity = jpaMediafileRepository.getMediafile(id);
//         String filenameOfMediaFile = mediafileEntity.getUrl();
//         s3StorageService.update(filenameOfMediaFile, file.getBytes());

//         MediafileDTO mediafileDTO = MediafileDTO.builder()
//                 .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
//                 .url(mediafileEntity.getUrl())
//                 .postId(UUID.fromString(postId))
//                 .id(id)
//                 .build();

//         try {
//             mediafileService.update(id, mediafileDTO);
//             log.info("Mediafile updated successfully with ID: {}", id);
//             return new ResponseEntity<>(HttpStatus.OK);
//         } catch (IllegalArgumentException | EntityNotFoundException e) {
//             log.error("Error updating mediafile: {}", e.getMessage());
//             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
//         }
//     }

//     @DeleteMapping("/delete")
//     public ResponseEntity<Void> deleteMediafile(@RequestParam("id") UUID id) {
//         log.info("Received request to delete mediafile with ID: {}", id);
//         try {
//             mediafileService.delete(id);
//             log.info("Mediafile deleted successfully with ID: {}", id);
//             return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//         } catch (EntityNotFoundException e) {
//             log.warn("Mediafile not found for deletion with ID: {}", id);
//             return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//         }
//     }
// }


@RestController
@RequestMapping("/mediafiles")
@Slf4j
public class MediafileController {
    @Autowired
    private MediafileService mediafileService;
    @Autowired
    private JpaMediafileRepository jpaMediafileRepository;
    @Autowired
    private EntityMediafileRepository entityMediafileRepository;
    
    @Autowired
    private S3StorageServiceImpl s3StorageService;

    @PostMapping("/add")
    public ResponseEntity<Void> addMediafile(@RequestPart("file") MultipartFile file,
                                             @RequestParam("postId") String postId) {
        log.info("Received request to add mediafile for post ID: {}", postId);

        String generatedUrl = s3StorageService.upload(file);

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
                .url(generatedUrl)
                .postId(UUID.fromString(postId))
                .build();

        try {
            mediafileService.add(mediafileDTO);
            log.info("Mediafile added successfully with URL: {}", generatedUrl);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Error adding mediafile: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<MediafileDTO> getMediafile(@RequestParam("id") UUID id) {
        log.info("Received request to get mediafile with ID: {}", id);
        Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
        return mediafileDTO.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Mediafile not found with ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateMediafile(@RequestPart("file") MultipartFile file,
                                                @RequestParam("postId") String postId,
                                                @RequestParam("id") UUID id) throws Exception {
        log.info("Received request to update mediafile with ID: {}", id);

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        boolean isAdmin = isUserAdmin(jwt);

        Optional<MediafileEntity> mediafileOpt = entityMediafileRepository.findById(id);
        if (mediafileOpt.isEmpty()) {
            log.warn("Mediafile not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MediafileEntity mediafileEntity = mediafileOpt.get();

        if (!isAdmin && !mediafileEntity.getCreatedBy().equals(username)) {
            log.warn("User '{}' is not authorized to update mediafile with ID: {}", username, id);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        String filenameOfMediaFile = mediafileEntity.getUrl();
        s3StorageService.update(filenameOfMediaFile, file.getBytes());

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
                .url(mediafileEntity.getUrl())
                .postId(UUID.fromString(postId))
                .id(id)
                .build();

        try {
            mediafileService.update(id, mediafileDTO);
            log.info("Mediafile updated successfully with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            log.error("Error updating mediafile: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMediafile(@RequestParam("id") UUID id) {
        log.info("Received request to delete mediafile with ID: {}", id);

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        boolean isAdmin = isUserAdmin(jwt);

        Optional<MediafileEntity> mediafileOpt = entityMediafileRepository.findById(id);
        if (mediafileOpt.isEmpty()) {
            log.warn("Mediafile not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        MediafileEntity mediafileEntity = mediafileOpt.get();

        if (!isAdmin && !mediafileEntity.getCreatedBy().equals(username)) {
            log.warn("User '{}' is not authorized to delete mediafile with ID: {}", username, id);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        s3StorageService.delete(mediafileEntity.getUrl());

        mediafileService.delete(id);
        log.info("Mediafile deleted successfully with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private boolean isUserAdmin(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("spring_sec_roles");
        return roles != null && roles.contains("ROLE_ADMIN");
    }
}
