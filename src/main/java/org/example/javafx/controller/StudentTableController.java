package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentTableController {

    @FXML
    private Button addButton;

    @FXML
    private TableColumn classesColumn;

    @FXML
    private TableView dataTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn departmentColumn;

    @FXML
    private Button editButton;

    @FXML
    private TableColumn gradeColumn;

    @FXML
    private TableColumn id;

    @FXML
    private TableColumn majorColumn;

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
    private TableColumn student_idColumn;

    @FXML
    private TableColumn student_nameColumn;
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    private ArrayList<Map> studentList = new ArrayList();  // 学生信息列表数据
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
    public void initialize() {
        id.setCellValueFactory(new MapValueFactory<>("id"));
        student_idColumn.setCellValueFactory(new MapValueFactory("person_id"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
        DataRequest req = new DataRequest();
        List studentList = new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req); //从后台获取所有学生信息列表集合

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        setTableViewData();
    }

    private void setTableViewData() {
        observableList.clear();
        for (int j = 0; j < studentList.size(); j++) {
            observableList.addAll(FXCollections.observableArrayList(studentList.get(j)));
        }
        dataTableView.setItems(observableList);
    }
}
