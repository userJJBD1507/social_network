package com.example.newsService.core;

import java.util.Optional;
import java.util.UUID;

import com.example.newsService.app.DTO.ReactionDTO;

public interface ReactionsCrud<T, ID> {
    void add(ReactionDTO entity);
    void delete(ID id);
}