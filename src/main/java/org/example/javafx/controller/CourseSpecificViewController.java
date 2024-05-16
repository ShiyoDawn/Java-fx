package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseSpecificViewController {
    static String id;
    static String week;
//    static String week_time;
//    static String time_sort;
//    static String course_name;
    @FXML
    AnchorPane pane;
    @FXML
    Button edit;
    @FXML
    ImageView score;
    @FXML
    ImageView member;
    @FXML
    ImageView team;
    @FXML
    ImageView updateTime;
    @FXML
    Label bigName;
    @FXML
    Label bigClasses;
    @FXML
    AnchorPane lesson;
    @FXML
    Label id1;
    static TextField textField = new TextField();

    @FXML
    public void initialize() throws IOException, InterruptedException {
        textField.setText("");
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectInfo", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        bigName.setText(String.valueOf(dataList.get(0).get("course_name")));
        bigClasses.setText(String.valueOf(dataList.get(0).get("classes")));
        DataRequest dataRequest1 = new DataRequest();
        Map<String, String> map1 = new HashMap<>();
        map1.put("course_id", String.valueOf(id));
        dataRequest1.setData(map1);
        Result data1 = HttpRequestUtils.courseField("/lesson/selectByCourseId", dataRequest1);
        List<Map<String, ? extends Object>> dataList1 = new Gson().fromJson(data1.getData().toString(), List.class);
        addLabel(dataList1);

    }
    public void addLabel(List<Map<String, ? extends Object>> dataList1){
        int count = 0;
        for (Map<String, ? extends Object> a : dataList1) {
            String time;
            time = switch (a.get("time_sort").toString()) {
                case "1.0" -> "8:00-9:50";
                case "2.0" -> "10:10-12:00";
                case "3.0" -> "14:00-15:50";
                case "4.0" -> "16:10-18:00";
                case "5.0" -> "19:00-20:50";
                default -> "线上上课";
            };
            count++;
            Button button = new Button();
            button.setPrefWidth(103.0);
            button.setPrefHeight(35.0);
            button.setLayoutX(820);
            button.setLayoutY(75 + (count - 1) * 150);
            button.setText("删除本节课");
            lesson.getChildren().add(button);
            Button buttonHome = new Button();
            buttonHome.setPrefWidth(113.0);
            buttonHome.setPrefHeight(35.0);
            buttonHome.setLayoutX(700);
            buttonHome.setLayoutY(75 + (count - 1) * 150);
            buttonHome.setText("添加本节作业");
            lesson.getChildren().add(buttonHome);
            Label label = new Label();
            label.setMaxSize(450, 30);
            label.setId(String.valueOf(a.get("id")));
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(25));
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {  // 检查是否是单击事件
                    //可编辑操作
                    label.setVisible(false);
                    TextField textField = new TextField();
                    textField.setPrefHeight(30);
                    textField.setPrefWidth(200);
                    textField.setMaxSize(450, 30);
                    textField.setLayoutX(label.getLayoutX());
                    textField.setLayoutY(label.getLayoutY());
                    lesson.getChildren().add(textField);
                    Button buttonLabel = new Button();
                    buttonLabel.setText("保存");
                    buttonLabel.setPrefWidth(60.0);
                    buttonLabel.setPrefHeight(30.0);
                    buttonLabel.setLayoutX(label.getLayoutX() - 100);
                    buttonLabel.setLayoutY(label.getLayoutY());
                    lesson.getChildren().add(buttonLabel);
                    buttonLabel.setOnAction(event1 -> {
                        Map<String,String> map = new HashMap<>();
                        map.put("notes",textField.getText());
                        map.put("course_id",id);
                        map.put("week", String.valueOf(a.get("week")));
                        map.put("week_time",String.valueOf(a.get("week_time")));
                        map.put("time_sort",String.valueOf(a.get("time_sort")));
                        label.setText(textField.getText());
                        lesson.getChildren().remove(buttonLabel);
                        lesson.getChildren().remove(textField);
                        label.setVisible(true);
                        DataRequest dataRequest = new DataRequest();
                        dataRequest.setData(map);
                        Result data = null;
                        try {
                            data = HttpRequestUtils.courseField("/lesson/updateInfo", dataRequest);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
//                        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
                    });
                }
            });
            if((a.get("notes")) == null){
                label.setText("暂无描述，请点击编辑");
            } else {
                label.setText((String) a.get("notes"));
            }
            label.setLayoutX(200.0);
            label.setLayoutY(80 + (count - 1) * 150);
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            Label label1 = new Label();
            label1.setMaxSize(450, 30);
            label1.setId("notes");
            label1.setTextAlignment(TextAlignment.CENTER);
            label1.setWrapText(true);
            label1.setFont(new Font(15));
            label1.setText(("第" + weekC(String.valueOf(a.get("week"))) + "周") + "   " + ("星期" + weekC(String.valueOf(a.get("week_time")))) + "    " + time + "    " + a.get("room"));
            label1.setLayoutX(90.0);
            label1.setLayoutY(30 + (count - 1) * 150);
            label1.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            lesson.getChildren().add(label);
            lesson.getChildren().add(label1);
            Circle circle = new Circle();
            circle.setLayoutX(50);
            circle.setLayoutY(40 + (count - 1) * 150);
            circle.setRadius(5);
            circle.setFill(Color.BLUE);
            Line line = new Line();
            line.setLayoutX(50);
            line.setLayoutY(40 + (count - 1) * 150);
            line.setScaleY(150);
            lesson.getChildren().addAll(circle, line);
        }
    }
    private String weekC(String str){
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '.'){
                count = i;
            }
        }
        return str.substring(0,count);
    }

    public void memberC() {
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-member.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("成员");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void scoreC() {
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-select-view.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("选课界面");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void teamC() {
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-add-view.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("团队信息");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void homeworkC() {
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-add-view.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("成绩单");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void edit() {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("course-view.fxml"));
        try {
            pane.getChildren().removeAll(pane.getChildren());
            BorderPane pane1 = new BorderPane(fxmlLoader.load());
            pane.getChildren().add(pane1);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void timeC() {
        CourseLessonController.id = id;
        CourseLessonController.name = bigName.getText();
        CourseLessonController.source = "specific";
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-lesson.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("编辑上课时间");
            newStage.setScene(new Scene(root));
            newStage.show();
            //自动刷新->类内操作
            textField.setVisible(false);
            textField.textProperty().addListener((observable, oldValue, newValue) -> {
                if(newValue.equals("success")){
                    FXMLLoader fxml = new FXMLLoader();
                    fxml.setLocation(MainApplication.class.getResource("course-specific-view.fxml"));
                    try {
                        pane.getChildren().removeAll(pane.getChildren());
                        BorderPane pane1 = new BorderPane(fxml.load());
                        pane.getChildren().add(pane1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
