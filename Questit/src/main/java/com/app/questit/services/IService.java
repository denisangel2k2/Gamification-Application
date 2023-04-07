package com.app.questit.services;


import com.app.questit.domain.Quest;
import com.app.questit.domain.User;
import com.app.questit.utils.exceptions.RepoException;
import com.app.questit.utils.exceptions.ValidationException;

import java.util.HashMap;

public interface IService {
    Iterable<Quest> getSolvedQuestsForUser(Long userId);
    Iterable<User> getAllUsers();
    Iterable<Quest> getAllQuests();
    HashMap<User, Integer> getLeaderboard();
    void completeQuest(Long taskId, Long userId);
    int getTokensForUser(Long userId);
    User getUserById(Long asker_id);
    User getUserByUsernameAndPassword(String username,String password);
    int getRankForUser(Long userId);
    void addQuest();
    void removeQuest();
    void updateUser(long id, String first_name,String last_name,String email,String password,String username,int tokens);
    void addUser(String first_name,String last_name,String email,String password,String username) throws RepoException, ValidationException;

    void deleteUser(Long id);


}
