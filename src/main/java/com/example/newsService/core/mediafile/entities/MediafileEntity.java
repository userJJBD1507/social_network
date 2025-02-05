package com.example.newsService.core.mediafile.entities;

import com.example.newsService.core.Mediafile;
import com.example.newsService.core.post.entities.PostEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class MediafileEntity extends Mediafile {

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post;

}
