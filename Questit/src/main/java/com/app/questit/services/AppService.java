package com.app.questit.services;


import com.app.questit.domain.DataTypes.TaskStatus;
import com.app.questit.domain.Task;
import com.app.questit.domain.TaskResponse;
import com.app.questit.domain.User;
import com.app.questit.repository.Interfaces.ITaskRepository;
import com.app.questit.repository.Interfaces.ITaskResponseRepository;
import com.app.questit.repository.Interfaces.IUserRepository;


import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class AppService implements IService{
    private IUserRepository userRepository;
    private ITaskResponseRepository taskResponseRepository;
    private ITaskRepository taskRepository;

    public AppService(IUserRepository userRepository, ITaskResponseRepository taskResponseRepository, ITaskRepository taskRepository) {
        this.userRepository = userRepository;
        this.taskResponseRepository = taskResponseRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        List<User> users=(List<User>)userRepository.findAll();

        Optional<User> optional=users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
        if(optional.isPresent()){
            return optional.get();
        }
        else{
            return null;
        }
    }

    @Override
    public Iterable<Task> getAvailableTasksForUser(Long userId, String description) {
        if (description==null || description.isEmpty()) {
            List<Task> tasks = (List<Task>) taskRepository.findAll();
            List<Task> availableTasks;
            availableTasks = tasks.stream()
                    .filter(task -> task.getStatus().equals(TaskStatus.AVAILABLE) && task.getAsker_id() != userId)
                    .collect(Collectors.toList());


            return availableTasks;

        }
        else{
            List<Task> tasks = (List<Task>) taskRepository.findAll();
            List<Task> availableTasks;
            availableTasks = tasks.stream()
                    .filter(task -> task.getStatus().equals(TaskStatus.AVAILABLE) && task.getAsker_id() != userId && task.getDescription().contains(description))
                    .collect(Collectors.toList());
            return availableTasks;
        }

    }

    @Override
    public Iterable<Task> getSolvedTasksForUser(Long userId) {
        List<Task> tasks=(List<Task>)taskRepository.findAll();
        List<Task> solvedTasks;
        solvedTasks=tasks.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.DONE) && task.getResponder_id().equals(userId))
                .collect(Collectors.toList());
        return solvedTasks;
    }

    @Override
    public void completeTask(Long taskId, Long userId) {
        Task task=taskRepository.findOne(taskId);
        task.setStatus(TaskStatus.DONE);
        task.setResponder_id(userId);
        taskRepository.update(task);
    }

    @Override
    public void addTaskResponse(Long taskId, Long responderId, String description) {
        TaskResponse taskResponse=new TaskResponse(description,taskId,responderId);
        taskResponseRepository.save(taskResponse);
    }

    @Override
    public void addTask(String description, Long askerId) {
        Task task=new Task(description,askerId);
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.delete(taskId);
    }

    @Override
    public int getBadgeLevelForUser(Long userId) {
        User user=userRepository.findOne(userId);
        return user.getBadge_level();
    }

    @Override
    public int getTokensForUser(Long userId) {
        User user=userRepository.findOne(userId);
        return user.getTokens();
    }


    @Override
    public HashMap<User, Integer> getLeaderboard() {
        List<User> users=(List<User>)userRepository.findAll();
        HashMap<User,Integer> userPoints=new HashMap<User,Integer>();
        for(User user:users){
            List<Task> userSolvedTasks=(List<Task>)getSolvedTasksForUser(user.getId());
            userPoints.put(user, userSolvedTasks.size());
        }
        return userPoints;
    }

    @Override
    public int getRankForUser(Long userId) {
        var userPoints=getLeaderboard();
        User user=userRepository.findOne(userId);
        return userPoints.get(user);
    }

    @Override
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long asker_id) {
        return userRepository.findOne(asker_id);
    }
}
