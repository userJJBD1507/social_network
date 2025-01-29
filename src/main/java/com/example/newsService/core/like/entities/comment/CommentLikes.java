package com.example.newsService.core.like.entities.comment;

import com.example.newsService.core.Likes;
import com.example.newsService.core.comment.entities.CommentEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "t_comment_likes")
public class CommentLikes extends Likes {

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public CommentLikes(UUID id, String userId) {
        super(id, userId);
    }

    public CommentLikes() {
    }

}
