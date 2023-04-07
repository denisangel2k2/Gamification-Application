package com.app.questit.controllers;

import com.app.questit.Main;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


public class LoginController {



    @FXML
    private TextField emailRegTextField;

    @FXML
    private TextField firstnameRegTextField;

    @FXML
    private TextField lastnameRegTextField;

    @FXML
    private Button loginButton;

    @FXML
    private Label loginLabel;

    @FXML
    private AnchorPane loginPane;

    @FXML
    private TextField passwordRegTextField;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private Button registerButton;

    @FXML
    private AnchorPane registerPane;

    @FXML
    private Label signUpLabel;

    @FXML
    private TextField usernameRegTextField;

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
            signUpLabel.setOnMouseClicked(event -> {
                registerPane.setVisible(true);
                loginPane.setVisible(false);
            });
            loginLabel.setOnMouseClicked(event -> {
                registerPane.setVisible(false);
                loginPane.setVisible(true);
            });



        }
        @FXML
        private void onRegisterButtonAction(){
            String username=usernameRegTextField.getText();
            String password=passwordRegTextField.getText();
            String email=emailRegTextField.getText();
            String firstname=firstnameRegTextField.getText();
            String lastname=lastnameRegTextField.getText();
            try{
                service.addUser(firstname,lastname,email,password,username);
                Alert alert = new Alert(Alert.AlertType.INFORMATION,"User registered successfully", ButtonType.OK);
                alert.show();
            }
            catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.ERROR,e.getMessage(), ButtonType.OK);
                alert.show();
            }
        }



}
