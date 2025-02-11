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
import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.app.services.PostService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.entity.EntityMediafileRepository;
import com.example.newsService.core.repositories.entity.EntityPostRepository;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;
import com.example.newsService.presentation.api.MediafileController;
import com.example.newsService.presentation.api.PostController;

import static org.junit.jupiter.api.Assertions.*;

import org.mockito.Mockito;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PostControllerTest {

    @Mock
    private PostService postService;

    @Mock
    private EntityPostRepository postRepository;

    @InjectMocks
    private PostController postController;

    private static final String USERNAME = "testUser";
    
    @Captor
    ArgumentCaptor<PostDTO> postDTOCaptor;

    @BeforeEach
    void setUpSecurityContext() {
        Jwt jwt = Mockito.mock(Jwt.class);
        when(jwt.getClaimAsString("preferred_username")).thenReturn(USERNAME);
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createPost_ShouldReturnCreatedStatus_WhenPostIsCreated() {
        // Arrange
        PostDTO postDTO = PostDTO.builder()
                .title("New Post")
                .content("This is a test post.")
                .build();

        when(postService.add(postDTO)).thenReturn(postDTO);

        // Act
        ResponseEntity<PostDTO> response = postController.createPost(postDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
    }

    @Test
    void updatePost_ShouldReturnOk_WhenUpdatedSuccessfully() {
        // Arrange
        UUID postId = UUID.randomUUID();
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated Post");
        postDTO.setContent("Updated content.");
        
        PostEntity postEntity = mock(PostEntity.class);
        when(postEntity.getCreatedBy()).thenReturn(USERNAME);
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postService.update(postId, postDTO)).thenReturn(postDTO);

        // Act
        ResponseEntity<PostDTO> response = postController.updatePost(postId, postDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
    }

    @Test
    void updatePost_ShouldReturnForbidden_WhenNotAuthorized() {
        // Arrange
        UUID postId = UUID.randomUUID();
        PostDTO postDTO = new PostDTO();
        postDTO.setTitle("Updated Post");
        postDTO.setContent("Updated content.");
        
        PostEntity postEntity = mock(PostEntity.class);
        when(postEntity.getCreatedBy()).thenReturn("otherUser");
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Act
        ResponseEntity<PostDTO> response = postController.updatePost(postId, postDTO);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void updatePost_ShouldReturnNotFound_WhenPostNotFound() {
        // Arrange
        UUID postId = UUID.randomUUID();
        PostDTO postDTO = new PostDTO();
        
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<PostDTO> response = postController.updatePost(postId, postDTO);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deletePost_ShouldReturnNoContent_WhenDeletedSuccessfully() {
        // Arrange
        UUID postId = UUID.randomUUID();
        PostEntity postEntity = mock(PostEntity.class);
        when(postEntity.getCreatedBy()).thenReturn(USERNAME);
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Act
        ResponseEntity<Void> response = postController.deletePost(postId);

        // Assert
        verify(postService).delete(postId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void deletePost_ShouldReturnForbidden_WhenNotAuthorized() {
        // Arrange
        UUID postId = UUID.randomUUID();
        PostEntity postEntity = mock(PostEntity.class);
        when(postEntity.getCreatedBy()).thenReturn("otherUser");
        when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        // Act
        ResponseEntity<Void> response = postController.deletePost(postId);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void deletePost_ShouldReturnNotFound_WhenPostNotFound() {
        // Arrange
        UUID postId = UUID.randomUUID();
        when(postRepository.findById(postId)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Void> response = postController.deletePost(postId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void replyToPost_ShouldReturnCreatedStatus_WhenPostIsRepliedTo() {
        // Arrange
        UUID parentPostId = UUID.randomUUID();
        PostDTO postDTO = PostDTO.builder()
                .title("Reply Post")
                .content("This is a reply to the post.")
                .build();

        when(postService.addPostAsReply(parentPostId, postDTO)).thenReturn(postDTO);

        // Act
        ResponseEntity<PostDTO> response = postController.replyToPost(parentPostId, postDTO);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postDTO, response.getBody());
    }
}
