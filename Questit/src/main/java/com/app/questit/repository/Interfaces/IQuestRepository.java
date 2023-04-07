package com.app.questit.repository.Interfaces;


import com.app.questit.domain.Quest;
import com.app.questit.utils.exceptions.RepoException;


public interface IQuestRepository extends IRepository<Quest>{
    void save(Quest entity);
}
