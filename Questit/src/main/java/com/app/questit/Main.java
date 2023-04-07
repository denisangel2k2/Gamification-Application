package com.app.questit;

import com.app.questit.configs.GamificationConfig;
import com.app.questit.controllers.LoginController;
import com.app.questit.services.AppService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws IOException {


        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 266, 381);
        LoginController controller=fxmlLoader.getController();
        controller.setService(getService());

        stage.setTitle("Quest IT!");
        stage.setScene(scene);
        stage.show();
    }

    AppService getService(){
        ApplicationContext context =new AnnotationConfigApplicationContext(GamificationConfig.class);
        AppService service=context.getBean(AppService.class);
        return service;
    }
    public static void main(String[] args) {
        launch();

    }
}