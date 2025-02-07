package com.example.newsService.app.DTO;

import org.springframework.web.multipart.MultipartFile;

import com.example.newsService.core.utils.ReactionType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDTO {
    private String description;
    private String url;
}