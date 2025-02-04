package com.example.newsService.core.reaction.entities;

import com.example.newsService.core.UserReaction;
import com.example.newsService.core.post.entities.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_reactions")
public class UserReactionEntity extends UserReaction {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    PostEntity post;

    @ManyToOne
    @JoinColumn(name = "reaction_id", nullable = false)
    ReactionEntity reaction;

}
