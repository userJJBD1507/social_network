package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.reaction.entities.ReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EntityReactionRepository extends JpaRepository<ReactionEntity, UUID> {
}
