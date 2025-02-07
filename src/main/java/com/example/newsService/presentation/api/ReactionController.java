package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.ReactionDTO;
import com.example.newsService.app.services.ReactionService;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;


@RestController
@RequestMapping("/reaction")
@RequiredArgsConstructor
@Slf4j
public class ReactionController {

    private final ReactionService reactionService;
    private final S3StorageServiceImpl s3StorageService;

    @Operation(
            summary = "Добавить новую реакцию",
            description = "Добавляет новую реакцию с описанием и опциональным файлом."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Реакция успешно добавлена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/add")
    public ResponseEntity<Void> addReaction(
            @Parameter(description = "Описание реакции") @RequestParam("description") String description,
            @Parameter(description = "Файл, связанный с реакцией") @RequestParam("file") MultipartFile multipartFile) {

        log.info("Получен запрос на добавление реакции с описанием: {}", description);

        try {
            String fileUrl = s3StorageService.uploadFileWithExistingFilename(multipartFile, description);

            ReactionDTO reactionDTO = new ReactionDTO();
            reactionDTO.setDescription(description);
            reactionDTO.setUrl(fileUrl);
            reactionService.add(reactionDTO);

            log.info("Реакция успешно добавлена с URL: {}", fileUrl);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Ошибка при добавлении реакции: {}", e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Удалить реакцию",
            description = "Удаляет реакцию по её идентификатору (ID)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Реакция успешно удалена"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @DeleteMapping("/remove")
    public ResponseEntity<Void> deleteReaction(
            @Parameter(description = "Идентификатор реакции для удаления") @RequestParam("id") UUID id) {

        log.info("Получен запрос на удаление реакции с ID: {}", id);

        try {
            reactionService.delete(id);
            log.info("Реакция успешно удалена с ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            log.error("Ошибка при удалении реакции с ID {}: {}", id, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Добавить ответ на пост",
            description = "Добавляет ответ на конкретный пост."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ответ успешно добавлен"),
            @ApiResponse(responseCode = "500", description = "Внутренняя ошибка сервера")
    })
    @PostMapping("/reply")
    public ResponseEntity<Void> addReactionReply(
            @Parameter(description = "Описание ответа") @RequestParam("reaction_description") String description,
            @Parameter(description = "Идентификатор поста для ответа") @RequestParam("postId") UUID postId) {

        log.info("Получен запрос на ответ к посту с ID: {} и описанием: {}", postId, description);

        try {
            reactionService.replyToPost(description, postId);
            log.info("Ответ успешно добавлен к посту с ID: {}", postId);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Ошибка при добавлении ответа к посту с ID {}: {}", postId, e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
