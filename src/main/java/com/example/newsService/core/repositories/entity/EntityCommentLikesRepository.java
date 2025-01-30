package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.like.entities.comment.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityCommentLikesRepository extends JpaRepository<CommentLikes, UUID> {

}
