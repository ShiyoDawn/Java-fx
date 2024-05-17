package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDeliverHomeworkController {
    @FXML
    TextField road;
    @FXML
    Label homework;
    @FXML
    Button deliver;
    static String course_id;
    static String week;
    static String week_time;
    static String time_sort;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        road.setEditable(false);
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_id", course_id);
        map.put("week",week);
        map.put("week_time",week_time);
        map.put("time_sort",time_sort);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/lesson/selectSpecific", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        if(String.valueOf(dataList.get(0).get("homework")).equals("null") || String.valueOf(dataList.get(0).get("homework")).equals("")){
            homework.setText("老师还没留作业");
            deliver.setOnAction(actionEvent -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("老师还未留作业，暂不能提交");
                alert.showAndWait();
                Node node = deliver.getScene().getRoot();
                Window window = node.getScene().getWindow();
                window.hide(); // 关闭窗口
            });
        } else {
            homework.setText(String.valueOf(dataList.get(0).get("homework")));
        }

    }
    public void deliverC(){
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(deliver.getScene().getWindow());
        if (selectedFile != null) {
            // 在这里处理上传选定文件的逻辑
            road.setText(selectedFile.getAbsolutePath());
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择文件");
            alert.showAndWait();
        }

    }
    public void confirmC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_id", course_id);
        map.put("week",week);
        map.put("week_time",week_time);
        map.put("time_sort",time_sort);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/lesson/selectSpecific", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
    }
}
