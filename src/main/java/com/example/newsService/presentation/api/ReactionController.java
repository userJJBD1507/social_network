package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.services.ReactionService;
<<<<<<< HEAD
import com.example.newsService.core.S3StorageService;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
=======
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
@Slf4j
public class ReactionController {

    private final ReactionService reactionService;
<<<<<<< HEAD
    @Autowired
    private S3StorageServiceImpl s3StorageService;
=======

>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732
    @PostMapping("/add")
    public ResponseEntity<Void> addReaction(@RequestParam("description") String description,
                                            @RequestParam("file") MultipartFile multipartFile) {
        log.info("Received request to add reaction with description: {}", description);

        try {

<<<<<<< HEAD
            String fileUrl = s3StorageService.uploadFileWithExistingFilename(multipartFile, description);
=======
            String fileUrl = "mock-url"; // Метод сохранения файла
>>>>>>> a6b5e9255c615854e7d1d1469838473ef4a9d732

            ReactionDTO reactionDTO = new ReactionDTO();
            reactionDTO.setDescription(description);
            reactionDTO.setUrl(fileUrl);
            reactionService.add(reactionDTO);

            log.info("Reaction added successfully with URL: {}", fileUrl);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while adding reaction: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteReaction(@RequestParam("id") UUID id) {
        log.info("Received request to delete reaction with ID: {}", id);

        try {
            reactionService.delete(id);
            log.info("Reaction deleted successfully with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Error while deleting reaction with ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/reply")
    public ResponseEntity<Void> addReactionReply(@RequestParam("reaction_description") String description,
                                                 @RequestParam("postId") UUID postId) {
        log.info("Received request to reply to post with ID: {} and description: {}", postId, description);

        try {
            reactionService.replyToPost(description, postId);
            log.info("Reply added successfully to post ID: {}", postId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error while replying to post with ID {}: {}", postId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
