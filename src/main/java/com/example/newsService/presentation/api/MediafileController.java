package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.utils.MediaFileType;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
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
public class MediafileController {

    private final MediafileService mediafileService;

    @PostMapping("/add")
    public ResponseEntity<Void> addMediafile(@RequestPart("file") MultipartFile file,
                                             @RequestParam("postId") String postId) {

        // Генерация URL (заглушка)
        String generatedUrl = "http://example.com/media/" + file.getOriginalFilename();

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
                .url(generatedUrl)
                .postId(UUID.fromString(postId))
                .build();

        try {
            mediafileService.add(mediafileDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/get")
    public ResponseEntity<MediafileDTO> getMediafile(@RequestParam("id") UUID id) {
        Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
        return mediafileDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updateMediafile(@RequestPart("file") MultipartFile file,
                                                @RequestParam("postId") String postId,
                                                @RequestParam("id") UUID id) {
        // Генерация нового URL для файла
        String generatedUrl = "http://example.com/media/" + file.getOriginalFilename();

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(file.getContentType().equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
                .url(generatedUrl)
                .postId(UUID.fromString(postId))
                .id(id)
                .build();

        try {
            mediafileService.update(id, mediafileDTO);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteMediafile(@RequestParam("id") UUID id) {
        try {
            mediafileService.delete(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
