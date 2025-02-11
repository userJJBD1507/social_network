package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.services.PostService;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.repositories.entity.EntityPostRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// @RestController
// @RequestMapping("/post")
// @RequiredArgsConstructor
// @Slf4j
// public class PostController {

//     private final PostService postService;

//     @PostMapping("/create")
//     public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
//         log.info("Received request to create post: {}", postDTO);
//         PostDTO createdPost = postService.add(postDTO);
//         log.info("Post created successfully: {}", createdPost);
//         return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
//     }

//     @GetMapping("/get")
//     public ResponseEntity<PostDTO> getPost(@RequestParam("id") UUID id) {
//         log.info("Received request to get post with ID: {}", id);
//         Optional<PostDTO> post = postService.get(id);
//         return post.map(ResponseEntity::ok)
//                 .orElseGet(() -> {
//                     log.warn("Post not found with ID: {}", id);
//                     return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//                 });
//     }

//     @PutMapping("/update")
//     public ResponseEntity<PostDTO> updatePost(@RequestParam("id") UUID id, @RequestBody PostDTO postDTO) {
//         log.info("Received request to update post with ID: {} and data: {}", id, postDTO);
//         PostDTO updatedPost = postService.update(id, postDTO);
//         log.info("Post updated successfully: {}", updatedPost);
//         return ResponseEntity.ok(updatedPost);
//     }

//     @DeleteMapping("/delete")
//     public ResponseEntity<Void> deletePost(@RequestParam("id") UUID id) {
//         log.info("Received request to delete post with ID: {}", id);
//         postService.delete(id);
//         log.info("Post deleted successfully with ID: {}", id);
//         return new ResponseEntity<>(HttpStatus.NO_CONTENT);
//     }

//     @PostMapping("/reply")
//     public ResponseEntity<PostDTO> replyToPost(@RequestParam("parentPostId") UUID parentPostId,
//                                                @RequestBody PostDTO postDTO) {
//         log.info("Received request to reply to post with ID: {} and data: {}", parentPostId, postDTO);
//         PostDTO createdPost = postService.addPostAsReply(parentPostId, postDTO);
//         log.info("Reply created successfully: {}", createdPost);
//         return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
//     }
// }



@RestController
@RequestMapping("/post")
@Slf4j
public class PostController {
    @Autowired
    private PostService postService;
    @Autowired
    private EntityPostRepository postRepository;

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        log.info("Received request to create post: {}", postDTO);
        PostDTO createdPost = postService.add(postDTO);
        log.info("Post created successfully: {}", createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<PostDTO> getPost(@RequestParam("id") UUID id) {
        log.info("Received request to get post with ID: {}", id);
        Optional<PostDTO> post = postService.get(id);
        Long commentQuantity = (long) post.get().getCommentIds().size();
        Long likeQuantity = (long) post.get().getUserReactionIds().size();
        post.get().setCommentQuantity(commentQuantity);
        post.get().setLikeQuantity(likeQuantity);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Post not found with ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @PutMapping("/update")
    public ResponseEntity<PostDTO> updatePost(@RequestParam("id") UUID id, 
                                              @RequestBody PostDTO postDTO) {
        log.info("Received request to update post with ID: {}", id);

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        boolean isAdmin = isUserAdmin(jwt);

        Optional<PostEntity> postEntityOpt = postRepository.findById(id);
        if (postEntityOpt.isEmpty()) {
            log.warn("Post not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        PostEntity postEntity = postEntityOpt.get();
        if (!isAdmin && !postEntity.getCreatedBy().equals(username)) {
            log.warn("User '{}' is not authorized to update post with ID: {}", username, id);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        PostDTO updatedPost = postService.update(id, postDTO);
        log.info("Post updated successfully: {}", updatedPost);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestParam("id") UUID id) {
        log.info("Received request to delete post with ID: {}", id);
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = jwt.getClaimAsString("preferred_username");
        boolean isAdmin = isUserAdmin(jwt);
        Optional<PostEntity> postEntityOpt = postRepository.findById(id);
        if (postEntityOpt.isEmpty()) {
            log.warn("Post not found with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        PostEntity postEntity = postEntityOpt.get();
        if (!isAdmin && !postEntity.getCreatedBy().equals(username)) {
            log.warn("User '{}' is not authorized to delete post with ID: {}", username, id);
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        postService.delete(id);
        log.info("Post deleted successfully with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/reply")
    public ResponseEntity<PostDTO> replyToPost(@RequestParam("parentPostId") UUID parentPostId,
                                               @RequestBody PostDTO postDTO) {
        log.info("Received request to reply to post with ID: {} and data: {}", parentPostId, postDTO);
        PostDTO createdPost = postService.addPostAsReply(parentPostId, postDTO);
        log.info("Reply created successfully: {}", createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    private boolean isUserAdmin(Jwt jwt) {
        List<String> roles = jwt.getClaimAsStringList("spring_sec_roles");
        return roles != null && roles.contains("ROLE_ADMIN");
    }
}
