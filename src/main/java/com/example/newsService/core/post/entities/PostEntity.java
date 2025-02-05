package com.example.newsService.core.post.entities;

import com.example.newsService.core.Post;
import com.example.newsService.core.mediafile.entities.MediafileEntity;
import com.example.newsService.core.reaction.entities.UserReactionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "posts")
public class PostEntity extends Post {

    @OneToMany(mappedBy = "parentPost",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    private List<PostEntity> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "parent_post_id")
    private PostEntity parentPost;


    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<UserReactionEntity> userReactions = new ArrayList<>();

    @OneToMany(mappedBy = "post",
            cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<MediafileEntity> mediafiles = new ArrayList<>();

}
