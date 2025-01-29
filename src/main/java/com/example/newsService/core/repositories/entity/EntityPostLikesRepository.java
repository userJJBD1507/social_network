package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.like.entities.post.PostLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityPostLikesRepository extends JpaRepository<PostLikes, UUID> {

}
