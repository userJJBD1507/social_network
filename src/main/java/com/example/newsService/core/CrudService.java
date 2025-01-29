package com.example.newsService.core;

import java.util.Optional;

public interface CrudService<T, ID> {

    T add(T entity);

    Optional<T> get(ID id);

    T update(T entity);

    void delete(ID id);
}
