package com.app.questit.controllers;

import com.app.questit.Main;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class LoginController {


        @FXML
        private Button loginButton;

        @FXML
        private PasswordField passwordTextField;

        @FXML
        private TextField usernameTextField;


        private AppService service;

        public void setService(AppService service) {
            this.service = service;
        }

        @FXML
        public void login() {
            String username = usernameTextField.getText();
            String password = passwordTextField.getText();
            try{
                User userToLogin=service.getUserByUsernameAndPassword(username, password);
                if(userToLogin!=null){
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 893, 515);
                    scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
                    MainController controller=fxmlLoader.getController();
                    controller.setService(service, userToLogin.getId());

                    Stage currentStage=(Stage)loginButton.getScene().getWindow();
                    currentStage.setScene(scene);
                    currentStage.setOpacity(0.9);


                }
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR,"Wrong username or password", ButtonType.OK);
                    alert.show();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        public void initialize() {
            loginButton.setOnAction(e->login());
        }

}
