package com.example.newsService.core.mediafile.entities.post;

import com.example.newsService.core.Mediafile;
import com.example.newsService.core.post.entities.PostEntity;
import com.example.newsService.core.utils.MediaFileType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "t_mediafile_post")
public class MediafilePost extends Mediafile {

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    public MediafilePost(UUID id, String url, String userId, MediaFileType type) {
        super(id, url, userId, type);
    }

    public MediafilePost() {
    }
}
