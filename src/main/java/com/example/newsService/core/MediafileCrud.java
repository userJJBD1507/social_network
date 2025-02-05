package com.example.newsService.core;


import java.util.Optional;

public interface MediafileCrud<T, ID> {
    T add(T entity);

    Optional<T> get(ID id);

    void update(T entity);

    void delete(ID id);
}