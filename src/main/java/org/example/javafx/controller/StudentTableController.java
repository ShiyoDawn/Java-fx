package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class StudentTableController {

    @FXML
    private Button add;

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
    private ImageView image;

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
    private TableView tableView;

    @FXML
    private Button update;
    @FXML
    public void initialize(){
        id.setCellValueFactory(new MapValueFactory<>("id"));
        person_idColumn.setCellValueFactory(new MapValueFactory<>("person_id"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
        DataRequest req = new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req);
        List<Map> studentMap = (List<Map>) studentResult.getData();
        tableView.setItems(FXCollections.observableList(studentMap));
    }
}
