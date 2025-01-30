package com.example.newsService.core;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder(toBuilder = true)
@ToString
public class Post {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    @Column(name = "like_quantity")
    private Long likeQuantity;
    @Column(name = "comment_quantity")
    private Long commentQuantity;
    @Column(name = "is_private")
    private boolean isPrivate;
    @Column(name = "user_id")
    private String userId;

}
