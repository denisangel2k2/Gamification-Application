package com.app.questit.repository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger= LogManager.getLogger();
    private Connection newConnection(){

        logger.traceEntry();
        String url=properties.getProperty("db.url");
        String user=properties.getProperty("db.user");
        String pass=properties.getProperty("db.pass");
        Connection con=null;
        try{
            if (user!=null && pass!=null)
                con=DriverManager.getConnection(url,user,pass);
            else con=DriverManager.getConnection(url);
            logger.trace("Connection established");
        }
        catch (SQLException exception){
            exception.printStackTrace();
            logger.trace("Connection failed");
        }
        logger.traceExit();
        return con;
    }
    public Connection getConnection(){
        logger.traceEntry();
        try{

            if (connection_instance==null || connection_instance.isClosed())
                connection_instance=newConnection();
            logger.trace("New connection established");
        }
        catch (SQLException exception){
            exception.printStackTrace();
            logger.trace("New connection failed");
        }
        logger.traceExit();
        return connection_instance;

    }

}
