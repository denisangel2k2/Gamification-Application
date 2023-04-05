package com.app.questit.configs;


import com.app.questit.repository.Implementations.TaskRepository;
import com.app.questit.repository.Implementations.TaskResponseRepository;
import com.app.questit.repository.Implementations.UserRepository;
import com.app.questit.services.AppService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

@Configuration
public class GamificationConfig {
    @Bean(name="props")
    Properties getProps(){
        Properties properties=new Properties();
        try{
            properties.load(new FileReader("bd.config"));
            return properties;
        }
        catch (IOException e){
            System.err.println("Cannot find properties file");
        }
        return properties;
    }

    @Bean
    TaskRepository taskRepository(){
        return new TaskRepository(getProps());
    }
    @Bean
    UserRepository userRepository(){
        return new UserRepository(getProps());
    }
    @Bean
    TaskResponseRepository taskResponseRepository(){
        return new TaskResponseRepository(getProps());
    }
    @Bean
    AppService appService(){
        return new AppService(userRepository(), taskResponseRepository(), taskRepository());
    }

}
