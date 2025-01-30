package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.comment.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityCommentRepository extends JpaRepository<CommentEntity, UUID> {

}


