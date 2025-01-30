package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.like.entities.post.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityPostLikesRepository extends JpaRepository<PostLikes, UUID> {

}
