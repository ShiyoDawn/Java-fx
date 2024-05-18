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

public class StudentClassificationController {

    @FXML
    private TableColumn activity_numberColumn;

    @FXML
    private TableColumn average_scoreColumn;

    @FXML
    private TableColumn classesColumn;

    @FXML
    private TableColumn femaleColumn;

    @FXML
    private TableColumn ge_19Column;

    @FXML
    private TableColumn glory_numberColumn;

    @FXML
    private TableColumn gradeColumn;

    @FXML
    private TableColumn highest_scoreColumn;

    @FXML
    private TableColumn le_18Column;

    @FXML
    private TableColumn league_memberColumn;

    @FXML
    private TableColumn lowest_scoreColumn;

    @FXML
    private TableColumn maleColumn;

    @FXML
    private TableColumn party_memberColumn;

    @FXML
    private TableView tableView;

    @FXML
    private void initialize() {
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        maleColumn.setCellValueFactory(new MapValueFactory<>("male"));
        femaleColumn.setCellValueFactory(new MapValueFactory<>("female"));
        party_memberColumn.setCellValueFactory(new MapValueFactory<>("party_member"));
        league_memberColumn.setCellValueFactory(new MapValueFactory<>("league_member"));
        glory_numberColumn.setCellValueFactory(new MapValueFactory<>("glory_number"));
        activity_numberColumn.setCellValueFactory(new MapValueFactory<>("activity_number"));
        average_scoreColumn.setCellValueFactory(new MapValueFactory<>("average_score"));
        highest_scoreColumn.setCellValueFactory(new MapValueFactory<>("highest_score"));
        lowest_scoreColumn.setCellValueFactory(new MapValueFactory<>("lowest_score"));
        le_18Column.setCellValueFactory(new MapValueFactory<>("le_18"));
        ge_19Column.setCellValueFactory(new MapValueFactory<>("ge_19"));
        Result result = HttpRequestUtils.request("/studentClassification/getStudentClassification",new DataRequest());
        List<Map> list = (List<Map>) result.getData();
        if (list != null) {
            tableView.setItems(FXCollections.observableList(list));
        }
    }
}
