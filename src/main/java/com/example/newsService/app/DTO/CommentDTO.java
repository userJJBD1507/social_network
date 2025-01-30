package com.example.newsService.app.DTO;


import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
@Getter
@Setter
public class CommentDTO {
    private UUID id;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likeQuantity;
    private Long commentQuantity;
    private String user_id;
    private UUID postId;
    private List<MediafileDTO> mediafiles;
}