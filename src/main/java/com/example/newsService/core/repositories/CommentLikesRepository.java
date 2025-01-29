package com.example.newsService.core.repositories;

import com.example.newsService.core.like.entities.comment.CommentLikes;

import java.util.UUID;

public interface CommentLikesRepository {

    void addCommentLike(CommentLikes commentLikes);

    CommentLikes getCommentLike(UUID id);

    void deleteCommentLike(UUID id);

}
