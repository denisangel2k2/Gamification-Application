package com.app.questit.controllers;

import com.app.questit.domain.Task;
import com.app.questit.domain.User;
import com.app.questit.services.AppService;
import javafx.beans.value.WeakChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.util.List;


public class MainController {
    private AppService service;
    private ObservableList<Task> availabeTasks= FXCollections.observableArrayList();

    private ObservableList<User> leaderboard= FXCollections.observableArrayList();

    @FXML
    private ImageView tasksButtonView;

    @FXML
    private ImageView addTaskButtonView;

    @FXML
    private ImageView solvedButtonView;

    @FXML
    private VBox navBar;

    @FXML
    private ImageView logoutButtonView;

    @FXML
    private ImageView profileButtonView;
    @FXML
    private TextField searchTaskTextField;
    @FXML
    private TableColumn<Task, String> askerColumn;

    @FXML
    private TableColumn<Task, String> descriptionColumn;

    @FXML
    private TableView<User> leaderboardTableView;

    @FXML
    private AnchorPane mainViewPane;

    @FXML
    private TableColumn<User, String> questsLbColumn;

    @FXML
    private TableView<Task> questsTableView;

    @FXML
    private TableColumn<Task, String> statusColumn;

    @FXML
    private TableColumn<User, String> usernameLbColumn;

    private long loggedUserId;
    public void setService(AppService service, long loggedUserId){
        this.service=service;
        this.loggedUserId=loggedUserId;
        initLists();

    }

    public void initialize(){

        questsTableView.setItems(availabeTasks);
        leaderboardTableView.setItems(leaderboard);
        questsTableView.setOpacity(1);
        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactoryUser =
                new Callback<>() {
                    @Override
                    public TableCell<Task, String> call(TableColumn<Task, String> param) {
                        final TableCell<Task, String> cell = new TableCell<>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    User user=service.getUserById(getTableView().getItems().get(getIndex()).getAsker_id());
                                    setText(user.getUsername());
                                }
                            }
                        };
                        return cell;
                    }
                };

        Callback<TableColumn<Task, String>, TableCell<Task, String>> cellFactoryDesc =
                new Callback<>() {
                    @Override
                    public TableCell<Task, String> call(TableColumn<Task, String> param) {
                        final TableCell<Task, String> cell = new TableCell<>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    Task task=getTableView().getItems().get(getIndex());
                                    if (task.getDescription().length()>20)
                                        setText(task.getDescription().substring(0,20)+"...");
                                    else
                                        setText(task.getDescription());
                                }
                            }
                        };
                        return cell;
                    }
                };



        askerColumn.setCellFactory(cellFactoryUser);
        descriptionColumn.setCellFactory(cellFactoryDesc);
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));



        Callback<TableColumn<User, String>, TableCell<User, String>> cellFactoryQuests =
                new Callback<>() {
                    @Override
                    public TableCell<User, String> call(TableColumn<User, String> param) {
                        final TableCell<User, String> cell = new TableCell<>() {
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    int numberOfSolvedQuests=((List<Task>)service.getSolvedTasksForUser(getTableView().getItems().get(getIndex()).getId())).size();
                                    setText(String.valueOf(numberOfSolvedQuests));
                                }
                            }
                        };
                        return cell;
                    }
                };

        questsLbColumn.setCellFactory(cellFactoryQuests);
        usernameLbColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        searchTaskTextField.textProperty().addListener((observable, oldValue, newValue) -> { filterList(); });




    }
    private void filterList(){
        String filter=searchTaskTextField.getText();
        List<Task> tasks= (List<Task>) service.getAvailableTasksForUser(loggedUserId,filter);
        availabeTasks.setAll(tasks);
    }
    private void initLists(){
        List<Task> tasks= (List<Task>) service.getAvailableTasksForUser(loggedUserId,"");
        availabeTasks.setAll(tasks);
        //sort users by number of solved quests descending
        List<User> users= (List<User>) service.getAllUsers();
        users.sort((o1, o2) -> {
            int numberOfSolvedQuests1=((List<Task>)service.getSolvedTasksForUser(o1.getId())).size();
            int numberOfSolvedQuests2=((List<Task>)service.getSolvedTasksForUser(o2.getId())).size();
            return numberOfSolvedQuests2-numberOfSolvedQuests1;
        });

        leaderboard.setAll(users);

    }
}
