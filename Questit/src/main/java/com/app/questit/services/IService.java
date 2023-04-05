package com.app.questit.services;


import com.app.questit.domain.Task;
import com.app.questit.domain.User;

import java.util.HashMap;

public interface IService {
    Iterable<Task> getAvailableTasksForUser(Long userId, String description);
    Iterable<Task> getSolvedTasksForUser(Long userId);
    Iterable<User> getAllUsers();
    HashMap<User, Integer> getLeaderboard();
    void completeTask(Long taskId,Long userId);
    void addTaskResponse(Long taskId,Long responderId,String description);
    void addTask(String description,Long askerId);
    void deleteTask(Long taskId);
    int getBadgeLevelForUser(Long userId);
    int getTokensForUser(Long userId);
    int getRankForUser(Long userId);

    User getUserById(Long asker_id);
    User getUserByUsernameAndPassword(String username,String password);

}
