package com.example.newsService.app.DTO;

import com.example.newsService.core.utils.MediaFileType;
import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
public class MediafileDTO {
    private UUID id;
    private String url;
    private String userId;
    private MediaFileType type;
}
