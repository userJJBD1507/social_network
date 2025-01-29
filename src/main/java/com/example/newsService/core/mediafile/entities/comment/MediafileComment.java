package com.example.newsService.core.mediafile.entities.comment;

import com.example.newsService.core.Mediafile;
import com.example.newsService.core.comment.entities.CommentEntity;
import com.example.newsService.core.utils.MediaFileType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.UUID;

@Entity
@Table(name = "t_mediafile_comment")
public class MediafileComment extends Mediafile {

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    public MediafileComment(UUID id, String url, String userId, MediaFileType type) {
        super(id, url, userId, type);
    }

    public MediafileComment() {
    }
}
