package com.app.questit.controllers;

import com.app.questit.Main;
import com.app.questit.domain.DataTypes.QuestType;
import com.app.questit.domain.Quest;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import com.app.questit.utils.patterns.Observer;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class GuessTheImageController implements Observer {
    @FXML
    private ImageView angryImage;

    @FXML
    private Button backButton;

    @FXML
    private ImageView castleImage;

    @FXML
    private TextArea descriptionTextArea;

    @FXML
    private ImageView giraffeImage;

    @FXML
    private AnchorPane imagesPane;

    @FXML
    private ImageView sultanImage;

    @FXML
    private Label timeLabel;

    @FXML
    private Label tipLabel;

    @FXML
    private Label tokenLabel;

    @Override
    public void update() {

    }

    private long loggedInUserId;
    private AppService service;
    private Quest quest;

    public void setService(AppService service, Quest quest, long loggedInUserId) {
        this.service = service;
        this.service.addObserver(this);
        this.loggedInUserId = loggedInUserId;
        this.quest = quest;
        initGame();

        startTime = System.currentTimeMillis();

    }

    HashMap<Integer, List<String>> getHints() {
        HashMap<Integer, List<String>> hints = new HashMap<>();
        List<String> giraffeHints = new ArrayList<>();
        List<String> angryHints = new ArrayList<>();
        List<String> sultanHints = new ArrayList<>();
        List<String> castleHints = new ArrayList<>();

        giraffeHints.add("It is a mammal");
        giraffeHints.add("It is a herbivore");
        giraffeHints.add("It is a long necked animal");
        giraffeHints.add("It has two eyes");

        angryHints.add("We all get to that point sometime");
        angryHints.add("You can see how he feels");
        angryHints.add("Not happy");
        angryHints.add("Table flipping");

        sultanHints.add("He is a king");
        sultanHints.add("He is a ruler");
        sultanHints.add("He is a leader");
        sultanHints.add("He is a monarch");

        castleHints.add("It is majestic");
        castleHints.add("It is a fortress");
        castleHints.add("It is a place of refuge");
        castleHints.add("It gives you a sense of security");

        hints.put(1, giraffeHints);
        hints.put(2, angryHints);
        hints.put(3, sultanHints);
        hints.put(4, castleHints);

        return hints;
    }

    private void pause(int time) {
        PauseTransition pauseTransition = new PauseTransition(Duration.seconds(time));
        pauseTransition.setOnFinished(event -> {
            imagesPane.setVisible(true);
        });
        pauseTransition.play();
    }

    private long startTime, endTime;


    private void initGame() {

        descriptionTextArea.setText(quest.getDescription());
        var hints = getHints();

        Random rand = new Random();

        int imageIndex = rand.nextInt(4) + 1;
        int hintIndex = rand.nextInt(4);


        switch (imageIndex) {
            case 1:
                tipLabel.setText(hints.get(imageIndex).get(hintIndex));
                giraffeImage.setOnMouseClicked(event -> {
                    finishGame();
                    imagesPane.setVisible(false);
                });

                angryImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);

                });

                sultanImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });

                castleImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });


                break;
            case 2:
                tipLabel.setText(hints.get(imageIndex).get(hintIndex));
                angryImage.setOnMouseClicked(event -> {
                    finishGame();
                    imagesPane.setVisible(false);
                });
                giraffeImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });
                sultanImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });
                castleImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });

                break;
            case 3:
                tipLabel.setText(hints.get(imageIndex).get(hintIndex));
                sultanImage.setOnMouseClicked(event -> {
                    finishGame();
                    imagesPane.setVisible(false);
                });
                giraffeImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });
                angryImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });
                castleImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);


                });


                break;
            case 4:
                tipLabel.setText("Hint: " + hints.get(imageIndex).get(hintIndex));
                castleImage.setOnMouseClicked(event -> {
                    finishGame();
                    imagesPane.setVisible(false);
                });
                giraffeImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);

                });
                angryImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);

                });
                sultanImage.setOnMouseClicked(event -> {
                    timeLabel.setText("You lost 1 second");
                    imagesPane.setVisible(false);
                    pause(1);

                });

                break;
        }

    }

    private void finishGame() {
        endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        int tokens = quest.getTokens();
        int tokensObtained = (int) (tokens * (1 - ((double) timeTaken / 10000)));

        tokenLabel.setText("Tokens obtained: " + String.valueOf(tokensObtained));
        timeLabel.setText("You guessed the picture in " + String.valueOf(timeTaken / 1000) + " seconds");

        User player = service.getUserById(loggedInUserId);
        int userTokens = player.getTokens();

        tokensObtained = userTokens + tokensObtained;

        if (tokensObtained < 0)
            tokensObtained = 0;

        service.updateUser(player.getId(), player.getFirst_name(), player.getLast_name(), player.getEmail(), player.getPassword(), player.getUsername(), tokensObtained);

        if (tokensObtained > 0)
            service.completeQuest(quest.getId(), loggedInUserId);

    }

    @FXML
    void backButtonAction() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 893, 515);
            scene.getStylesheets().add(Main.class.getResource("style.css").toExternalForm());
            MainController controller = fxmlLoader.getController();
            controller.setService(service, loggedInUserId);

            Stage currentStage = (Stage) backButton.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setScene(scene);
            newStage.setOpacity(0.9);
            newStage.show();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
