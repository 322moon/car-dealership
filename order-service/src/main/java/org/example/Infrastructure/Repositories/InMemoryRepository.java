package org.example.Infrastructure.Repositories;

import org.example.exception.NotFoundException.*;
import org.example.lab1.Domain.repository.BaseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryRepository <T, ID> implements BaseRepository<T, ID> {
    protected Map<ID, T> storage = new HashMap<>();

    @Override
    public void save(T entity){
        storage.put(getID(entity), entity);
    }

    @Override
    public void deleteById(ID id){
        if (storage.remove(id) == null){
            throw new EntityNotFoundException("Object with id: " + id.toString() + " not found");
        }
    }

    @Override
    public T findById(ID id){
        if (id == null){
            throw new IllegalArgumentException("ID cannot be null");
        }
        T result = storage.get(id);
        if (result == null){
            throw new EntityNotFoundException("Entity with id: " + id.toString() + " not found");
        }

        return result;
    }

    @Override
    public List<T> findAll(){
        return new ArrayList<>(storage.values());
    }

    protected abstract ID getID(T entity);
}
