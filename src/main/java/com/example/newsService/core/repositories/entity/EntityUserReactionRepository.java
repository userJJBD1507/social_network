package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.reaction.entities.UserReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityUserReactionRepository extends JpaRepository<UserReactionEntity, UUID> {
}
