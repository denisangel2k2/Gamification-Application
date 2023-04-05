package com.app.questit.repository;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class JdbcUtils {

    private Properties properties;
    public JdbcUtils(Properties properties){
        this.properties=properties;
    }
    private Connection connection_instance=null;
    private Connection newConnection(){

        String url=properties.getProperty("db.url");
        String user=properties.getProperty("db.user");
        String pass=properties.getProperty("db.pass");
        Connection con=null;
        try{
            if (user!=null && pass!=null)
                con=DriverManager.getConnection(url,user,pass);
            else con=DriverManager.getConnection(url);
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }
        return con;
    }
    public Connection getConnection(){

        try{
            if (connection_instance==null || connection_instance.isClosed())
                connection_instance=newConnection();
        }
        catch (SQLException exception){
            exception.printStackTrace();
        }

        return connection_instance;
    }

}
