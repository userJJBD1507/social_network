package com.example.newsService.core.repositories;

import com.example.newsService.core.post.entities.PostEntity;

import java.util.List;
import java.util.UUID;

public interface PostRepository {

    void addPost(PostEntity post);

    PostEntity getPost(UUID id);

    void updatePost(PostEntity post);

    void deletePost(UUID id);

    List<PostEntity> getAllPosts();
}
