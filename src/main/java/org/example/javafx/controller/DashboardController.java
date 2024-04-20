package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardController {
    @FXML
    BorderPane borderPane;
    @FXML
    Label course11;
    @FXML
    Label course12;
    @FXML
    Label course13;
    @FXML
    Label course14;
    @FXML
    Label course15;
    @FXML
    Label course21;
    @FXML
    Label course22;
    @FXML
    Label course23;
    @FXML
    Label course24;
    @FXML
    Label course25;
    @FXML
    Label course31;
    @FXML
    Label course32;
    @FXML
    Label course33;
    @FXML
    Label course34;
    @FXML
    Label course35;
    @FXML
    Label course41;
    @FXML
    Label course42;
    @FXML
    Label course43;
    @FXML
    Label course44;
    @FXML
    Label course45;
    @FXML
    Label course51;
    @FXML
    Label course52;
    @FXML
    Label course53;
    @FXML
    Label course54;
    @FXML
    Label course55;
    @FXML
    Label course61;
    @FXML
    Label course62;
    @FXML
    Label course63;
    @FXML
    Label course64;
    @FXML
    Label course65;
    @FXML
    Label course71;
    @FXML
    Label course72;
    @FXML
    Label course73;
    @FXML
    Label course74;
    @FXML
    Label course75;





    @FXML
    MenuBar menuBar;
    @FXML
    GridPane gridPane;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        Map<String,String> student = new HashMap();
        student.put("student_id","1");
        DataRequest dataRequest = new DataRequest();
        dataRequest.setData(student);
        Result data = HttpRequestUtils.courseField("/course/selectLessonByStudent",dataRequest);
        /*List<Map<String,String>> dataList = new Gson().fromJson(data.getData().toString(),List.class);
        for (Map<String,String> a: dataList) {
            String week = a.get("week");
            String course_name = a.get("course_name");
            String terms = a.get("terms");
            String week_time = a.get("week_time");
            String room = a.get("room");
            String time_sort = a.get("time_sort");
            String teacher_name = a.get("teacher_name");
            addLabel(week_time,time_sort,course_name,room,teacher_name);
        }*/
    }
    private void addLabel(String week_time, String time_sort,String course_name,String room,String teacher_name){
        int weekNum = Integer.parseInt(week_time);
        int timeNum = Integer.parseInt(time_sort);
        switch (weekNum) {
            case 1:
                switch (timeNum) {
                    case 1:
                        course11.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course12.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course13.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course14.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course15.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 2:
                switch (timeNum) {
                    case 1:
                        course21.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course22.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course23.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course24.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course25.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 3:
                switch (timeNum) {
                    case 1:
                        course31.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course32.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course33.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course34.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course35.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 4:
                switch (timeNum) {
                    case 1:
                        course41.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course42.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course43.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course44.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course45.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 5:
                switch (timeNum) {
                    case 1:
                        course51.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course52.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course53.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course54.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course55.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 6:
                switch (timeNum) {
                    case 1:
                        course61.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course62.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course63.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course64.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course65.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
            case 7:
                switch (timeNum) {
                    case 1:
                        course71.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 2:
                        course72.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 3:
                        course73.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 4:
                        course74.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                    case 5:
                        course75.setText(course_name + "\n" + room + "\n" + teacher_name);
                        break;
                }
                break;
        }
    }

}
