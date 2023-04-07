package com.app.questit.repository.Interfaces;


import com.app.questit.domain.User;
import com.app.questit.utils.exceptions.RepoException;

public interface IUserRepository extends IRepository<User>{

    void save(User entity) throws RepoException;
}
