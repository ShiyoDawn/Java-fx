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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseTeacherLookController {
    static String lesson_id;
    static String student_id;
    @FXML
    ImageView image11;
    @FXML
    ComboBox rank;
    @FXML
    Button deliver;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        rank.setValue("A+");
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("student_id",idC(student_id));
        map.put("lesson_id",idC(lesson_id));
        dataRequest.setData(map);
        String str = HttpRequestUtils.request("/lesson/getPhoto", dataRequest).getData().toString();
        byte[] data = Base64.getDecoder().decode(str);
        if (data != null) {
            Image image1 = new Image(new ByteArrayInputStream(data));
            image11.setImage(image1);
        }
    }
    public void deliverC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("student_id",idC(student_id));
        map.put("lesson_id",idC(lesson_id));
        map.put("homework_rank",String.valueOf(rank.getValue()));
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/lesson/updateHomeworkRank", dataRequest);
        if(data.getCode() == 200){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("提交成功");
            alert.showAndWait();
            Node node = rank.getScene().getRoot();
            Window window = node.getScene().getWindow();
            window.hide(); // 关闭窗口
            try {
                // 加载新的FXML文件
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(MainApplication.class.getResource("course-homework.fxml"));
                Parent root = fxmlLoader.load();
                // 创建新的Stage
                Stage newStage = new Stage();
                newStage.initStyle(StageStyle.DECORATED);
                newStage.setTitle("查看作业/考勤");
                newStage.setScene(new Scene(root));
                newStage.setResizable(false);
                newStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String idC(String str){
        if(str == null){
            return str;
        }else {
            int count = str.length();
            for (int i = 0; i < str.length(); i++) {
                if(str.charAt(i) == '.'){
                    count = i;
                }
            }
            return str.substring(0,count);
        }

    }
}
