package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import org.example.javafx.HelloApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrameController {

    private Map<String,Tab> tabMap = new HashMap<String,Tab>();
    private Map<String, Scene> sceneMap = new HashMap<String,Scene>();

    @FXML
    BorderPane borderPane;
    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(HelloApplication.class.getResource("dashboard-view.fxml"));
        BorderPane childBorderPane = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(childBorderPane);

    }

    public void onClick() throws IOException {

    }
}
