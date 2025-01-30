package com.example.newsService.core.comment.entities;

import com.example.newsService.core.Comment;
import com.example.newsService.core.like.entities.comment.CommentLikes;
import com.example.newsService.core.mediafile.entities.comment.MediafileComment;
import com.example.newsService.core.post.entities.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "t_comment")
@Getter
@Setter
public class CommentEntity extends Comment {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "comment")
    private List<MediafileComment> mediafileComments;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CommentLikes> likes;

    public CommentEntity(UUID id,
                         String content,
                         LocalDateTime createdAt,
                         LocalDateTime updatedAt,
                         Long likeQuantity,
                         String user_id) {
        super(id, content, createdAt, updatedAt, likeQuantity, user_id);
    }

    public CommentEntity() {
    }
}
