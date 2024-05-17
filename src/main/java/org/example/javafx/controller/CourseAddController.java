package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseAddController {
    @FXML
    Button add;
    @FXML
    TextField course_name;
    @FXML
    TextField num;
    @FXML
    ComboBox precourse;
    @FXML
    TextField credit;
    @FXML
    TextField teacher;
    @FXML
    TextField book;
    @FXML
    TextField extra;
    @FXML
    TextField classes;
    @FXML
    ComboBox type;
    @FXML
    ComboBox terms;
    @FXML
    TextField capacity;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Result data = HttpRequestUtils.courseField("/course/selectAll", dataRequest);
        List<Map<String, String>> dataListType = new Gson().fromJson(data.getData().toString(), List.class);
        precourse.getItems().add("0-无前序课程");
        for (Map<String, String> a : dataListType) {
            precourse.getItems().add(String.valueOf(a.get("id")) + '-' + String.valueOf(a.get("course_name")));
        }
    }
    public void add() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        if (course_name.getText() == null || course_name.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("课程名称不能为空");
            alert.showAndWait();
        } else if (credit.getText() == null || credit.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("学分不能为空");
            alert.showAndWait();
        } else if (num.getText() == null || num.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("课序号不能为空");
            alert.showAndWait();
        } else if (book.getText() == null || book.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("教材不能为空");
            alert.showAndWait();
        } else if (extra.getText() == null || extra.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("课外书不能为空");
            alert.showAndWait();
        } else if (teacher.getText() == null || teacher.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("老师不能为空");
            alert.showAndWait();
        } else if (classes.getText() == null || classes.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("班级不能为空");
            alert.showAndWait();
        } else if (type.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("课程性质不能为空");
            alert.showAndWait();
        } else if (terms.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("学期不能为空");
            alert.showAndWait();
        } else if (capacity.getText() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("课容量不能为空");
            alert.showAndWait();
        } else if (precourse.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("前序课程不能为空");
            alert.showAndWait();
        } else {
            if(String.valueOf(precourse.getValue()).charAt(1) == '-'){
                map.put("pre_course_id", String.valueOf(precourse.getValue()).substring(0,1));
            } else if (String.valueOf(precourse.getValue()).charAt(3) == '-'){
                map.put("pre_course_id", String.valueOf(precourse.getValue()).substring(0,3));
            } else if (String.valueOf(precourse.getValue()).charAt(4) == '-') {
                map.put("pre_course_id", String.valueOf(precourse.getValue()).substring(0,4));
            }
            map.put("course_name", course_name.getText());
            map.put("credit", credit.getText());
            map.put("num", num.getText());
            map.put("course_type", (String) type.getValue());
            map.put("book", book.getText());
            map.put("terms", (String) terms.getValue());
            map.put("extracurricular", extra.getText());
            map.put("classes", classes.getText());
            map.put("teacher_name", teacher.getText());
            map.put("capacity", capacity.getText());
            map.put("students", "0");
            dataRequest.setData(map);
            Result data = HttpRequestUtils.courseField("/course/addCourse", dataRequest);
            String data1 = data.getMsg();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(data1);
            alert.showAndWait();
            CourseLessonController.num = num.getText();
            CourseLessonController.source = "add";
            if(data1.equals("添加成功")){
                try {
                    // 加载新的FXML文件
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("course-lesson.fxml"));
                    Parent root = fxmlLoader.load();
                    // 创建新的Stage
                    Stage newStage = new Stage();
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("添加课程具体信息界面");
                    newStage.setScene(new Scene(root));
                    newStage.setResizable(false);
                    newStage.initModality(Modality.APPLICATION_MODAL);
//                    Node node = add.getScene().getRoot();
//                    Window window = node.getScene().getWindow();
//                    window.hide(); // 关闭窗口
                    newStage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
