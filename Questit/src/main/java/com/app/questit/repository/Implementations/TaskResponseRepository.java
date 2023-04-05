package com.app.questit.repository.Implementations;




import com.app.questit.domain.TaskResponse;
import com.app.questit.repository.Interfaces.ITaskResponseRepository;
import com.app.questit.repository.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class TaskResponseRepository implements ITaskResponseRepository {
    private JdbcUtils jdbcUtils;

    public TaskResponseRepository(Properties properties) {
        this.jdbcUtils = new JdbcUtils(properties);
    }

    @Override
    public void save(TaskResponse entity) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("insert into taskresponses (response,task_id,user_id) values (?,?,?)")) {
            preparedStatement.setString(1, entity.getResponse());
            preparedStatement.setLong(2, entity.getTask_id());
            preparedStatement.setLong(3, entity.getUser_id());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("delete from taskresponses where id=?")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void update(TaskResponse entity) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("update taskresponses set task_id=?, user_id=?, response=? where id=?")) {
            preparedStatement.setLong(1, entity.getTask_id());
            preparedStatement.setLong(2, entity.getUser_id());
            preparedStatement.setString(3, entity.getResponse());
            preparedStatement.setLong(4, entity.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public TaskResponse findOne(Long id) {
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from taskresponses where id=?")) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            resultSet.next();
            String response = resultSet.getString("response");
            long task_id = resultSet.getLong("task_id");
            long user_id = resultSet.getLong("user_id");
            boolean favorite = resultSet.getBoolean("favorite");
            long idd=resultSet.getLong("id");
            TaskResponse taskResponse = new TaskResponse(response, task_id, user_id);
            taskResponse.setFavorite(favorite);
            taskResponse.setId(idd);
            return taskResponse;
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<TaskResponse> findAll() {
        List<TaskResponse> responseList = new ArrayList<>();
        Connection connection = jdbcUtils.getConnection();
        try (PreparedStatement preparedStatement = connection.prepareStatement("select * from taskresponses")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String response = resultSet.getString("response");
                long task_id = resultSet.getLong("task_id");
                long user_id = resultSet.getLong("user_id");
                boolean favorite = resultSet.getBoolean("favorite");
                long id=resultSet.getLong("id");
                TaskResponse taskResponse = new TaskResponse(response, task_id, user_id);
                taskResponse.setFavorite(favorite);
                taskResponse.setId(id);
                responseList.add(taskResponse);

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return responseList;
    }
}
