package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {


    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(@RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.add(postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<PostDTO> getPost(@RequestParam("id") UUID id) {
        Optional<PostDTO> post = postService.get(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update")
    public ResponseEntity<PostDTO> updatePost(@RequestParam("id") UUID id, @RequestBody PostDTO postDTO) {
        PostDTO updatedPost = postService.update(id, postDTO);
        return ResponseEntity.ok(updatedPost);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePost(@RequestParam("id") UUID id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }




    @PostMapping("/reply")
    public ResponseEntity<PostDTO> replyToPost(@RequestParam("parentPostId") UUID parentPostId,
            @RequestBody PostDTO postDTO) {
        PostDTO createdPost = postService.addPostAsReply(parentPostId, postDTO);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
}