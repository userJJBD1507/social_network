package com.example.newsService.core;

import com.example.newsService.core.utils.MediaFileType;
import jakarta.persistence.*;

import java.util.UUID;

@MappedSuperclass
public class Mediafile {

    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "url")
    private String url;
    @Column(name = "user_id")
    private String userId;
    @Enumerated(EnumType.STRING)
    private MediaFileType type;

    public Mediafile(UUID id, String url, String userId, MediaFileType type) {
        this.id = id;
        this.url = url;
        this.userId = userId;
        this.type = type;
    }

    public Mediafile() {}

    public UUID getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public MediaFileType getType() {
        return type;
    }

    public void setType(MediaFileType type) {
        this.type = type;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Mediafile{" +
                "id=" + id +
                ", url='" + url + '\'' +
                '}';
    }
}
