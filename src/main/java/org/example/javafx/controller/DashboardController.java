
package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardController {
    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML
    Button selectCourseSheet;
    @FXML
    ComboBox comboBoxTerm;
    @FXML
    ComboBox comboBoxWeek;
    @FXML
    GridPane gridPane;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        addlabel(Integer.parseInt(getCurrentTime().get("week")), 1, getCurrentTime().get("terms"));
    }

    //添加课程表上的课程

    private void addlabel(int week, Integer student_id, String terms) throws IOException, InterruptedException {
        Map<String, String> student = new HashMap();
        student.put("student_id", String.valueOf(student_id));
        student.put("week", String.valueOf(week));
        student.put("terms", terms);
        DataRequest dataRequest = new DataRequest();
        dataRequest.setData(student);
        Result data = HttpRequestUtils.courseField("/course/selectLessonByStudent", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        for (Map<String, ? extends Object> a : dataList) {
            String course_name = (String) a.get("course_name");
            double week_time = (double) a.get("week_time");
            String room = (String) a.get("room");
            double time_sort = (double) a.get("time_sort");
            Label label = new Label();
            label.setId("1");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            GridPane.setHgrow(label, Priority.ALWAYS);
            GridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle("-fx-background-color: #00ff33; -fx-padding: 10;");
            label.setText(course_name + "\n" + "\n" + room);
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) { // 检查是否是单击事件
                    detailCourse();
                }
            });
            gridPane.setHalignment(label, HPos.CENTER); // 设置水平对齐方式为居中
            gridPane.setValignment(label, VPos.CENTER); // 设置垂直对齐方式为居中
            gridPane.add(label, (int) week_time, (int) (time_sort + 1));
        }

        /*Result data = HttpRequestUtils.courseField("/course/selectLessonByStudent",dataRequest);
        List<Map<String,String>> dataList = new Gson().fromJson(data.getData().toString(),List.class);
        for (Map<String,String> a: dataList) {
            //String week = a.get("week");
            String course_name = a.get("course_name");
            //String terms = a.get("terms");
            String week_time = a.get("week_time");
            String room = a.get("room");
            String time_sort = a.get("time_sort");
            String teacher_name = a.get("teacher_name");
            addLabel(week_time,time_sort,course_name,room,teacher_name);
        }*/
    }

    private Map<String, String> selectData() {
        Map<String, String> map = new HashMap<>();
        String a = (String) comboBoxTerm.getValue();
        if (a == null) {
            return null;
        }
        String b = (String) comboBoxWeek.getValue();
        if (b == null) {
            return null;
        }
        map.put("terms", a);
        switch (b.length()) {
            case 4:
                map.put("week", b.substring(1, 3));
                break;
            case 3:
                map.put("week", b.substring(1, 2));
                break;
        }
        return map;
    }

    public void select() throws IOException, InterruptedException {
        if (selectData() == null) {
            return;
        } else {
            //防止异常
            List<Node> toRemove = new ArrayList<>();
            gridPane.getChildren().forEach(node -> {
                if (node.getId() == "1" && node instanceof Label) {
                    toRemove.add(node);
                }
            });
            for (Node a : toRemove) {
                gridPane.getChildren().remove(a);
            }


            int week = Integer.parseInt(selectData().get("week"));
            String terms = selectData().get("terms");
            addlabel(week, 1, terms);
        }
    }

    private void detailCourse() {

    }

    private Map<String, String> getCurrentTime() {
        Map<String, String> map = new HashMap<>();
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String time = formattedTime.substring(0, 10);
        if (time.compareTo("2023-09-05") >= 0 && time.compareTo("2024-02-26") < 0) {
            map.put("terms", "2023-2024-1");
            map.put("week","19");
            comboBoxTerm.setValue("2023-2024-1");
        } else if (time.compareTo("2024-02-26") >= 0 && time.compareTo("2024-09-05") < 0) {
            map.put("terms", "2023-2024-2");
            comboBoxTerm.setValue("2023-2024-2");
            if (time.compareTo("2024-03-04") < 0) {
                map.put("week", "1");
                comboBoxWeek.setValue("第一周");
            } else if (time.compareTo("2024-03-11") < 0) {
                map.put("week", "2");
                comboBoxWeek.setValue("第2周");
            } else if (time.compareTo("2024-03-18") < 0) {
                map.put("week", "3");
                comboBoxWeek.setValue("第3周");
            } else if (time.compareTo("2024-03-25") < 0) {
                map.put("week", "4");
                comboBoxWeek.setValue("第4周");
            } else if (time.compareTo("2024-04-01") < 0) {
                map.put("week", "5");
                comboBoxWeek.setValue("第5周");
            } else if (time.compareTo("2024-04-08") < 0) {
                map.put("week", "6");
                comboBoxWeek.setValue("第6周");
            } else if (time.compareTo("2024-04-15") < 0) {
                map.put("week", "7");
                comboBoxWeek.setValue("第7周");
            } else if (time.compareTo("2024-04-22") < 0) {
                map.put("week", "8");
                comboBoxWeek.setValue("第8周");
            } else if (time.compareTo("2024-04-29") < 0) {
                map.put("week", "9");
                comboBoxWeek.setValue("第9周");
            } else if (time.compareTo("2024-05-06") < 0) {
                map.put("week", "10");
                comboBoxWeek.setValue("第10周");
            } else if (time.compareTo("2024-05-13") < 0) {
                map.put("week", "11");
                comboBoxWeek.setValue("第11周");
            } else if (time.compareTo("2024-05-20") < 0) {
                map.put("week", "12");
                comboBoxWeek.setValue("第12周");
            } else if (time.compareTo("2024-05-27") < 0) {
                map.put("week", "13");
                comboBoxWeek.setValue("第13周");
            } else if (time.compareTo("2024-06-03") < 0) {
                map.put("week", "14");
                comboBoxWeek.setValue("第14周");
            } else if (time.compareTo("2024-06-10") < 0) {
                map.put("week", "15");
                comboBoxWeek.setValue("第15周");
            } else if (time.compareTo("2024-06-17") < 0) {
                map.put("week", "16");
                comboBoxWeek.setValue("第16周");
            } else if (time.compareTo("2024-06-24") < 0) {
                map.put("week", "17");
                comboBoxWeek.setValue("第17周");
            } else if (time.compareTo("2024-07-01") < 0) {
                map.put("week", "18");
                comboBoxWeek.setValue("第18周");
            } else if (time.compareTo("2024-07-08") < 0) {
                map.put("week", "19");
                comboBoxWeek.setValue("第19周");
            } else {
                map.put("week", "20");
                comboBoxWeek.setValue("第20周");
            }
        }
        return map;
    }


}

