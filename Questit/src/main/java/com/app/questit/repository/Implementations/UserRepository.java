package com.app.questit.repository.Implementations;


import com.app.questit.domain.User;
import com.app.questit.repository.Interfaces.IUserRepository;
import com.app.questit.repository.JdbcUtils;
import com.app.questit.utils.exceptions.RepoException;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;


public class UserRepository implements IUserRepository {

    private JdbcUtils jdbcUtils;

    public UserRepository(Properties properties) {
        jdbcUtils = new JdbcUtils(properties);
    }

    private boolean findByUsername(String username){
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from users where username=?")){
            preparedStatement.setString(1,username);
            try(ResultSet resultSet=preparedStatement.executeQuery()){
                if(resultSet.next())
                    return true;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
    @Override
    public void save(User entity) throws RepoException{
        if (findByUsername(entity.getUsername()))
            throw new RepoException("User already exists");

        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("insert into users(first_name,last_name,email,password,username,tokens) values(?,?,?,?,?,?)")){
            preparedStatement.setString(1,entity.getFirst_name());
            preparedStatement.setString(2,entity.getLast_name());
            preparedStatement.setString(3,entity.getEmail());
            preparedStatement.setString(4,entity.getPassword());
            preparedStatement.setString(5,entity.getUsername());
            preparedStatement.setInt(6,entity.getTokens());

            preparedStatement.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }


    }

    @Override
    public void delete(Long id) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("delete from users where id=?")){
            preparedStatement.setLong(1,id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User entity) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("update users set first_name=?,last_name=?,email=?,password=?,username=?,tokens=? where id=?")){
            preparedStatement.setString(1,entity.getFirst_name());
            preparedStatement.setString(2,entity.getLast_name());
            preparedStatement.setString(3,entity.getEmail());
            preparedStatement.setString(4,entity.getPassword());
            preparedStatement.setString(5,entity.getUsername());
            preparedStatement.setInt(6,entity.getTokens());
            preparedStatement.setLong(7,entity.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public User findOne(Long id) {
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from users where id=?")){
            preparedStatement.setLong(1,id);
            ResultSet resultSet=preparedStatement.executeQuery();
            resultSet.next();

            long idd=resultSet.getLong("id");
            String first_name=resultSet.getString("first_name");
            String last_name=resultSet.getString("last_name");
            String email=resultSet.getString("email");
            String password=resultSet.getString("password");
            String username=resultSet.getString("username");
            int tokens=resultSet.getInt("tokens");

            User user=new User(first_name,last_name,email,password,username);
            user.setId(idd);
            user.setTokens(tokens);
            return user;

        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    @Override
    public Iterable<User> findAll() {
        List<User> userList=new ArrayList<>();
        Connection connection=jdbcUtils.getConnection();
        try(PreparedStatement preparedStatement=connection.prepareStatement("select * from users")){
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                long id=resultSet.getLong("id");
                String first_name=resultSet.getString("first_name");
                String last_name=resultSet.getString("last_name");
                String email=resultSet.getString("email");
                String password=resultSet.getString("password");
                String username=resultSet.getString("username");
                int tokens=resultSet.getInt("tokens");

                User user=new User(first_name,last_name,email,password,username);
                user.setId(id);
                user.setTokens(tokens);
                userList.add(user);
            }
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return userList;
    }
}
