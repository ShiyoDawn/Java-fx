package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseHomeworkController {
    @FXML
    TableView tableView;
    @FXML
    CheckBox only;
    @FXML
    Button select;
    @FXML
    TableColumn idColumn;
    @FXML
    TableColumn person_numColumn;
    @FXML
    TableColumn student_nameColumn;
    @FXML
    TableColumn classes;
    @FXML
    TableColumn diliver;
    @FXML
    TableColumn time;
    @FXML
    TableColumn rank;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        idColumn.setCellValueFactory(new MapValueFactory<>("student_id"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        classes.setCellValueFactory(new MapValueFactory<>("classes"));
        diliver.setCellValueFactory(new MapValueFactory<>("diliver"));
        time.setCellValueFactory(new MapValueFactory<>("time"));
        rank.setCellValueFactory(new MapValueFactory<>("rank"));
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(CourseSpecificViewController.id));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectStudentCourse2", dataRequest);
        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        //改科学计数法
        for (Map<String,String> a : dataList) {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(a.get("person_num")));
            String originalNumber = bigDecimal.toPlainString();
            a.put("person_num",originalNumber);
        }
        tableView.setItems(FXCollections.observableList(dataList));
    }
    public void selectC(){

    }
}
