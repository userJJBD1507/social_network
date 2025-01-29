package com.example.newsService.core;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.time.LocalDateTime;
import java.util.UUID;

@MappedSuperclass
public class Comment {

    @Id
    @GeneratedValue
    private UUID id;
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
    @Column(name ="user_id")
    private String user_id;

    public Comment(UUID id,
                   String content,
                   LocalDateTime createdAt,
                   LocalDateTime updatedAt,
                   Long likeQuantity,
                   Long commentQuantity,
                   String user_id) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.likeQuantity = likeQuantity;
        this.commentQuantity = commentQuantity;
        this.user_id = user_id;
    }

    public Comment() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLikeQuantity() {
        return likeQuantity;
    }

    public void setLikeQuantity(Long likeQuantity) {
        this.likeQuantity = likeQuantity;
    }

    public Long getCommentQuantity() {
        return commentQuantity;
    }

    public void setCommentQuantity(Long commentQuantity) {
        this.commentQuantity = commentQuantity;
    }

}
