package com.example.newsService.app.DTO;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostDTO {
    private UUID id;
    private String title;
    private String content;
    private Long likeQuantity;
    private Long commentQuantity;
    private boolean isPrivate;
    private UUID userId;
    private Date createdAt;
    private Date updatedAt;
    private List<UUID> commentIds;
    private UUID parentPostId;
    private List<UUID> userReactionIds;
    private List<UUID> mediafileIds;
    public PostDTO(String title, String content, boolean isPrivate) {
        this.title = title;
        this.content = content;
        this.isPrivate = isPrivate;
    }

}