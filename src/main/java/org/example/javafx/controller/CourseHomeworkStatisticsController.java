package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CourseHomeworkStatisticsController {
    @FXML
    TableView tableView;
    @FXML
    TableColumn idColumn;
    @FXML
    TableColumn person_numColumn;
    @FXML
    TableColumn student_nameColumn;
    @FXML
    TableColumn classes;
    @FXML
    TableColumn countH;
    @FXML
    TableColumn count1;
    @FXML
    TableColumn count2;
    @FXML
    TableColumn count3;
    @FXML
    TableColumn count4;
    @FXML
    TableColumn count5;
    @FXML
    TableColumn attend;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        idColumn.setCellValueFactory(new MapValueFactory<>("student_id"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        classes.setCellValueFactory(new MapValueFactory<>("classes"));
        countH.setCellValueFactory(new MapValueFactory<>("countH"));
        count1.setCellValueFactory(new MapValueFactory<>("count1"));
        count2.setCellValueFactory(new MapValueFactory<>("count2"));
        count3.setCellValueFactory(new MapValueFactory<>("count3"));
        count4.setCellValueFactory(new MapValueFactory<>("count4"));
        count5.setCellValueFactory(new MapValueFactory<>("count5"));
        attend.setCellValueFactory(new MapValueFactory<>("attend"));
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_id", String.valueOf(CourseSpecificViewController.id));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectLessonByStudentA", dataRequest);
        AtomicReference<List<Map>> dataList = new AtomicReference<>(new Gson().fromJson(data.getData().toString(), List.class));
        //改科学计数法
        for (Map<String, String> a : dataList.get()) {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(a.get("person_num")));
            String originalNumber = bigDecimal.toPlainString();
            a.put("person_num", originalNumber);
        }
        tableView.setItems(FXCollections.observableList(dataList.get()));
        if(AppStore.getUser().getUser_type_id() == 3){
            dataList.set(CommonMethod.filter(dataList.get(), "student_name", DashboardController.student_name));
            tableView.setItems(FXCollections.observableList(dataList.get()));
        }
    }
}
