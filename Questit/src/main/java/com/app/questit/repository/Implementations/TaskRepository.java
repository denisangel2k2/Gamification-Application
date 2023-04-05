package com.app.questit.repository.Implementations;

import com.app.questit.domain.DataTypes.TaskStatus;
import com.app.questit.domain.Task;
import com.app.questit.repository.Interfaces.ITaskRepository;
import com.app.questit.repository.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class TaskRepository implements ITaskRepository {
    private JdbcUtils jdbcUtils;
    public TaskRepository(Properties properties){
        jdbcUtils=new JdbcUtils(properties);
    }

    @Override
    public void save(Task entity) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into tasks(description,status,asker_id,responder_id) values (?,?,?,?)")){
           preparedStatement.setString(1,entity.getDescription());
              preparedStatement.setString(2,entity.getStatus().getStringValue());
              preparedStatement.setLong(3,entity.getAsker_id());
              if(entity.getResponder_id()==null)
                  preparedStatement.setNull(4,java.sql.Types.INTEGER);
              else
                preparedStatement.setLong(4,entity.getResponder_id());

              preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("delete from  where id=?")){
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Task entity) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update tasks set description=?,status=?,asker_id=?,responder_id=? where id=?")){
            preparedStatement.setString(1,entity.getDescription());
            preparedStatement.setString(2,entity.getStatus().getStringValue());
            preparedStatement.setLong(3,entity.getAsker_id());
            preparedStatement.setLong(4,entity.getResponder_id());
            preparedStatement.setLong(5,entity.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            e.printStackTrace();
        }
    }

    @Override
    public Task findOne(Long id) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from tasks where id=?")){
            preparedStatement.setLong(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();
            long idd=resultSet.getLong("id");
            String description=resultSet.getString("description");
            String status=resultSet.getString("status");
            long asker_id=resultSet.getLong("asker_id");
            long responder_id=resultSet.getLong("responder_id");
            Task task=new Task(description,asker_id);
            task.setId(idd);
            task.setStatus(TaskStatus.valueOf(status));
            task.setResponder_id(responder_id);

            return task;


        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<Task> findAll() {
        List<Task> taskList=new ArrayList<>();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from tasks")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                long id=resultSet.getLong("id");
                String description=resultSet.getString("description");
                String status=resultSet.getString("status");
                long asker_id=resultSet.getLong("asker_id");
                long responder_id=resultSet.getLong("responder_id");
                Task task=new Task(description,asker_id);
                task.setId(id);
                task.setStatus(TaskStatus.valueOf(status));
                task.setResponder_id(responder_id);
                taskList.add(task);
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return taskList;
    }
}
