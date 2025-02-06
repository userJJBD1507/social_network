package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.services.ReactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
public class ReactionController {

    private final ReactionService reactionService;

    // @PostMapping("/add")
    // public ResponseEntity<Void> addReaction(@RequestBody ReactionDTO reactionDTO) {
    //     reactionService.add(reactionDTO);
    //     return new ResponseEntity<>(HttpStatus.CREATED);
    // }

    // @DeleteMapping("/remove")
    // public ResponseEntity<Void> deleteReaction(@RequestParam("id") UUID id) {
    //     reactionService.delete(id);
    //     return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    // }


    @PostMapping("/add")
    public ResponseEntity<Void> addReaction(@RequestParam("description") String description,
    @RequestParam("file") MultipartFile multipartFile) {
        //логика на сохранения файла и возврата url
        ReactionDTO reactionDTO = new ReactionDTO();
        reactionDTO.setDescription(description);
        reactionDTO.setUrl("mock url");
        reactionService.add(reactionDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteReaction(@RequestParam("id") UUID id) {
        reactionService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/reply")
    public ResponseEntity<Void> addReactionReply(@RequestParam("reaction_description") String description,
        @RequestParam("postId") UUID postId) {
        reactionService.replyToPost(description, postId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}