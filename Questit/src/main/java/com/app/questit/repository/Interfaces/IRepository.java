package com.app.questit.repository.Interfaces;


import com.app.questit.domain.Entity;

public interface IRepository<E extends Entity<Long>>{
    void save(E entity);
    void delete(Long id);
    void update(E entity);
    E findOne(Long id);
    Iterable<E> findAll();
}
