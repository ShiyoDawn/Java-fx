package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Window;
import javafx.util.StringConverter;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.awt.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
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
    public void initialize() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String,String> map = new HashMap<>();
        map.put("course_id",course_id);
        map.put("week", String.valueOf(week));
        map.put("week_time",String.valueOf(week_time));
        map.put("time_sort",String.valueOf(time_sort));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/lesson/selectSpecific", dataRequest);
        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        if(String.valueOf(dataList.get(0).get("homework")).equals("null")){
            textField.setText("您还未布置作业");
        } else {
            textField.setText(String.valueOf(dataList.get(0).get("homework")));
        }
        //添加初始值
        if(!(String.valueOf(dataList.get(0).get("ddl")).equals("null"))){
            StringConverter<LocalDate> converter = new StringConverter<>() {
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                @Override
                public String toString(LocalDate date) {
                    if (date != null) {
                        return dateFormatter.format(date);
                    } else {
                        return "";
                    }
                }

                @Override
                public LocalDate fromString(String string) {
                    if (string != null && !string.isEmpty()) {
                        return LocalDate.parse(string, dateFormatter);
                    } else {
                        return null;
                    }
                }
            };
            // 将自定义的StringConverter设置给DatePicker
            ddl.setConverter(converter);

            // 将字符串时间转换为LocalDate对象
            String dateString = String.valueOf(dataList.get(0).get("ddl"));
            LocalDate date = LocalDate.parse(dateString);

            // 将LocalDate对象设置为DatePicker的初始值
            ddl.setValue(date);
        }
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
