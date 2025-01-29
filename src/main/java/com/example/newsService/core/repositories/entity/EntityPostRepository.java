package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.post.entities.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EntityPostRepository extends JpaRepository<PostEntity, UUID> {

}
