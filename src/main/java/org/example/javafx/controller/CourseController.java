package org.example.javafx.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.GridPane;
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
    private GridPane course;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        LocalDateTime currentTime = LocalDateTime.now();
        FXMLLoader fxmlLoader = new FXMLLoader();
        DataRequest dataRequest1 = new DataRequest();
        Result data = HttpRequestUtils.courseField("/lesson/selectLesson",dataRequest1);
        String[] course1 = data.getData().toString().split(",");
        System.out.println(course1[4]);
//        List cou = new ArrayList();
//        for (Object course : courseList){
//            String[] courseL = course.toString().split(",");
//            System.out.println(courseL);
//        }



    }


}
