package com.example.newsService.core.like.entities.post;

import com.example.newsService.core.Likes;
import com.example.newsService.core.post.entities.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "t_post_likes")
public class PostLikes extends Likes {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public PostLikes(UUID id, String userId) {
        super(id, userId);
    }

    public PostLikes() {
    }
}
