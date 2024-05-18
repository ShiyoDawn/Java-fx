package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class CourseLookHomeworkController {
    static String lesson_id;
    @FXML
    ImageView image11;
    @FXML
    ImageView image12;
    @FXML
    ImageView image13;
    @FXML
    public void initialize(){
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
