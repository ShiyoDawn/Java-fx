package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CourseLessonHomeworkController {
    static String course_id;
    static String week;
    static String week_time;
    static String time_sort;
    @FXML
    TextField textField;
    @FXML
    DatePicker ddl;
    @FXML
    Button save;
    @FXML
    public void initialize(){
        ddl.setEditable(false);
    }
    public void saveC(){
        Map<String,String> map = new HashMap<>();
        map.put("course_id",course_id);
        map.put("week", String.valueOf(week));
        map.put("week_time",String.valueOf(week_time));
        map.put("time_sort",String.valueOf(time_sort));
        map.put("homework",textField.getText());
        map.put("ddl", String.valueOf(ddl.getValue()));
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("添加成功");
                alert.showAndWait();
                Node node = save.getScene().getRoot();
                Window window = node.getScene().getWindow();
                window.hide(); // 关闭窗口
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
