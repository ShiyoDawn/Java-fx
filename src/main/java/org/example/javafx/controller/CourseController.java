package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.w3c.dom.Node;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseController {

    @FXML
    ComboBox selectType;
    @FXML
    ComboBox selectTerms;
    @FXML
    Button selectCourse;
    @FXML
    TextField inputName;
    @FXML
    AnchorPane tabCenter;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        int count = 0;
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("course-view.fxml"));
        DataRequest dataRequest = new DataRequest();
        DataRequest dataRequest1 = new DataRequest();
        Result data = HttpRequestUtils.courseField("/course/selectAllType", dataRequest);
        Result dataCou = HttpRequestUtils.courseField("/course/selectAll", dataRequest1);
        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        List<Map<String, String>> dataListCou = new Gson().fromJson(dataCou.getData().toString(), List.class);
        for(Map<String, String> a : dataList){
            selectType.getItems().add(a.get("course_type_name"));
        }
        for(Map<String, String> a : dataListCou){
            count++;
            Label label = new Label();
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setText(a.get("course_name") + a.get("num") + a.get("classes") + a.get("teacher_name"));
            label.setLayoutX(37.0);
            label.setLayoutY(112+(count-1)*80);
            tabCenter.getChildren().add(label);
            if(count % 6 == 0){
                break;
            }
        }
    }


}
