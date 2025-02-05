package com.example.newsService.core;


import java.util.Optional;
import java.util.UUID;

public interface MediafileCrud<T, ID> {
    void add(T entity);

    Optional<T> get(ID id);

    void update(UUID id, T entity);

    void delete(ID id);
}