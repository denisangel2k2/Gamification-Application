package com.app.questit.repository.Interfaces;


import com.app.questit.domain.Entity;
import com.app.questit.utils.exceptions.RepoException;

public interface IRepository<E extends Entity<Long>>{

    void delete(Long id);
    void update(E entity);
    E findOne(Long id);
    Iterable<E> findAll();
}
