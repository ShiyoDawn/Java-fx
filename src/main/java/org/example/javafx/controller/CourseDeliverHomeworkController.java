package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.example.javafx.AppStore;
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
    @FXML
    Button look;
    @FXML
    ComboBox combobox;
    static String course_id;
    static String week;
    static String week_time;
    static String time_sort;
    static String lesson_id;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        combobox.setValue("第1页");
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
            lesson_id = String.valueOf(dataList.get(0).get("id"));
            homework.setText(String.valueOf(dataList.get(0).get("homework")));
        }

    }
    public void deliverC(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(deliver.getScene().getWindow());
        if (selectedFile != null) {
            // 在这里处理上传选定文件的逻辑
            road.setText(selectedFile.getAbsolutePath());
            Result data = HttpRequestUtils.uploadFile("/user/uploadPhoto", selectedFile.getPath(), "homework", idC(DashboardController.student_id) +"a"+ idC(lesson_id) + "a"+ String.valueOf(combobox.getValue()).charAt(1));
            if(data.getMsg().equals("上传成功")){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("上传成功");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择文件");
            alert.showAndWait();
        }

    }
    public void lookC() throws IOException, InterruptedException {
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
    private String idC(String str){
        int count = str.length();
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '.'){
                count = i;
            }
        }
        return str.substring(0,count);
    }
}
