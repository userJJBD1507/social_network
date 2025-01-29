package com.example.newsService.core.repositories;

import com.example.newsService.core.like.entities.post.PostLikes;

import java.util.UUID;

public interface PostLikesRepository {

    void addPostLike(PostLikes postLikes);

    PostLikes getPostLike(UUID id);

    void deletePostLike(UUID id);
}
