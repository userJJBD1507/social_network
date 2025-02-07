package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.utils.MediaFileType;
import com.example.newsService.infra.repositories.JpaMediafileRepository;
import com.example.newsService.infra.services.S3StorageServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

@RestController
@RequiredArgsConstructor
@RequestMapping("/mediafiles")
@Slf4j
@Tag(name = "Управление медиафайлами", description = "API для работы с медиафайлами (изображениями и видео)")
public class MediafileController {

    private final MediafileService mediafileService;

    private final S3StorageServiceImpl s3StorageService;

    private final JpaMediafileRepository jpaMediafileRepository;

    @Operation(
            summary = "Добавить медиафайл",
            description = "Загрузка нового медиафайла (изображение/видео) и привязка к посту"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Медиафайл успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "500", description = "Ошибка при загрузке файла")
    })
    @PostMapping("/add")
    public ResponseEntity<Void> addMediafile(
            @Parameter(description = "Файл для загрузки (JPEG/видео)", required = true)
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "ID поста для привязки", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
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

    @Operation(
            summary = "Получить медиафайл",
            description = "Получение информации о медиафайле по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Информация о медиафайле"),
            @ApiResponse(responseCode = "404", description = "Медиафайл не найден")
    })
    @GetMapping("/get")
    public ResponseEntity<MediafileDTO> getMediafile(
            @Parameter(description = "ID медиафайла", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id) {
        log.info("Received request to get mediafile with ID: {}", id);
        Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
        return mediafileDTO.map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Mediafile not found with ID: {}", id);
                    return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                });
    }


    @Operation(
            summary = "Обновить медиафайл",
            description = "Обновление содержимого медиафайла и его метаданных"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Медиафайл успешно обновлен"),
            @ApiResponse(responseCode = "400", description = "Некорректные входные данные"),
            @ApiResponse(responseCode = "404", description = "Медиафайл не найден")
    })
    @PutMapping("/update")
    public ResponseEntity<Void> updateMediafile(
            @Parameter(description = "Новый файл для замены", required = true)
            @RequestPart("file") MultipartFile file,
            @Parameter(description = "ID поста", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("postId") String postId,
            @Parameter(description = "ID медиафайла", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id) throws Exception {
        log.info("Received request to update mediafile with ID: {} for post ID: {}", id, postId);

        MediafileEntity mediafileEntity = jpaMediafileRepository.getMediafile(id);
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

    @Operation(
            summary = "Удалить медиафайл",
            description = "Удаление медиафайла по его ID"
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Медиафайл успешно удален"),
            @ApiResponse(responseCode = "404", description = "Медиафайл не найден")
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMediafile(
            @Parameter(description = "ID медиафайла", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @RequestParam("id") UUID id) {
        log.info("Received request to delete mediafile with ID: {}", id);
        try {
            mediafileService.delete(id);
            log.info("Mediafile deleted successfully with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            log.warn("Mediafile not found for deletion with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
