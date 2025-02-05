package com.example.newsService.core.repositories;

import com.example.newsService.core.UserReaction;
import com.example.newsService.core.reaction.entities.UserReactionEntity;

public interface UserReactionRepository {

    void addUserReaction(UserReactionEntity userReaction);

    void deleteUserReaction(UserReactionEntity userReaction);
}
