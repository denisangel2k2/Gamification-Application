package com.app.questit.controllers;
import com.app.questit.Main;
import com.app.questit.domain.DataTypes.QuestType;
import com.app.questit.domain.DataTypes.TaskStatus;
import com.app.questit.domain.Quest;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import com.app.questit.utils.patterns.Observer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class MainController implements Observer {


    @FXML
    private TableColumn<Quest, String> actionColumn;

    @FXML
    private TableView<Quest> availableQuestsTableView;

    @FXML
    private TableColumn<Quest, String> tokensColumn;

    @FXML
    private TableColumn<Quest, String> descriptionColumn;

    @FXML
    private TableView<User> leaderboardTableView;

    @FXML
    private ImageView logoutButtonView;

    @FXML
    private AnchorPane mainViewPane;

    @FXML
    private VBox navBar;

    @FXML
    private ImageView profileButtonView;

    @FXML
    private TableColumn<User, String> questsLbColumn;

    @FXML
    private Button refreshQuestsButton;

    @FXML
    private ImageView solvedButtonView;

    @FXML
    private TextField searchUserTextField;
    @FXML
    private TableView<User> allUsersTableView;
    @FXML
    private TableColumn<User,String> usernameUPColumn;

    @FXML
    private TableColumn<User,String> tokensUPColumn;

    @FXML
    private TableColumn<User,String> questsFinishedUPColumn;
    @FXML
    private TableColumn<User,String> divisionColumn;

    @FXML
    private AnchorPane allUsersPane;

    @FXML
    private AnchorPane completedQuestsPane;

    @FXML
    private TableView<Quest> completedQuestsTableView;

    @FXML
    private TableColumn<Quest, String> descriptionCQColumn;

    @FXML
    private TableColumn<Quest, String> tokensCQColumn;

    @FXML
    private TableColumn<Quest, String> statusCQColumn;

    @FXML
    private TextArea questCQDescription;

    @FXML
    private TableColumn<Quest, String> statusColumn;

    @FXML
    private ImageView tasksButtonView;

    @FXML
    private Label usernameLabel;

    @FXML
    private Label firstnameLabel;
    @FXML
    private Label lastnameLabel;
    @FXML
    private Label emailLabel;

    @FXML
    private AnchorPane bronzeBadge;

    @FXML
    private AnchorPane goldBadge;

    @FXML
    private AnchorPane platinumBadge;

    @FXML
    private AnchorPane obsidianBadge;

    @FXML
    private Button deleteAccountButton;
    @FXML
    private AnchorPane userInfoPane;

    @FXML
    private TableColumn<User, String> usernameLbColumn;

    @FXML
    private ImageView usersButtonView;

    @FXML
    private ImageView completedQuestsButtonView;

    @FXML
    private Label tokensUPLabel;

    ObservableList<Quest> availableQuestsList= FXCollections.observableArrayList();
    ObservableList<User> leaderboardList= FXCollections.observableArrayList();
    ObservableList<Quest> completedQuestsList= FXCollections.observableArrayList();
    ObservableList<User> allUsersList= FXCollections.observableArrayList();


    private long loggedUserId;
    private AppService service;
    public void setService(AppService service, long loggedUserId){
        this.service=service;
        this.loggedUserId=loggedUserId;
        this.service.addObserver(this);
        update();


    }

    private void handleLeaderboard(){
        leaderboardTableView.setItems(leaderboardList);
        usernameLbColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        questsLbColumn.setCellFactory(cellFact->{
            TableCell<User, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        User currentUser= getTableView().getItems().get(getIndex());
                        setText(String.valueOf(((List<Quest>)service.getSolvedQuestsForUser(currentUser.getId())).size()));
                    }
                }
            };
            return cell;
        });
    }
    private void handleAvailableQuests(){
        availableQuestsTableView.setItems(availableQuestsList);
        Callback<TableColumn<Quest, String>, TableCell<Quest, String>> cellFactoryAction =
                new Callback<>() {
                    @Override
                    public TableCell<Quest, String> call(TableColumn<Quest, String> param) {
                        final TableCell<Quest, String> cell = new TableCell<>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    Quest currentQuest= getTableView().getItems().get(getIndex());
                                    Button button = new Button("Solve");

                                    setGraphic(button);
                                    if (currentQuest.getStatus().equals(TaskStatus.DONE))
                                        button.setDisable(true);

                                    button.setOnAction(event -> {

                                        try {

                                            if (currentQuest.getQuestType().equals(QuestType.MATH)) {
                                                FXMLLoader loader = new FXMLLoader(Main.class.getResource("GuessTheNumberView.fxml"));
                                                Scene scene = new Scene(loader.load(), 312, 400);
                                                GuessTheNumberController controller = loader.getController();
                                                controller.setService(service, currentQuest, loggedUserId);
                                                Stage currentStage= (Stage) mainViewPane.getScene().getWindow();
                                                Stage newStage= new Stage();
                                                currentStage.setScene(scene);
                                                //newStage.show();
                                                //currentStage.close();
                                            }
                                            else if (currentQuest.getQuestType().equals(QuestType.PICK_PICTURE)){
                                                FXMLLoader loader = new FXMLLoader(Main.class.getResource("GuessTheImageView.fxml"));
                                                Scene scene = new Scene(loader.load(), 552, 384);
                                                GuessTheImageController controller = loader.getController();
                                                controller.setService(service, currentQuest, loggedUserId);
                                                Stage currentStage= (Stage) mainViewPane.getScene().getWindow();
                                                Stage newStage= new Stage();
                                                currentStage.setScene(scene);
                                                //newStage.show();
                                                //currentStage.close();
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionColumn.setCellFactory(cellFactoryAction);
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        tokensColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    }
    private void handleCompletedQuests(){
        completedQuestsTableView.setItems(completedQuestsList);
        descriptionCQColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        tokensCQColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        statusCQColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        completedQuestsTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                questCQDescription.setText(newSelection.getDescription());
            }
        });
    }
    private void handleAllUsers(){
        allUsersTableView.setItems(allUsersList);
        usernameUPColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        tokensUPColumn.setCellValueFactory(new PropertyValueFactory<>("tokens"));
        questsFinishedUPColumn.setCellFactory(cellFact->{
            TableCell<User, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        User currentUser= getTableView().getItems().get(getIndex());
                        setText(String.valueOf(((List<Quest>)service.getSolvedQuestsForUser(currentUser.getId())).size()));
                    }
                }
            };
            return cell;
        });
        divisionColumn.setCellFactory(cellFact->{
            TableCell<User, String> cell = new TableCell<>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                        setText(null);
                    } else {
                        User currentUser= getTableView().getItems().get(getIndex());
                        ImageView highestBadge= new ImageView();
                        highestBadge.setFitHeight(19);
                        highestBadge.setFitWidth(19);
                        int numberOfSolvedQuests= ((List<Quest>)service.getSolvedQuestsForUser(currentUser.getId())).size();
                        if (numberOfSolvedQuests>=15){
                            String path=Main.class.getResource("images/mythic.png").toString();
                            Image image=new Image(path);
                            highestBadge.setImage(image);
                            setGraphic(highestBadge);
                        }
                        else if (numberOfSolvedQuests>=10){
                            String path=Main.class.getResource("images/platinum.png").toString();
                            Image image=new Image(path);
                            highestBadge.setImage(image);
                            setGraphic(highestBadge);
                        }
                        else if (numberOfSolvedQuests>=7){
                            String path=Main.class.getResource("images/gold.png").toString();
                            Image image=new Image(path);
                            highestBadge.setImage(image);
                            setGraphic(highestBadge);
                        }
                        else if (numberOfSolvedQuests>=5){

                            String path=Main.class.getResource("images/bronze.png").toString();
                            Image image=new Image(path);
                            highestBadge.setImage(image);
                            setGraphic(highestBadge);
                        }
                        else setGraphic(null);
                        setText(null);

                    }
                }
            };
            return cell;
        });
        searchUserTextField.textProperty().addListener(o->{
            String text= searchUserTextField.getText();
            if (text.equals("")){
                allUsersTableView.setItems(allUsersList);
            }
            else{
                List<User> filteredUsers= ((List<User>)service.getAllUsers()).stream()
                        .filter(user -> user.getUsername().contains(text))
                        .collect(Collectors.toList());

                allUsersList.setAll(filteredUsers);
            }
        });
        allUsersTableView.getSelectionModel().selectedItemProperty().addListener(o->{
            initLists();
            User selectedUser= allUsersTableView.getSelectionModel().getSelectedItem();
            usernameLabel.setText(selectedUser.getUsername()+"'s profile");
            firstnameLabel.setText(selectedUser.getFirst_name());
            lastnameLabel.setText(selectedUser.getLast_name());
            emailLabel.setText(selectedUser.getEmail());
            tokensUPLabel.setText("Tokens: "+selectedUser.getTokens());

            handleBadgeVisibility(selectedUser);

            if (selectedUser.getId()==loggedUserId)
                deleteAccountButton.setVisible(true);
            else deleteAccountButton.setVisible(false);

            userInfoPane.setVisible(true);
            mainViewPane.setVisible(false);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(false);
        });
    }
    public void initialize(){

        handleAvailableQuests();
        handleLeaderboard();
        handleCompletedQuests();
        handleAllUsers();
        handleNavbarClicks();

    }
    @FXML
    private void findNewQuests(){

        User loggedUser= service.getUserById(loggedUserId);
        service.updateUser(loggedUser.getId(),loggedUser.getFirst_name(),loggedUser.getLast_name(),loggedUser.getEmail(),loggedUser.getPassword(),loggedUser.getUsername(),loggedUser.getTokens()-1000);

        for (int i=0; i<2; i++)
            service.removeQuest();

        for (int i = 0; i < 5; i++)
            service.addQuest();

        update();
    }
    private void initLists(){
        List<Quest> availableQuests= ((List<Quest>) service.getAllQuests()).stream()
                .filter(quest -> quest.getStatus().equals(TaskStatus.AVAILABLE))
                .collect(Collectors.toList());

        availableQuests.sort((o1, o2) -> {
            return o2.getTokens()-o1.getTokens();
        });

        List<User> users= (List<User>) service.getAllUsers();
        users.sort((o1, o2) -> {
            return ((List<Quest>)service.getSolvedQuestsForUser(o2.getId())).size()-((List<Quest>)service.getSolvedQuestsForUser(o1.getId())).size();
        });

        List<Quest> completedQuests= (List<Quest>)service.getSolvedQuestsForUser(loggedUserId);

        List<User> allUsers= (List<User>) service.getAllUsers();

        availableQuestsList.setAll(availableQuests);
        leaderboardList.setAll(users);
        completedQuestsList.setAll(completedQuests);
        allUsersList.setAll(allUsers);
    }

    @FXML
    private void onDeleteAccountButtonAction(){

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginView.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 266, 381);
            LoginController controller=fxmlLoader.getController();
            controller.setService(service);
            Stage currentStage = (Stage) logoutButtonView.getScene().getWindow();
            Stage newStage= new Stage();
            newStage.setTitle("Quest IT!");
            newStage.setScene(scene);
            newStage.show();
            currentStage.close();
            service.deleteUser(loggedUserId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleBadgeVisibility(User user){
        bronzeBadge.setVisible(false);
        goldBadge.setVisible(false);
        platinumBadge.setVisible(false);
        obsidianBadge.setVisible(false);

        int numberOfSolvedQuests= ((List<Quest>)service.getSolvedQuestsForUser(user.getId())).size();
        if (numberOfSolvedQuests>=5){
            bronzeBadge.setVisible(true);
        }
        if (numberOfSolvedQuests>=7){
            goldBadge.setVisible(true);
        }
        if (numberOfSolvedQuests>=10){
            platinumBadge.setVisible(true);
        }
        if (numberOfSolvedQuests>=15){
            obsidianBadge.setVisible(true);
        }
    }
    private void handleNavbarClicks(){
        profileButtonView.setOnMouseClicked(event -> {
            initLists();
            User loggedUser= service.getUserById(loggedUserId);
            usernameLabel.setText(loggedUser.getUsername()+"'s profile");
            firstnameLabel.setText(loggedUser.getFirst_name());
            lastnameLabel.setText(loggedUser.getLast_name());
            emailLabel.setText(loggedUser.getEmail());
            tokensUPLabel.setText("Tokens: "+loggedUser.getTokens());
            handleBadgeVisibility(loggedUser);
            deleteAccountButton.setVisible(true);

            userInfoPane.setVisible(true);
            mainViewPane.setVisible(false);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(false);

        });
        tasksButtonView.setOnMouseClicked(event -> {
            initLists();
            userInfoPane.setVisible(false);
            mainViewPane.setVisible(true);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(false);
        });
        usersButtonView.setOnMouseClicked(event -> {
            initLists();
            userInfoPane.setVisible(false);
            mainViewPane.setVisible(false);
            allUsersPane.setVisible(true);
            completedQuestsPane.setVisible(false);
        });
        completedQuestsButtonView.setOnMouseClicked(event -> {
            initLists();
            userInfoPane.setVisible(false);
            mainViewPane.setVisible(false);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(true);
        });

        logoutButtonView.setOnMouseClicked(event ->{
            try {

                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("LoginView.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 266, 381);
                LoginController controller=fxmlLoader.getController();
                controller.setService(service);
                Stage currentStage = (Stage) logoutButtonView.getScene().getWindow();
                Stage newStage= new Stage();
                newStage.setTitle("Quest IT!");
                newStage.setScene(scene);
                newStage.show();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void update() {
        initLists();
        User loggedUser= service.getUserById(loggedUserId);
        if (loggedUser.getTokens()>1000)
            refreshQuestsButton.setVisible(true);
        else refreshQuestsButton.setVisible(false);
    }
}
