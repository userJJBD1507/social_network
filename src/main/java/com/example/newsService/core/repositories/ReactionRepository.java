package com.example.newsService.core.repositories;

import com.example.newsService.core.Reaction;
import com.example.newsService.core.reaction.entities.ReactionEntity;

public interface ReactionRepository {

    void addReaction(ReactionEntity reaction);

    void deleteReaction(ReactionEntity reaction);
}
