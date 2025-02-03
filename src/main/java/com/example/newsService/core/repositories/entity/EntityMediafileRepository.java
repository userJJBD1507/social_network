package com.example.newsService.core.repositories.entity;

import com.example.newsService.core.mediafile.entities.MediafileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface EntityMediafileRepository extends JpaRepository<MediafileEntity, UUID> {
}
