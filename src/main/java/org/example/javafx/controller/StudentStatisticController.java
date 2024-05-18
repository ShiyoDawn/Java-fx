package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.util.List;
import java.util.Map;

public class StudentStatisticController {

    @FXML
    private TableColumn activity_numberColumn;

    @FXML
    private TableColumn average_scoreColumn;

    @FXML
    private TableColumn course_numberColumn;

    @FXML
    private TableColumn glory_numberColumn;

    @FXML
    private TableColumn max_scoreColumn;

    @FXML
    private TableColumn min_scoreColumn;

    @FXML
    private TableColumn person_numColumn;

    @FXML
    private TableColumn student_family_numberColumn;

    @FXML
    private TableColumn student_nameColumn;

    @FXML
    private TableView tableView;

    @FXML
    public void initialize(String person_num) {
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        student_family_numberColumn.setCellValueFactory(new MapValueFactory<>("student_family_number"));
        course_numberColumn.setCellValueFactory(new MapValueFactory<>("course_number"));
        activity_numberColumn.setCellValueFactory(new MapValueFactory<>("activity_number"));
        glory_numberColumn.setCellValueFactory(new MapValueFactory<>("glory_number"));
        average_scoreColumn.setCellValueFactory(new MapValueFactory<>("average_score"));
        max_scoreColumn.setCellValueFactory(new MapValueFactory<>("max_score"));
        min_scoreColumn.setCellValueFactory(new MapValueFactory<>("min_score"));
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_num", person_num);
        Result result = HttpRequestUtils.request("/studentStatistic/getStudentStatistic",dataRequest);
        Map<String, Object> map = (Map<String, Object>) result.getData();
        if (map != null) {
            tableView.setItems(FXCollections.observableArrayList(map));
        }
    }
}
