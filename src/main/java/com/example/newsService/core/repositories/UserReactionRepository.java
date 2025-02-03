package com.example.newsService.core.repositories;

import com.example.newsService.core.UserReaction;

public interface UserReactionRepository {

    void addUserReaction(UserReaction userReaction);

    void deleteUserReaction(UserReaction userReaction);
}
