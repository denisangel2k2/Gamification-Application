package com.app.questit.controllers;


import com.app.questit.Main;
import com.app.questit.domain.Quest;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import com.app.questit.utils.patterns.Observer;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.Random;

public class GuessTheNumberController implements Observer{
    @FXML
    private Button backButton;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private Button guessButton;

    @FXML
    private Label numberIntervalLabel;

    @FXML
    private TextField numberTextField;

    @FXML
    private Label timeLabel;

    @FXML
    private Label tokensObtainedLabel;

    private AppService service;

    private long loggedInUserId;
    private Quest quest;

    public void setService(AppService service, Quest quest , long loggedInUserId){
        this.service=service;
        this.quest=quest;
        this.loggedInUserId=loggedInUserId;
        this.service.addObserver(this);

        initGame();
    }

    @Override
    public void update() {

    }
    private int getDifficulty() {
        int tokens_to_be_obtained=quest.getTokens();
        if (tokens_to_be_obtained<=100)
            return 3;
        else if (tokens_to_be_obtained<=200)
            return 5;
        else if (tokens_to_be_obtained<=500)
            return 7;
        else if (tokens_to_be_obtained<=700)
            return 9;
        else
            return 20;

    }

    private int guessNumber;
    long startTime, endTime;
    private void initGame() {

        descriptionTextArea.setText(quest.getDescription());
        numberIntervalLabel.setText("The number is between 1 and " + String.valueOf(getDifficulty()));

        Random rand = new Random();
        int numberGuess = rand.nextInt(getDifficulty());
        guessNumber = numberGuess;

        startTime = System.currentTimeMillis();

    }

    @FXML
    private void backButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 893, 515);
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            MainController controller = fxmlLoader.getController();
            controller.setService(service, loggedInUserId);

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Stage newStage= new Stage();
            newStage.setTitle("Quest IT!");

            currentStage.setScene(scene);
            newStage.setOpacity(0.9);
            //newStage.show();
            //currentStage.close();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void finishGame(){
        endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        int tokens = quest.getTokens();
        int tokensObtained = (int) (tokens * (1 - ((double) timeTaken / 10000)));

        tokensObtainedLabel.setText("Tokens obtained: " + String.valueOf(tokensObtained));
        timeLabel.setText("You guessed the number in " + String.valueOf(timeTaken / 1000) + " seconds");

        User player=service.getUserById(loggedInUserId);
        int userTokens=player.getTokens();

        tokensObtained=userTokens+tokensObtained;

        if (tokensObtained<0)
            tokensObtained=0;



        service.updateUser(player.getId(),player.getFirst_name(),player.getLast_name(),player.getEmail(),player.getPassword(),player.getUsername(),tokensObtained);

        if (tokensObtained>0)
            service.completeQuest(quest.getId(),loggedInUserId);

        guessButton.setDisable(true);
    }
    @FXML
    private void guessButtonAction() {
        try {
            int number = Integer.parseInt(numberTextField.getText());
            if (number == guessNumber) {
                finishGame();
            } else if (number > guessNumber) {
                numberIntervalLabel.setText("The number is smaller than " + String.valueOf(number));
            } else {
                numberIntervalLabel.setText("The number is bigger than " + String.valueOf(number));
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter a number", ButtonType.OK);
            alert.show();
        }
    }


}
