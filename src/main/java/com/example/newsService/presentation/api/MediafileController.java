package com.example.newsService.presentation.api;

import com.example.newsService.app.DTO.MediafileDTO;
import com.example.newsService.app.services.MediafileService;
import com.example.newsService.core.utils.MediaFileType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/mediafiles")
public class MediafileController {

    private final MediafileService mediafileService;

    @PostMapping
    public ResponseEntity<MediafileDTO> addMediafile(@RequestPart("file") MultipartFile file,
                                                     @RequestParam("postId") String postId) {

        // Генерация URL (заглушка)
        String generatedUrl = "http://example.com/media/" + file.getOriginalFilename();

        MediafileDTO mediafileDTO = MediafileDTO.builder()
                .type(file.getContentType()
                        .equals("image/jpeg") ? MediaFileType.PHOTO : MediaFileType.VIDEO)
                .url(generatedUrl)
                .postId(UUID.fromString(postId))
                .build();


        MediafileDTO savedMediafile = mediafileService.add(mediafileDTO);

        return new ResponseEntity<>(savedMediafile, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediafileDTO> getMediafile(@PathVariable UUID id) {
        Optional<MediafileDTO> mediafileDTO = mediafileService.get(id);
        return mediafileDTO.map(ResponseEntity::ok)
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}