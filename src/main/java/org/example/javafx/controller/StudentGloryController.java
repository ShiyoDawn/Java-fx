package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

public class StudentGloryController {

    @FXML
    private Button addButton;

    @FXML
    private TableView dataTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane editAnchorPane;

    @FXML
    private Button editButton;

    @FXML
    private Button editCancelButton;

    @FXML
    private Button editComfirmButton;

    @FXML
    private TabPane editTabPane;

    @FXML
    private TextArea editTextArea;

    @FXML
    private AnchorPane gloryAnchorPane;

    @FXML
    private BorderPane gloryBorderPane;

    @FXML
    private ComboBox gloryLevelComboBox;

    @FXML
    private TableColumn<String,String> gloryNameColumn;

    @FXML
    private TableColumn<String,String> gloryTypeColumn;

    @FXML
    private TableColumn<String,String> gloryTypeColumn1;

    @FXML
    private ComboBox gloryTypeEditComboBox;

    @FXML
    private TextField gloryUpdateTextField;

    @FXML
    private TableColumn<String,String> id;

    @FXML
    private Button queryButton;

    @FXML
    private Button resetButton;

    @FXML
    private ComboBox studentComboBox;

    @FXML
    private ComboBox studentEditComboBox;

    @FXML
    private TableColumn<String,String> studentNameColumn;

    @FXML
    private TableColumn<String,String> studentNumColumn;

    @FXML
    void onAddButtonClick(ActionEvent event) {

    }

    @FXML
    void onCancelClick(ActionEvent event) {

    }

    @FXML
    void onConfirmClick(ActionEvent event) {

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
