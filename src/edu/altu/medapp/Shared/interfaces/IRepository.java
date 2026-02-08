package edu.altu.medapp.Shared.interfaces;

import java.util.List;

public interface IRepository<T> {
    void save(T item);
    T findById(int id);
    List<T> findAll();
    void update(T item);
}