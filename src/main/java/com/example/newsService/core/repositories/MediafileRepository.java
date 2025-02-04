package com.example.newsService.core.repositories;

import com.example.newsService.core.mediafile.entities.MediafileEntity;

import java.util.List;
import java.util.UUID;

public interface MediafileRepository {

    void addMediafile(MediafileEntity post);

    MediafileEntity getPost(UUID id);

    void updateMediafile(MediafileEntity post);

    void deleteMediafile(UUID id);

    List<MediafileEntity> getAllMediafiles();
}
