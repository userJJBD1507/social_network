package com.example.newsService.app.DTO;

import com.example.newsService.core.utils.MediaFileType;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MediafileDTO {
    private UUID id;
    private MediaFileType type;
    private String url;
    private UUID postId;
}