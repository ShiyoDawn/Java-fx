package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseMemberController {
    @FXML
    AnchorPane paneMember;
    @FXML
    AnchorPane pane;
    @FXML
    ScrollPane scroll;
    @FXML
    AnchorPane paneTeacher;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        try {
            // 加载新的FXML文件
            DataRequest dataRequest = new DataRequest();
            Map<String,String> map = new HashMap<>();
            map.put("id", String.valueOf(CourseSpecificViewController.id));
            dataRequest.setData(map);
            Result data = HttpRequestUtils.courseField("/course/selectStudentCourse", dataRequest);
            List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
            if(dataList.size()==0){
                Label label = new Label();
                label.setMaxSize(450, 30);
                label.setId("student");
                label.setTextAlignment(TextAlignment.CENTER);
                label.setWrapText(true);
                label.setFont(new Font(20));
                label.setText("无成员");
                label.setLayoutX(27.0);
                label.setLayoutY(80);
                pane.getChildren().add(label);
            } else {
                int count = 0;
                for (Map<String, String> a :dataList) {
                    Label label = new Label();
                    label.setMaxSize(450, 30);
                    label.setId("student");
                    label.setTextAlignment(TextAlignment.CENTER);
                    label.setWrapText(true);
                    label.setFont(new Font(20));
                    label.setText(a.get("student_name"));
                    label.setLayoutX(27.0);
                    label.setLayoutY(80 + (count - 1) * 60);
                    pane.getChildren().add(label);
                    count++;
                }
                Label label1 = new Label();
                label1.setMaxSize(450, 30);
                label1.setId("teacher");
                label1.setTextAlignment(TextAlignment.CENTER);
                label1.setWrapText(true);
                label1.setFont(new Font(20));
                label1.setText(dataList.get(0).get("teacher_name"));
                label1.setLayoutX(27.0);
                label1.setLayoutY(20);
                paneTeacher.getChildren().add(label1);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
