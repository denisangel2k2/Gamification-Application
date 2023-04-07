package com.app.questit.repository.Implementations;

import com.app.questit.domain.DataTypes.QuestType;
import com.app.questit.domain.DataTypes.TaskStatus;
import com.app.questit.domain.Quest;
import com.app.questit.repository.Interfaces.IQuestRepository;
import com.app.questit.repository.JdbcUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class QuestRepository implements IQuestRepository {
    private JdbcUtils jdbcUtils;
    private static final Logger logger= LogManager.getLogger();
    public QuestRepository(Properties properties){
        jdbcUtils=new JdbcUtils(properties);
    }

    /**
     * Saves an entity
     * @param entity Quest
     */
    @Override
    public void save(Quest entity) {
        logger.traceEntry();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into quests(status,responder_id,quest_type,tokens,description) values (?,?,?,?,?)")){
            preparedStatement.setString(1,entity.getStatus().getStringValue());
            if(entity.getResponder_id()==null)
                preparedStatement.setNull(2, Types.BIGINT);
            else
                preparedStatement.setLong(2,entity.getResponder_id());
            preparedStatement.setString(3,entity.getQuestType().getStringValue());

          preparedStatement.setInt(4,entity.getTokens());
            preparedStatement.setString(5,entity.getDescription());

            preparedStatement.executeUpdate();
            logger.info("Inserted quest");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        logger.traceExit();
    }

    /**
     * Deletes an entity
     * @param id Long
     */
    @Override
    public void delete(Long id) {
        logger.traceEntry();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("delete from quests where id=?")){
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
            logger.info("Deleted quest with id: "+id);
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        logger.traceExit();
    }

    /**
     * Updates an entity which has the given entity's id
     * @param entity
     */
    @Override
    public void update(Quest entity) {
        logger.traceEntry();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update quests set status=?,responder_id=? where id=?")){
            preparedStatement.setString(1,entity.getStatus().getStringValue());
            preparedStatement.setLong(2,entity.getResponder_id());
            preparedStatement.setLong(3,entity.getId());

            preparedStatement.executeUpdate();
            logger.info("Updated quest with id: "+entity.getId());
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        logger.traceExit();
    }

    /**
     * Finds an entity by id
     * @param id Long
     * @return Quest
     */
    @Override
    public Quest findOne(Long id) {
        logger.traceEntry();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from quests where id=?")){
            preparedStatement.setLong(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            long idd=resultSet.getLong("id");
            TaskStatus status=TaskStatus.valueOf(resultSet.getString("status"));
            long responder_id=resultSet.getLong("responder_id");
            QuestType questType=QuestType.valueOf(resultSet.getString("quest_type"));
            int tokens=resultSet.getInt("tokens");
            String desc=resultSet.getString("description");

            Quest quest =new Quest(questType,tokens);


            quest.setDescription(desc);
            quest.setId(idd);
            quest.setStatus(status);
            quest.setResponder_id(responder_id);

            logger.info("Found quest with id: "+id);
            logger.traceExit();
            return quest;


        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        logger.traceExit();
        return null;
    }

    /**
     * Finds all entities
     * @return Iterable<Quest>
     */
    @Override
    public Iterable<Quest> findAll() {
        logger.traceEntry();
        List<Quest> questList =new ArrayList<>();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from quests")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                long idd=resultSet.getLong("id");
                TaskStatus status=TaskStatus.valueOf(resultSet.getString("status"));
                long responder_id=resultSet.getLong("responder_id");
                QuestType questType=QuestType.valueOf(resultSet.getString("quest_type"));
                int tokens=resultSet.getInt("tokens");
                String desc=resultSet.getString("description");

                Quest quest =new Quest(questType,tokens);


                quest.setDescription(desc);
                quest.setId(idd);
                quest.setStatus(status);
                quest.setResponder_id(responder_id);
                questList.add(quest);

            }
            logger.info("Found " +questList.size()+" quests!");
        }
        catch (SQLException e){
            e.printStackTrace();
            logger.error(e);
        }
        logger.traceExit();
        return questList;
    }
}
