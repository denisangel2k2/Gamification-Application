package com.app.questit.services;


import com.app.questit.domain.DataTypes.QuestType;
import com.app.questit.domain.DataTypes.TaskStatus;
import com.app.questit.domain.Quest;
import com.app.questit.domain.User;
import com.app.questit.domain.Validators.UserValidator;
import com.app.questit.repository.Interfaces.IQuestRepository;
import com.app.questit.repository.Interfaces.IUserRepository;
import com.app.questit.utils.exceptions.RepoException;
import com.app.questit.utils.exceptions.ValidationException;
import com.app.questit.utils.patterns.Observable;


import java.util.*;
import java.util.stream.Collectors;


public class AppService extends Observable implements IService  {
    private IUserRepository userRepository;

    private IQuestRepository questRepository;
    private UserValidator userValidator;

    public AppService(IUserRepository userRepository, IQuestRepository questRepository, UserValidator userValidator){
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.userValidator=userValidator;
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
    public void addQuest(){

        List<QuestType> questTypes=new ArrayList<>();
        questTypes.addAll(List.of(QuestType.values()));

        Random random = new Random();
        QuestType type = questTypes.get(random.nextInt(questTypes.size()));

        int tokens = random.nextInt(1000 - 100 + 1) + 100;

        Quest quest=new Quest(type,tokens);
        questRepository.save(quest);
        notifyObservers();

    }

    @Override
    public void removeQuest() {
        List<Quest> quests =(List<Quest>)questRepository.findAll();
        try {
            int i=0;
            while (quests.get(i).getStatus().equals(TaskStatus.DONE) && i<quests.size())
                i++;

            questRepository.delete(quests.get(i).getId());
        }
        catch (Exception e){
            System.out.println("No quests to delete");
        }
    }

    @Override
    public void addUser(String first_name, String last_name, String email, String password, String username) throws RepoException, ValidationException {
        User user=new User(first_name,last_name,email,password,username);
        userValidator.validate(user);
        userRepository.save(user);
        notifyObservers();
    }

    @Override
    public void updateUser(long id, String first_name, String last_name, String email, String password, String username, int tokens) {
        User user=new User(first_name,last_name,email,password,username);
        user.setId(id);
        user.setTokens(tokens);
        userRepository.update(user);

        notifyObservers();
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.delete(id);
        notifyObservers();
    }

    @Override
    public Iterable<Quest> getSolvedQuestsForUser(Long userId) {
        List<Quest> quests =(List<Quest>)questRepository.findAll();
        List<Quest> solvedQuests;
        solvedQuests = quests.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.DONE) && task.getResponder_id().equals(userId))
                .collect(Collectors.toList());
        return solvedQuests;
    }

    @Override
    public void completeQuest(Long taskId, Long userId) {
        Quest quest =questRepository.findOne(taskId);
        quest.setStatus(TaskStatus.DONE);
        quest.setResponder_id(userId);
        questRepository.update(quest);

        notifyObservers();
    }



    @Override
    public int getTokensForUser(Long userId) {
        User user=userRepository.findOne(userId);
        return user.getTokens();
    }

    @Override
    public Iterable<Quest> getAllQuests() {
        return questRepository.findAll();
    }

    @Override
    public HashMap<User, Integer> getLeaderboard() {
        List<User> users=(List<User>)userRepository.findAll();
        HashMap<User,Integer> userPoints=new HashMap<User,Integer>();
        for(User user:users){
            List<Quest> userSolvedQuests =(List<Quest>) getSolvedQuestsForUser(user.getId());
            userPoints.put(user, userSolvedQuests.size());
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
