package com.example.newsService.core.repositories;

import com.example.newsService.core.Reaction;

public interface ReactionRepository {

    void addReaction(Reaction reaction);

    void deleteReaction(Reaction reaction);
}
