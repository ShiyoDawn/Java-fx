package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;

public class StudentTableController {

    @FXML
    private Button add;

    @FXML
    private Button cancel;

    @FXML
    private TextField classText;

    @FXML
    private TableColumn classesColumn;

    @FXML
    private Button delete;

    @FXML
    private TableColumn departmentColumn;

    @FXML
    private TextField departmentText;

    @FXML
    private CheckBox fuzzySearch;

    @FXML
    private TableColumn gradeColumn;

    @FXML
    private TextField gradeText;

    @FXML
    private TableColumn id;

    @FXML
    private TableColumn majorColumn;

    @FXML
    private TextField majorText;

    @FXML
    private TableColumn person_idColumn;

    @FXML
    private TextField person_idText;

    @FXML
    private Button save;

    @FXML
    private TextField select;

    @FXML
    private TableColumn student_nameColumn;

    @FXML
    private TextField student_nameText;

    @FXML
    private Button update;

    @FXML
    public void initialize(){

    }

}
