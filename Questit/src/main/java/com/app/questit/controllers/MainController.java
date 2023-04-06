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
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.util.List;


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

    ObservableList<Quest> availableQuestsList= FXCollections.observableArrayList();
    ObservableList<User> leaderboardList= FXCollections.observableArrayList();

    private long loggedUserId;
    private AppService service;
    public void setService(AppService service, long loggedUserId){
        this.service=service;
        this.loggedUserId=loggedUserId;
        this.service.addObserver(this);
        initLists();


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
                                                newStage.setScene(scene);
                                                newStage.show();
                                                currentStage.close();
                                            }
                                            else if (currentQuest.getQuestType().equals(QuestType.PICK_PICTURE)){
                                                FXMLLoader loader = new FXMLLoader(Main.class.getResource("GuessTheImageView.fxml"));
                                                Scene scene = new Scene(loader.load(), 552, 384);
                                                GuessTheImageController controller = loader.getController();
                                                controller.setService(service, currentQuest, loggedUserId);
                                                Stage currentStage= (Stage) mainViewPane.getScene().getWindow();
                                                Stage newStage= new Stage();
                                                newStage.setScene(scene);
                                                newStage.show();
                                                currentStage.close();
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
    public void initialize(){

        handleAvailableQuests();
        handleLeaderboard();
        handleNavbarClicks();


    }

    @Override
    public void update() {
        initLists();
    }

    @FXML
    private void findNewQuests(){

        for (int i=0; i<2; i++)
            service.removeQuest();

        for (int i = 0; i < 5; i++)
            service.addQuest();

    }
    private void initLists(){
        List<Quest> availableQuests= (List<Quest>) service.getAllQuests();

        availableQuests.sort((o1, o2) -> {
            return o2.getTokens()-o1.getTokens();
        });

        List<User> users= (List<User>) service.getAllUsers();
        users.sort((o1, o2) -> {
            return ((List<Quest>)service.getSolvedQuestsForUser(o2.getId())).size()-((List<Quest>)service.getSolvedQuestsForUser(o1.getId())).size();
        });
        availableQuestsList.setAll(availableQuests);
        leaderboardList.setAll(users);
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
            User loggedUser= service.getUserById(loggedUserId);
            usernameLabel.setText(loggedUser.getUsername());
            firstnameLabel.setText(loggedUser.getFirst_name());
            lastnameLabel.setText(loggedUser.getLast_name());
            emailLabel.setText(loggedUser.getEmail());

            handleBadgeVisibility(loggedUser);

            userInfoPane.setVisible(true);
            mainViewPane.setVisible(false);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(false);

        });
        tasksButtonView.setOnMouseClicked(event -> {
            userInfoPane.setVisible(false);
            mainViewPane.setVisible(true);
            allUsersPane.setVisible(false);
            completedQuestsPane.setVisible(false);
        });

    }

}
