package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CourseAddController {
    @FXML
    Button add;
    @FXML
    TextField course_name;
    @FXML
    TextField num;
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
        } else {
            map.put("course_name", course_name.getText());
            map.put("credit", credit.getText());
            map.put("num", num.getText());
            map.put("course_type", (String) type.getValue());
            map.put("book", book.getText());
            map.put("terms", (String) terms.getValue());
            map.put("extracurricular", extra.getText());
            map.put("classes", classes.getText());
            map.put("teacher_name", teacher.getText());
            dataRequest.setData(map);
            Result data = HttpRequestUtils.courseField("/course/addCourse", dataRequest);
            String data1 = data.getMsg();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(data1);
            alert.showAndWait();
        }
    }

}
