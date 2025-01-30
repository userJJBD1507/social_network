package com.example.newsService.core.post.entities;

import com.example.newsService.core.Post;
import com.example.newsService.core.comment.entities.CommentEntity;
import com.example.newsService.core.like.entities.post.PostLikes;
import com.example.newsService.core.mediafile.entities.post.MediafilePost;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_post")
@Getter
@Setter
public class PostEntity extends Post {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy = "post")
    private List<MediafilePost> mediafilePosts;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<CommentEntity> comments;

    @OneToMany(mappedBy = "post", orphanRemoval = true, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PostLikes> likes;

    public PostEntity(UUID id,
                      String title,
                      String content,
                      LocalDateTime createdAt,
                      LocalDateTime updatedAt,
                      Long likeQuantity,
                      Long commentQuantity,
                      boolean isPrivate,
                      String userId) {
        super(id, title, content, createdAt, updatedAt, likeQuantity, commentQuantity, isPrivate, userId);
    }

    public PostEntity() {

    }
}
