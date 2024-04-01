package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import org.example.javafx.pojo.User;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;

public class DashboardController {
    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML
    public void initialize() {
        menuBar.getMenus().add(new Menu("Test"));
    }
}
