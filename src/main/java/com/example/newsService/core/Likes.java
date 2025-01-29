package com.example.newsService.core;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import java.util.UUID;

@MappedSuperclass
public class Likes {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "user_id")
    private String userId;

    public Likes(UUID id, String userId) {
        this.id = id;
        this.userId = userId;
    }

    public Likes() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Likes{" +
                "id=" + id +
                ", userId=" + userId +
                '}';
    }
}
