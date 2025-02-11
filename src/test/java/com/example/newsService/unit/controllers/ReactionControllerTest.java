package com.example.newsService.unit.controllers;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.multipart.MultipartFile;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.app.services.ReactionService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;
import com.example.newsService.presentation.api.MediafileController;
import com.example.newsService.presentation.api.ReactionController;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class ReactionControllerTest {

    @Mock
    private ReactionService reactionService;

    @Mock
    private S3StorageServiceImpl s3StorageService;

    @InjectMocks
    private ReactionController reactionController;

    @Captor
    ArgumentCaptor<ReactionDTO> reactionDTOCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void addReaction_ShouldReturnCreatedStatus_WhenReactionIsAddedSuccessfully() throws Exception {
        // Arrange
        String description = "Positive reaction";
        MultipartFile file = mock(MultipartFile.class);
        String fileUrl = "https://mock-url.com";
        when(s3StorageService.uploadFileWithExistingFilename(file, description)).thenReturn(fileUrl);

        // Act
        ResponseEntity<Void> response = reactionController.addReaction(description, file);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void addReaction_ShouldReturnInternalServerError_WhenErrorOccursDuringFileUpload() throws Exception {
        // Arrange
        String description = "Positive reaction";
        MultipartFile file = mock(MultipartFile.class);
        when(s3StorageService.uploadFileWithExistingFilename(file, description)).thenThrow(new RuntimeException("File upload failed"));

        // Act
        ResponseEntity<Void> response = reactionController.addReaction(description, file);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void deleteReaction_ShouldReturnNoContent_WhenReactionDeletedSuccessfully() {
        // Arrange
        UUID reactionId = UUID.randomUUID();
        doNothing().when(reactionService).delete(reactionId);

        // Act
        ResponseEntity<Void> response = reactionController.deleteReaction(reactionId);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deleteReaction_ShouldReturnInternalServerError_WhenErrorOccursDuringDeletion() {
        // Arrange
        UUID reactionId = UUID.randomUUID();
        doThrow(new RuntimeException("Error deleting reaction")).when(reactionService).delete(reactionId);

        // Act
        ResponseEntity<Void> response = reactionController.deleteReaction(reactionId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    @Test
    void addReactionReply_ShouldReturnCreatedStatus_WhenReplyIsAddedSuccessfully() {
        // Arrange
        String description = "Reply to post";
        UUID postId = UUID.randomUUID();
        doNothing().when(reactionService).replyToPost(description, postId);

        // Act
        ResponseEntity<Void> response = reactionController.addReactionReply(description, postId);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    void addReactionReply_ShouldReturnInternalServerError_WhenErrorOccursDuringReply() {
        // Arrange
        String description = "Reply to post";
        UUID postId = UUID.randomUUID();
        doThrow(new RuntimeException("Error replying to post")).when(reactionService).replyToPost(description, postId);

        // Act
        ResponseEntity<Void> response = reactionController.addReactionReply(description, postId);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
