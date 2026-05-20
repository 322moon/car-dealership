package org.example.lab1.Domain.repository;

import java.util.List;

public interface BaseRepository <T, ID> {
    void save(T entity);
    void deleteById(ID id);
    T findById(ID id);
    List<T> findAll();
}
