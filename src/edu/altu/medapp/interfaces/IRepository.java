package edu.altu.medapp.interfaces;

import java.util.List;
import java.util.Optional;

public interface IRepository<T> {
    void save(T entity);
    Optional<T> findById(int id);    // Primitive int
    List<T> findAll();
    void update(T entity);
    void delete(int id);             // Primitive int
}