package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrameController {

    private Map<String,Tab> tabMap = new HashMap<String,Tab>();
    private Map<String, Scene> sceneMap = new HashMap<String,Scene>();

    @FXML
    BorderPane borderPane;
    @FXML
    Label userLabel;
    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane childBorderPane = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(childBorderPane);
        String userInfo = AppStore.getUser().getPerson_num() + "/" + AppStore.confirmType(AppStore.getUser());
        userLabel.setText(userInfo);
    }
}
