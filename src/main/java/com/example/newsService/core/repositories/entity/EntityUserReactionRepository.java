package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.reaction.entities.UserReactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface EntityUserReactionRepository extends JpaRepository<UserReactionEntity, UUID> {
    @Query(value = "DELETE FROM user_reactions e WHERE e.reaction_id = :reaction_id", nativeQuery = true)
    @Transactional
    @Modifying
    public void delete(@Param("reaction_id") UUID id);
    
}
