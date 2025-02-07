package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.PostDTO;
import com.example.newsService.app.services.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Управление постами", description = "API для работы с постами и их реплаями")
public class PostController {

    private final PostService postService;

    @Operation(
            summary = "Создать пост",
            description = "Создание нового поста с указанными данными"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Пост успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании поста")
    })
    @PostMapping("/create")
    public ResponseEntity<PostDTO> createPost(
            @Parameter(description = "Данные для создания поста", required = true)
            @RequestBody PostDTO postDTO) {

        log.info("Received request to create post: {}", postDTO);
        PostDTO createdPost = postService.add(postDTO);
        log.info("Post created successfully: {}", createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Получить пост",
            description = "Получение информации о посте по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о посте"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @GetMapping("/get")
    public ResponseEntity<PostDTO> getPost(
            @Parameter(description = "ID поста", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id) {

        log.info("Received request to get post with ID: {}", id);
        Optional<PostDTO> post = postService.get(id);
        return post.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Post not found with ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }

    @Operation(
            summary = "Обновить пост",
            description = "Обновление данных поста по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Пост успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @PutMapping("/update")
    public ResponseEntity<PostDTO> updatePost(
            @Parameter(description = "ID поста", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id,

            @Parameter(description = "Новые данные для поста", required = true)
            @RequestBody PostDTO postDTO) {

        log.info("Received request to update post with ID: {} and data: {}", id, postDTO);
        PostDTO updatedPost = postService.update(id, postDTO);
        log.info("Post updated successfully: {}", updatedPost);
        return ResponseEntity.ok(updatedPost);
    }

    @Operation(
            summary = "Удалить пост",
            description = "Удаление поста по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Пост успешно удален"),
            @ApiResponse(responseCode = "404", description = "Пост не найден")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deletePost(
            @Parameter(description = "ID поста", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id) {

        log.info("Received request to delete post with ID: {}", id);
        postService.delete(id);
        log.info("Post deleted successfully with ID: {}", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Operation(
            summary = "Ответить на пост",
            description = "Создание ответа (реплая) на существующий пост"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Ответ успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Родительский пост не найден"),
            @ApiResponse(responseCode = "500", description = "Ошибка при создании ответа")
    })
    @PostMapping("/reply")
    public ResponseEntity<PostDTO> replyToPost(
            @Parameter(description = "ID родительского поста", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("parentPostId") UUID parentPostId,

            @Parameter(description = "Данные для создания ответа", required = true)
            @RequestBody PostDTO postDTO) {

        log.info("Received request to reply to post with ID: {} and data: {}", parentPostId, postDTO);
        PostDTO createdPost = postService.addPostAsReply(parentPostId, postDTO);
        log.info("Reply created successfully: {}", createdPost);
        return new ResponseEntity<>(createdPost, HttpStatus.CREATED);
    }
}
