package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.*;

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
    Label student_id;
    @FXML
    Button addStudent;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        addLabel();
    }

    public void addLabel() {
        try {
            // 加载新的FXML文件
            DataRequest dataRequest = new DataRequest();
            Map<String, String> map = new HashMap<>();
            map.put("id", String.valueOf(CourseSpecificViewController.id));
            dataRequest.setData(map);
            Result data = HttpRequestUtils.courseField("/course/selectStudentCourse", dataRequest);
            List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
            if (dataList.size() == 0) {
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
                for (Map<String, String> a : dataList) {
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
                    Button button = new Button("移除班级");
                    button.setId("student");
                    button.setLayoutX(300);
                    button.setLayoutY(82 + (count - 1) * 60);
                    button.setOnAction(event -> {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setContentText("您是否要移除该学生");
                        // 设置对话框的按钮类型
                        ButtonType buttonTypeOK = new ButtonType("确定");
                        ButtonType buttonTypeCancel = new ButtonType("取消");
                        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
                        // 显示弹窗并等待按钮点击事件
                        Optional<ButtonType> result = alert.showAndWait();
                        // 处理按钮点击事件
                        if (result.isPresent() && result.get() == buttonTypeOK) {
                            DataRequest dataRequest1 = new DataRequest();
                            Map<String, String> map1 = new HashMap<>();
                            map1.put("student_id", String.valueOf(a.get("student_id")));
                            map1.put("course_id", String.valueOf(CourseSpecificViewController.id));
                            dataRequest1.setData(map1);

                            try {
                                Result data1 = HttpRequestUtils.courseField("/course/deleteStudent", dataRequest1);
                                String data2 = data1.getMsg();
                                Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
                                alert1.setHeaderText(data2);
                                alert1.showAndWait();
                                deleteNode();
                                addLabel();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            alert.close();
                        }
                    });
                    pane.getChildren().add(button);
                    count++;
                }
                Label label1 = new Label();
                label1.setMaxSize(450, 30);
                label1.setId("teacher");
                label1.setTextAlignment(TextAlignment.CENTER);
                label1.setWrapText(true);
                label1.setFont(new Font(20));
                label1.setText(String.valueOf(dataList.get(0).get("teacher_name")));
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

    public void deleteNode() {
        List<javafx.scene.Node> toRemove = new ArrayList<>();
        pane.getChildren().forEach(node -> {
            if (node.getId()=="student" && (node instanceof Label || node instanceof Button) ){
                toRemove.add(node);
            }
        });
        for(Node b :toRemove){
            pane.getChildren().remove(b);
        }
    }
    @FXML
    public void addStu(){
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("add-student-view.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("添加学生界面");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
            Node node = addStudent.getScene().getRoot();
            Window window = node.getScene().getWindow();
            window.hide(); // 关闭窗口
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

