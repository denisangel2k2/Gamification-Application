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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.*;
import java.util.stream.Collectors;


public class AppService extends Observable implements IService  {
    private IUserRepository userRepository;
    private static final Logger logger= LogManager.getLogger();
    private IQuestRepository questRepository;
    private UserValidator userValidator;

    public AppService(IUserRepository userRepository, IQuestRepository questRepository, UserValidator userValidator){
        this.userRepository = userRepository;
        this.questRepository = questRepository;
        this.userValidator=userValidator;
    }

    /**
     *  Get a user by username and password
     * @param username String
     * @param password String
     * @return User
     */
    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        logger.traceEntry("getUserByUsernameAndPassword - method entered: username={}, password={}", username, password);
        List<User> users=(List<User>)userRepository.findAll();

        Optional<User> optional=users.stream()
                .filter(user -> user.getUsername().equals(username) && user.getPassword().equals(password))
                .findFirst();
        if(optional.isPresent()){
            logger.traceExit("getUserByUsernameAndPassword - method finished: user found");
            return optional.get();
        }
        else{
            logger.traceExit("getUserByUsernameAndPassword - method finished: user not found");
            return null;
        }

    }


    /**
     * Add a quest to the database
     */
    @Override
    public void addQuest(){

        logger.traceEntry("addQuest - method entered");
        List<QuestType> questTypes=new ArrayList<>();
        questTypes.addAll(List.of(QuestType.values()));

        Random random = new Random();
        QuestType type = questTypes.get(random.nextInt(questTypes.size()));

        int tokens = random.nextInt(1000 - 100 + 1) + 100;

        Quest quest=new Quest(type,tokens);
        questRepository.save(quest);
        logger.info("Quest added");
        notifyObservers();
        logger.traceExit("addQuest - method finished");
    }

    /**
     * Remove a quest that is AVAILABLE from the database
     */
    @Override
    public void removeQuest() {
        logger.traceEntry("removeQuest - method entered");
        List<Quest> quests =(List<Quest>)questRepository.findAll();
        try {
            int i=0;
            while (quests.get(i).getStatus().equals(TaskStatus.DONE) && i<quests.size())
                i++;


            questRepository.delete(quests.get(i).getId());
            logger.info("Quest removed");
        }
        catch (Exception e){
            System.out.println("No quests to delete");
            logger.error("No quests to delete");
        }
        logger.info("Quest removed");
    }

    /**
     * Adds a user to the database
     * @param first_name String
     * @param last_name String
     * @param email String
     * @param password String
     * @param username String
     * @throws RepoException
     * @throws ValidationException
     */
    @Override
    public void addUser(String first_name, String last_name, String email, String password, String username) throws RepoException, ValidationException {
        User user=new User(first_name,last_name,email,password,username);
        userValidator.validate(user);
        userRepository.save(user);
        notifyObservers();
    }

    /**
     * Update a user in the database
     * @param id Long
     * @param first_name String
     * @param last_name String
     * @param email String
     * @param password String
     * @param username String
     * @param tokens int
     */
    @Override
    public void updateUser(long id, String first_name, String last_name, String email, String password, String username, int tokens) {
        logger.traceEntry("updateUser - method entered: id={}, first_name={}, last_name={}, email={}, password={}, username={}, tokens={}", id, first_name, last_name, email, password, username, tokens);
        User user=new User(first_name,last_name,email,password,username);
        user.setId(id);
        user.setTokens(tokens);
        userRepository.update(user);

        notifyObservers();
        logger.traceExit("updateUser - method finished");
    }

    /**
     * Delete a user from the database
     * @param id Long
     */
    @Override
    public void deleteUser(Long id) {
        logger.traceEntry("deleteUser - method entered: id={}", id);
        userRepository.delete(id);
        notifyObservers();
        logger.traceExit("deleteUser - method finished");
    }

    /**
     * Get all the solved quests for a user with given id
     * @param userId Long
     * @return Iterable<Quest>
     */
    @Override
    public Iterable<Quest> getSolvedQuestsForUser(Long userId) {
        logger.traceEntry("getSolvedQuestsForUser - method entered: userId={}", userId);
        List<Quest> quests =(List<Quest>)questRepository.findAll();
        List<Quest> solvedQuests;
        solvedQuests = quests.stream()
                .filter(task -> task.getStatus().equals(TaskStatus.DONE) && task.getResponder_id().equals(userId))
                .collect(Collectors.toList());
        logger.traceExit("getSolvedQuestsForUser - method finished");
        return solvedQuests;
    }

    /**
     * Complete a quest with given id for a user with given id
     * @param taskId Long
     * @param userId Long
     */
    @Override
    public void completeQuest(Long taskId, Long userId) {
        logger.traceEntry("completeQuest - method entered: taskId={}, userId={}", taskId, userId);
        Quest quest =questRepository.findOne(taskId);
        quest.setStatus(TaskStatus.DONE);
        quest.setResponder_id(userId);
        questRepository.update(quest);

        logger.info("Quest completed");
        notifyObservers();
        logger.traceExit("completeQuest - method finished");
    }


    /**
     * Get number of tokens for a user with given id
     * @param userId Long
     * @return  int
     */
    @Override
    public int getTokensForUser(Long userId) {
        logger.traceEntry("getTokensForUser - method entered: userId={}", userId);
        User user=userRepository.findOne(userId);
        logger.traceExit("getTokensForUser - method finished");
        return user.getTokens();
    }

    /**
     * Get all the quests from the database
     * @return Iterable<Quest>
     */
    @Override
    public Iterable<Quest> getAllQuests() {

        return questRepository.findAll();

    }

    /**
     * Get the leaderboard for all the users as a hash map with user and number of solved quests
     * @return HashMap<User, Integer>
     */
    @Override
    public HashMap<User, Integer> getLeaderboard() {
        logger.traceEntry("getLeaderboard - method entered");
        List<User> users=(List<User>)userRepository.findAll();
        HashMap<User,Integer> userPoints=new HashMap<User,Integer>();
        for(User user:users){
            List<Quest> userSolvedQuests =(List<Quest>) getSolvedQuestsForUser(user.getId());
            userPoints.put(user, userSolvedQuests.size());
        }
        logger.traceExit("getLeaderboard - method finished");
        return userPoints;
    }

    /**
     * Get the rank for a user with given id
     * @param userId Long
     * @return int
     */
    @Override
    public int getRankForUser(Long userId) {
        logger.traceEntry("getRankForUser - method entered: userId={}", userId);
        var userPoints=getLeaderboard();
        User user=userRepository.findOne(userId);
        logger.traceExit("getRankForUser - method finished");
        return userPoints.get(user);
    }

    /**
     * Get all the users from the database
     * @return Iterable<User>
     */
    @Override
    public Iterable<User> getAllUsers() {

        return userRepository.findAll();
    }

    /**
     * Get a user with given id
     * @param asker_id Long
     * @return User
     */
    @Override
    public User getUserById(Long asker_id) {
        logger.traceEntry("getUserById - method entered: asker_id={}", asker_id);
        return userRepository.findOne(asker_id);
    }
}
