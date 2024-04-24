package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CourseController {

    @FXML
    ComboBox selectType;
    @FXML
    ComboBox selectTerms;
    @FXML
    Button selectCourse;
    @FXML
    TextField inputName;
    @FXML
    AnchorPane tabCenter;
    @FXML
    TextField course_name;
    @FXML
    TextField num;
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
    Button save;
    @FXML
    StackPane one;
    @FXML
    StackPane two;
    @FXML
    StackPane three;
    @FXML
    StackPane four;
    @FXML
    StackPane five;
    @FXML
    StackPane six;
    @FXML
    TextField credit;
    @FXML
    Pagination pagination;
    static List<StackPane> stackPanes = new ArrayList<>();
    @FXML
    public void initialize() throws IOException, InterruptedException {
        load();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(clickedPageIndex); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
    @FXML
    public void saveInfo(){
        DataRequest dataRequest = new DataRequest();

    }
    private void handlePageClick(int pageNum) throws IOException, InterruptedException {
        List<javafx.scene.Node> toRemove = new ArrayList<>();
        for (StackPane a:stackPanes) {
            a.getChildren().forEach(node -> {
                if (node.getId() == "course" && node instanceof Label) {
                    toRemove.add(node);
                }
            });
        }
        for (StackPane a:stackPanes) {
            for (Node b : toRemove) {
                a.getChildren().remove(b);
            }
        }
        DataRequest dataRequest = new DataRequest();
        Map<String,Integer> map = new HashMap<>();
        map.put("pageNum",pageNum);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectAllByPage", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        int count = 0;
        for(Map<String, ? extends Object> a : dataList){
            count++;
            Label label = new Label();
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(20));
            label.setText(a.get("course_name") + "                 " + a.get("classes") + "   " + a.get("teacher_name"));
            label.setLayoutX(37.0);
            label.setLayoutY(102+(count-1)*80);
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {  // 检查是否是单击事件
                    course_name.setText((String) a.get("course_name"));
                    num.setText((String) a.get("num"));
                    type.setValue((String)a.get("course_type_name"));
                    classes.setText((String) a.get("classes"));
                    book.setText((String) a.get("book"));
                    extra.setText((String) a.get("extracurricular"));
                    teacher.setText((String) a.get("teacher_name"));
                    credit.setText(String.valueOf( a.get("credit")));
                }
            });
            stackPanes.get(count - 1).getChildren().add(label);
            if(count % 6 == 0){
                break;
            }
        }
    }
    private void load() throws IOException, InterruptedException {
        stackPanes.add(one);
        stackPanes.add(two);
        stackPanes.add(three);
        stackPanes.add(four);
        stackPanes.add(five);
        stackPanes.add(six);
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("course-view.fxml"));
        DataRequest dataRequest = new DataRequest();
        DataRequest dataRequest1 = new DataRequest();
        Result data = HttpRequestUtils.courseField("/course/selectAllType", dataRequest);
        Result dataCou = HttpRequestUtils.courseField("/course/selectAll", dataRequest1);
        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        List<Map<String, ? extends Object>> dataListCou = new Gson().fromJson(dataCou.getData().toString(), List.class);
        for(Map<String, String> a : dataList){
            selectType.getItems().add(a.get("course_type_name"));
        }
        int count = 0;
        for(Map<String, ? extends Object> a : dataListCou){
            count++;
            Label label = new Label();
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(20));
            label.setText(a.get("course_name") + "                 " + a.get("classes") + "   " + a.get("teacher_name"));
            label.setLayoutX(37.0);
            label.setLayoutY(102+(count-1)*80);
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {  // 检查是否是单击事件
                    course_name.setText((String) a.get("course_name"));
                    num.setText((String) a.get("num"));
                    type.setValue((String)a.get("course_type_name"));
                    classes.setText((String) a.get("classes"));
                    book.setText((String) a.get("book"));
                    extra.setText((String) a.get("extracurricular"));
                    teacher.setText((String) a.get("teacher_name"));
                    credit.setText(String.valueOf( a.get("credit")));
                }
            });
            stackPanes.get(count - 1).getChildren().add(label);
            if(count % 6 == 0){
                count = 0;
                break;
            }
        }
        pagination.setPageCount((dataListCou.size() % 6 == 0 ? (dataListCou.size() / 6) : ((dataListCou.size() / 6) + 1)));
    }


}
