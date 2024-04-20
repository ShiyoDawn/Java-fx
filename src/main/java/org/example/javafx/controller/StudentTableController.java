package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StudentTableController {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn<?, ?> classesColumn;

    @FXML
    private TableView<?> dataTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn<?, ?> departmentColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn<?, ?> gradeColumn;

    @FXML
    private TableColumn<?, ?> id;

    @FXML
    private TableColumn<?, ?> majorColumn;

    @FXML
    private Button queryButton;

    @FXML
    private Button resetButton;

    @FXML
    private AnchorPane scoreAnchorPane;

    @FXML
    private BorderPane scoreBorderPane;

    @FXML
    private ComboBox<?> studentComboBox;

    @FXML
    private TableColumn<?, ?> student_idColumn;

    @FXML
    private TableColumn<?, ?> student_nameColumn;

    @FXML
    void onAddButtonClick(ActionEvent event) {

    }

    @FXML
    void onDeleteButtonClick(ActionEvent event) {

    }

    @FXML
    void onEditButtonClick(ActionEvent event) {

    }

    @FXML
    void onQueryButtonClick(ActionEvent event) {

    }

    @FXML
    void onResetButtonClick(ActionEvent event) {

    }

}
