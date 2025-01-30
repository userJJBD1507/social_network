package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.post.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityPostRepository extends JpaRepository<PostEntity, UUID> {

}
