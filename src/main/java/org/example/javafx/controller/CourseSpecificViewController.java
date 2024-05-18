package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
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
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.AppStore;
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
import java.util.Optional;

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
    @FXML
    Label updatet;
    static TextField textField = new TextField();
    Integer userType;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        if(AppStore.getUser().getUser_type_id() == 3){
            updateTime.setVisible(false);
            updatet.setVisible(false);
        }
        userType = AppStore.getUser().getUser_type_id();
        textField.setText("");
        textField.setText("su");
        textField.setText("");
        textField.setVisible(false);
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.equals("success")){
                FXMLLoader fxml = new FXMLLoader();
                fxml.setLocation(MainApplication.class.getResource("course-specific-view.fxml"));
                try {
                    pane.getChildren().removeAll(pane.getChildren());
                    BorderPane pane1 = new BorderPane(fxml.load());
                    pane.getChildren().add(pane1);
                    textField.setText("");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(id));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectInfoMe", dataRequest);
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
        if(dataList1.isEmpty()){
            Label label = new Label();
            label.setMaxSize(450, 30);
            label.setText("暂无上课时间，请添加上课时间");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(25));
            label.setLayoutX(200.0);
            label.setLayoutY(80);
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            lesson.getChildren().add(label);
        } else {
            if(userType != 3){
                Label label3 = new Label();
                label3.setMaxSize(450, 30);
                label3.setText("可点击上课地点和课堂描述进行编辑");
                label3.setTextAlignment(TextAlignment.CENTER);
                label3.setWrapText(true);
                label3.setFont(new Font(15));
                label3.setLayoutX(350.0);
                label3.setLayoutY(7);
                label3.setTextFill(Color.PINK);
                lesson.getChildren().add(label3);
            }
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
                if(userType == 3){
                    Button button = new Button();
                    button.setPrefWidth(103.0);
                    button.setPrefHeight(35.0);
                    button.setLayoutX(820);
                    button.setLayoutY(75 + (count - 1) * 150);
                    button.setText("提交作业");
                    lesson.getChildren().add(button);
                    button.setOnAction(event1 -> {
                        try {
                            CourseDeliverHomeworkController.course_id = String.valueOf(a.get("course_id"));
                            CourseDeliverHomeworkController.week = String.valueOf(a.get("week"));
                            CourseDeliverHomeworkController.week_time = String.valueOf(a.get("week_time"));
                            CourseDeliverHomeworkController.time_sort = String.valueOf(a.get("time_sort"));
                            // 加载新的FXML文件
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(MainApplication.class.getResource("course-deliver-homework.fxml"));
                            Parent root = fxmlLoader.load();
                            // 创建新的Stage
                            Stage newStage = new Stage();
                            newStage.initStyle(StageStyle.DECORATED);
                            newStage.setTitle("提交作业界面");
                            newStage.setScene(new Scene(root));
                            newStage.setResizable(false);
                            newStage.initModality(Modality.APPLICATION_MODAL);
                            newStage.showAndWait();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                } else {
                    Button button = new Button();
                    button.setPrefWidth(103.0);
                    button.setPrefHeight(35.0);
                    button.setLayoutX(820);
                    button.setLayoutY(75 + (count - 1) * 150);
                    button.setText("删除本节课");
                    button.setOnAction(event2 -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("您是否要删除本节课");
                        // 设置对话框的按钮类型
                        ButtonType buttonTypeOK = new ButtonType("确定");
                        ButtonType buttonTypeCancel = new ButtonType("取消");
                        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
                        // 显示弹窗并等待按钮点击事件
                        Optional<ButtonType> result = alert.showAndWait();
                        // 处理按钮点击事件
                        if (result.isPresent() && result.get() == buttonTypeOK) {
                            Map<String,String> map = new HashMap<>();
                            map.put("course_id", String.valueOf(a.get("course_id")));
                            map.put("week", String.valueOf(a.get("week")));
                            map.put("week_time",String.valueOf(a.get("week_time")));
                            map.put("time_sort",String.valueOf(a.get("time_sort")));
                            DataRequest dataRequest = new DataRequest();
                            dataRequest.setData(map);
                            Result data = null;
                            try {
                                data = HttpRequestUtils.courseField("/lesson/deleteLesson", dataRequest);
                                textField.setText("success");
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                alert1.setHeaderText(data.getMsg());
                                alert1.showAndWait();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                        } else {
                            alert.close();
                        }
                    });
                    lesson.getChildren().add(button);
                }
                if(userType == 3){

                } else {
                    Button buttonHome = new Button();
                    buttonHome.setPrefWidth(113.0);
                    buttonHome.setPrefHeight(35.0);
                    buttonHome.setLayoutX(700);
                    buttonHome.setLayoutY(75 + (count - 1) * 150);
                    buttonHome.setText("编辑本节作业");
                    buttonHome.setOnAction(event1 -> {
                        try {
                            CourseLessonHomeworkController.course_id = String.valueOf(a.get("course_id"));
                            CourseLessonHomeworkController.week = String.valueOf(a.get("week"));
                            CourseLessonHomeworkController.week_time = String.valueOf(a.get("week_time"));
                            CourseLessonHomeworkController.time_sort = String.valueOf(a.get("time_sort"));
                            // 加载新的FXML文件
                            FXMLLoader fxmlLoader = new FXMLLoader();
                            fxmlLoader.setLocation(MainApplication.class.getResource("course-lesson-homework.fxml"));
                            Parent root = fxmlLoader.load();
                            // 创建新的Stage
                            Stage newStage = new Stage();
                            newStage.initStyle(StageStyle.DECORATED);
                            newStage.setTitle("添加/更改作业界面");
                            newStage.setScene(new Scene(root));
                            newStage.setResizable(false);
                            newStage.initModality(Modality.APPLICATION_MODAL);
                            newStage.showAndWait();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                    lesson.getChildren().add(buttonHome);
                }
                Label label = new Label();
                label.setMaxSize(450, 30);
                label.setId(String.valueOf(a.get("id")));
                label.setTextAlignment(TextAlignment.CENTER);
                label.setWrapText(true);
                label.setFont(new Font(25));
                if(userType != 3){
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
                            Button eqit = new Button();
                            eqit.setText("返回");
                            eqit.setPrefWidth(60.0);
                            eqit.setPrefHeight(30.0);
                            eqit.setLayoutX(label.getLayoutX() + 270);
                            eqit.setLayoutY(label.getLayoutY());
                            lesson.getChildren().add(eqit);
                            eqit.setOnAction(event3 ->{
                                textField.setVisible(false);
                                eqit.setVisible(false);
                                buttonLabel.setVisible(false);
                                label.setVisible(true);
                            });
                            buttonLabel.setOnAction(event1 -> {
                                Map<String,String> map = new HashMap<>();
                                map.put("notes",textField.getText());
                                map.put("course_id",id);
                                map.put("week", String.valueOf(a.get("week")));
                                map.put("week_time",String.valueOf(a.get("week_time")));
                                map.put("time_sort",String.valueOf(a.get("time_sort")));
                                DataRequest dataRequest = new DataRequest();
                                dataRequest.setData(map);
                                Result data = null;
                                try {
                                    data = HttpRequestUtils.courseField("/lesson/updateInfo", dataRequest);
                                    if(data.getCode() != 200){
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText(data.getMsg());
                                        alert.showAndWait();
                                    } else {
                                        label.setText(textField.getText());
                                        buttonLabel.setVisible(false);
                                        textField.setVisible(false);
                                        label.setVisible(true);
                                        eqit.setVisible(false);
                                    }

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    });
                }
                if((a.get("notes")) == null || String.valueOf(a.get("notes")).equals("")){
                    if(userType == 3){
                        label.setText("暂无描述");
                    } else {
                        label.setText("暂无描述，请点击编辑");
                    }

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
                label1.setLayoutX(90.0);
                label1.setLayoutY(30 + (count - 1) * 150);
                label1.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
                if(userType != 3){
                    label1.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1) {  // 检查是否是单击事件
                            //可编辑操作
                            label1.setVisible(false);
                            TextField textField = new TextField();
                            textField.setPrefHeight(30);
                            textField.setPrefWidth(200);
                            textField.setMaxSize(450, 30);
                            textField.setLayoutX(label1.getLayoutX() + 100);
                            textField.setLayoutY(label1.getLayoutY());
                            lesson.getChildren().add(textField);
                            Button buttonLabel = new Button();
                            buttonLabel.setText("保存");
                            buttonLabel.setPrefWidth(60.0);
                            buttonLabel.setPrefHeight(30.0);
                            buttonLabel.setLayoutX(label1.getLayoutX());
                            buttonLabel.setLayoutY(label1.getLayoutY());
                            lesson.getChildren().add(buttonLabel);
                            Button eqit = new Button();
                            eqit.setText("返回");
                            eqit.setPrefWidth(60.0);
                            eqit.setPrefHeight(30.0);
                            eqit.setLayoutX(label1.getLayoutX() + 350);
                            eqit.setLayoutY(label1.getLayoutY());
                            lesson.getChildren().add(eqit);
                            eqit.setOnAction(event3 ->{
                                textField.setVisible(false);
                                eqit.setVisible(false);
                                buttonLabel.setVisible(false);
                                label1.setVisible(true);
                            });
                            buttonLabel.setOnAction(event1 -> {
                                Map<String,String> map = new HashMap<>();
                                map.put("room",textField.getText());
                                map.put("course_id",id);
                                map.put("week", String.valueOf(a.get("week")));
                                map.put("week_time",String.valueOf(a.get("week_time")));
                                map.put("time_sort",String.valueOf(a.get("time_sort")));
                                DataRequest dataRequest = new DataRequest();
                                dataRequest.setData(map);
                                Result data = null;
                                try {
                                    data = HttpRequestUtils.courseField("/lesson/updateInfo", dataRequest);
                                    if(data.getCode() != 200){
                                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                        alert.setHeaderText(data.getMsg());
                                        alert.showAndWait();
                                    } else {
                                        label1.setText(("第" + weekC(String.valueOf(a.get("week"))) + "周") + "   " + ("星期" + weekC(String.valueOf(a.get("week_time")))) + "    " + time + "    " + textField.getText());
                                        buttonLabel.setVisible(false);
                                        textField.setVisible(false);
                                        label1.setVisible(true);
                                        eqit.setVisible(false);
                                    }

                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                            });
                        }
                    });
                }

                if((a.get("room")) == null || String.valueOf(a.get("room")).equals("")){
                    if(userType == 3){
                        label1.setText(("第" + weekC(String.valueOf(a.get("week"))) + "周") + "   " + ("星期" + weekC(String.valueOf(a.get("week_time")))) + "    " + time + "    " + "暂无教室");
                    } else {
                        label1.setText(("第" + weekC(String.valueOf(a.get("week"))) + "周") + "   " + ("星期" + weekC(String.valueOf(a.get("week_time")))) + "    " + time + "    " + "暂无教室，请点击编辑");
                    }

                } else {
                    label1.setText(("第" + weekC(String.valueOf(a.get("week"))) + "周") + "   " + ("星期" + weekC(String.valueOf(a.get("week_time")))) + "    " + time + "    " + a.get("room"));
                }
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
            fxmlLoader.setLocation(MainApplication.class.getResource("course-homework.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("作业统计");
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
        CourseLessonController.list.clear();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
