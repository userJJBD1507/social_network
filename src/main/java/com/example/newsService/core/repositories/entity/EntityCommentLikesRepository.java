package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.like.entities.comment.CommentLikes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityCommentLikesRepository extends JpaRepository<CommentLikes, UUID> {

}
