package com.example.newsService.core;

import java.util.Optional;
import java.util.UUID;

public interface CrudService<T, ID> {

    T add(T entity);

    Optional<T> get(ID id);

    T update(UUID id, T entity);

    void delete(ID id);
}
