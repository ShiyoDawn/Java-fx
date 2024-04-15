package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.util.Map;

public class ScoreTableController {

    @FXML
    private TableColumn<Map, String> courseNameColumn;
    @FXML
    private TableColumn<Map, String>courseNumColumn;
    @FXML
    private TextField courseTextField;
    @FXML
    private TableColumn<Map, String> creditColumn;
    @FXML
    private TableColumn<Map, String> id;
    @FXML
    private TableColumn<Map, String> markColumn;
    @FXML
    private AnchorPane myAnchorPane;
    @FXML
    private BorderPane myBorderPane;
    @FXML
    private TableColumn<Map, String> rankingColumn;
    @FXML
    private TableColumn<Map, String> studentNameColumn;
    @FXML
    private TableColumn<Map, String> studentNumColumn;
    @FXML
    private TextField studentTextField;


    @FXML
    private void onAddButtonClick(ActionEvent event) {

    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {

    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {

    }

    @FXML
    private void onQueryButtonClick(ActionEvent event) {

    }

}
