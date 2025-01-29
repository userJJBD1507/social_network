package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.comment.entities.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityCommentRepository extends JpaRepository<CommentEntity, UUID> {

}


