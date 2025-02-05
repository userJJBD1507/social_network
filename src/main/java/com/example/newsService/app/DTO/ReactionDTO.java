package com.example.newsService.app.DTO;

import com.example.newsService.core.utils.ReactionType;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReactionDTO {
    private ReactionType reaction;
}