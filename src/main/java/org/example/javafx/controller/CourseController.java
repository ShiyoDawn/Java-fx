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

import javax.security.auth.callback.TextOutputCallback;
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
    AnchorPane pane;

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
    @FXML
    TextField classNum;
    @FXML
    ComboBox selectClass;
    static List<StackPane> stackPanes = new ArrayList<>();
    //分页查询，根据查询数量自适应页码数量
    @FXML
    public void initialize() throws IOException, InterruptedException {
        load();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(new HashMap<>(),clickedPageIndex,"/course/selectAllByPage"); // 处理点击事件
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
    private void handlePageClick(Map map,int pageNum,String url) throws IOException, InterruptedException {
        deleteNode();
        DataRequest dataRequest = new DataRequest();
        map.put("pageNum",pageNum);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField(url, dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        addLabel(dataList);
    }
    private void deleteNode(){
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
    }
    //初始，显示所有的课程
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
        Result data = HttpRequestUtils.courseField("/course/selectAllType", dataRequest);
        List<Map<String, String>> dataListType = new Gson().fromJson(data.getData().toString(), List.class);
        for(Map<String, String> a : dataListType){
            selectType.getItems().add(a.get("course_type_name"));
        }
        DataRequest dataRequest1 = new DataRequest();
        Result data1 = HttpRequestUtils.courseField("/course/selectAll", dataRequest1);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data1.getData().toString(), List.class);
        addLabel(dataList);
        setPagination(dataList);
    }

    private void addLabel(List<Map<String, ? extends Object>> dataList) throws IOException{
        int count = 0;
        for(Map<String, ? extends Object> a : dataList){
            count++;
            Label label = new Label();
            label.setMaxSize(450,30);
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(20));
            label.setText(a.get("course_name") + "                 " + a.get("classes") + "   " + a.get("teacher_name"));
            label.setLayoutX(37.0);
            label.setLayoutY(102+(count-1)*80);
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {  // 检查是否是单击事件
                    course_name.setText((String) a.get("course_name"));
                    num.setText((String) a.get("num"));
                    type.setValue(a.get("course_type_name"));
                    classes.setText((String) a.get("classes"));
                    book.setText((String) a.get("book"));
                    extra.setText((String) a.get("extracurricular"));
                    teacher.setText((String) a.get("teacher_name"));
                    credit.setText(String.valueOf( a.get("credit")));
                } else if (event.getClickCount() == 2) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("course-specific-view.fxml"));
                    try {
                        pane.getChildren().removeAll(pane.getChildren());

                        BorderPane pane1 = new BorderPane(fxmlLoader.load());
                        pane.getChildren().add(pane1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            stackPanes.get(count - 1).getChildren().add(label);
            if(count % 6 == 0){
                count = 0;
                break;
            }
        }
    }

    private void setPagination(List<Map<String,? extends Object>> list) throws IOException, InterruptedException {
        pagination.setPageCount((list.size() % 6 == 0 ? (list.size() / 6) : ((list.size() / 6) + 1)));
    }
    private Map<String, ? extends Object> selectData() {
        Map<String, String> map = new HashMap<>();
        String a = (String) selectTerms.getValue();
        if (a == null || a.equals("全部")) {
            map.put("terms",null);
        } else {
            map.put("terms", a);
        }
        String b = (String) selectType.getValue();
        if (b == null || b.equals("全部")) {
            map.put("course_type",null);
        } else {
            map.put("course_type",b);
        }
        String c = inputName.getText();
        if (c == null) {
            map.put("course_name",null);
        } else {
            map.put("course_name",c);
        }
        String d = classNum.getText();
        if (d == null) {
            map.put("classNum",null);
        } else {
            map.put("classNum",d);
        }
        String e = (String) selectClass.getValue();
        if (e == null || e.equals("全部")) {
            map.put("classes",null);
        } else {
            map.put("classes",e);
        }
        return map;
    }
    @FXML
    public void select() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String,? extends Object> map = selectData();
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectSpecial", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        deleteNode();
        setPagination(dataList);
        addLabel(dataList);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(map,clickedPageIndex,"/course/selectSpecial"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
