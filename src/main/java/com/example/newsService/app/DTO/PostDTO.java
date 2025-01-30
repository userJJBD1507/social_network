package com.example.newsService.app.DTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class PostDTO {
    private UUID id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeQuantity;
    private Long commentQuantity;
    private boolean isPrivate;
    private String userId;
    private List<MediafileDTO> mediafiles;
}
