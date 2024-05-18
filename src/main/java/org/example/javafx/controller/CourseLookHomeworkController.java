package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseLookHomeworkController {
    static String lesson_id;
    @FXML
    ImageView image11;
    @FXML
    Label rank;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("student_id",idC(DashboardController.student_id));
        map.put("lesson_id",idC(lesson_id));
        dataRequest.setData(map);
        String str = HttpRequestUtils.request("/lesson/getPhoto", dataRequest).getData().toString();
        byte[] data = Base64.getDecoder().decode(str);
        if (data != null) {
            Image image1 = new Image(new ByteArrayInputStream(data));
            image11.setImage(image1);
        }
        DataRequest dataRequest1 = new DataRequest();
        Map<String, String> map1 = new HashMap<>();
        map1.put("student_id", DashboardController.student_id);
        map1.put("lesson_id",String.valueOf(lesson_id));
        dataRequest.setData(map);
        Result data1 = HttpRequestUtils.courseField("/lesson/selectStudentLesson", dataRequest);
        List<Map<String, String>> dataList1 = new Gson().fromJson(data1.getData().toString(), List.class);
        if(String.valueOf(dataList1.get(0).get("homework_rank")).equals("null")){
            rank.setText("作业评级：" + "老师还未评级");
        }else {
            rank.setText("作业评级：" + String.valueOf(dataList1.get(0).get("homework_rank")));
        }

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
