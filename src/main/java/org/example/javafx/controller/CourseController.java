package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;


public class CourseController {
    @FXML
    AnchorPane pane;

    @FXML
    ComboBox selectType;
    @FXML
    Label id;
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
    Button add;
    @FXML
    Button delete;
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
    static List<StackPane> stackPanes = new ArrayList<>(6);

    //分页查询，根据查询数量自适应页码数量
    @FXML
    public void initialize() throws IOException, InterruptedException {
        load();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(new HashMap<>(), clickedPageIndex, "/course/selectAllByPage"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handlePageClick(Map map, int pageNum, String url) throws IOException, InterruptedException {
        deleteNode();
        DataRequest dataRequest = new DataRequest();
        map.put("pageNum", pageNum);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField(url, dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        addLabel(dataList);
    }

    private void deleteNode() {
        List<javafx.scene.Node> toRemove = new ArrayList<>();
        for (StackPane a : stackPanes) {
            a.getChildren().forEach(node -> {
                if (node.getId() == "course" && node instanceof Label) {
                    toRemove.add(node);
                }
            });
        }
        for (StackPane a : stackPanes) {
            for (Node b : toRemove) {
                a.getChildren().remove(b);
            }
        }
    }

    //初始，显示所有的课程
    private void load() throws IOException, InterruptedException {
        stackPanes.add(one);
        stackPanes.add(one);
        stackPanes.add(one);
        stackPanes.add(one);
        stackPanes.add(one);
        stackPanes.add(one);
        stackPanes.set(0,one);
        stackPanes.set(1,two);
        stackPanes.set(2,three);
        stackPanes.set(3,four);
        stackPanes.set(4,five);
        stackPanes.set(5,six);
        DataRequest dataRequest = new DataRequest();
        Result data = HttpRequestUtils.courseField("/course/selectAllType", dataRequest);
        List<Map<String, String>> dataListType = new Gson().fromJson(data.getData().toString(), List.class);
        for (Map<String, String> a : dataListType) {
            selectType.getItems().add(a.get("course_type_name"));
        }
        DataRequest dataRequest1 = new DataRequest();
        Result data1 = HttpRequestUtils.courseField("/course/selectAll", dataRequest1);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data1.getData().toString(), List.class);
        addLabel(dataList);
        course_name.setText((String) dataList.get(0).get("course_name"));
        num.setText((String) dataList.get(0).get("num"));
        type.setValue(dataList.get(0).get("course_type_name"));
        classes.setText((String) dataList.get(0).get("classes"));
        book.setText((String) dataList.get(0).get("book"));
        extra.setText((String) dataList.get(0).get("extracurricular"));
        teacher.setText((String) dataList.get(0).get("teacher_name"));
        credit.setText(String.valueOf(dataList.get(0).get("credit")));
        id.setText(String.valueOf(dataList.get(0).get("id")));
        setPagination(dataList);
    }

    private void addLabel(List<Map<String, ? extends Object>> dataList) throws IOException {
        int count = 0;
        for (Map<String, ? extends Object> a : dataList) {
            count++;
            Label label = new Label();
            label.setMaxSize(450, 30);
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(20));
            label.setText(a.get("course_name") + "                 " + a.get("classes") + "   " + a.get("teacher_name"));
            label.setLayoutX(37.0);
            label.setLayoutY(102 + (count - 1) * 80);
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) {  // 检查是否是单击事件
                    course_name.setText(a.get("course_name").toString());
                    num.setText(a.get("num").toString());
                    type.setValue(a.get("course_type_name").toString());
                    classes.setText(a.get("classes").toString());
                    book.setText(a.get("book").toString());
                    extra.setText(a.get("extracurricular").toString());
                    teacher.setText(a.get("teacher_name").toString());
                    credit.setText(String.valueOf(a.get("credit")));
                    id.setText(String.valueOf(a.get("id")));
                } else if (event.getClickCount() == 2) {
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("course-specific-view.fxml"));
                    try {
                        CourseSpecificViewController.id = String.valueOf(a.get("id"));
                        pane.getChildren().removeAll(pane.getChildren());
                        BorderPane pane1 = new BorderPane(fxmlLoader.load());
                        pane.getChildren().add(pane1);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            stackPanes.get(count - 1).getChildren().add(label);
            if (count % 6 == 0) {
                count = 0;
                break;
            }
        }
    }

    private void setPagination(List<Map<String, ? extends Object>> list) throws IOException, InterruptedException {
        if (list.size() == 0) {
            pagination.setPageCount(1);
        } else {
            pagination.setPageCount((list.size() % 6 == 0 ? (list.size() / 6) : ((list.size() / 6) + 1)));
        }
    }

    private Map<String, ? extends Object> selectData() {
        Map<String, String> map = new HashMap<>();
        String a = (String) selectTerms.getValue();
        if (a == null || a.equals("全部")) {
            map.put("terms", null);
        } else {
            map.put("terms", a);
        }
        String b = (String) selectType.getValue();
        if (b == null || b.equals("全部")) {
            map.put("course_type", null);
        } else {
            map.put("course_type", b);
        }
        String c = inputName.getText();
        if (c == null) {
            map.put("course_name", null);
        } else {
            map.put("course_name", c);
        }
        String d = classNum.getText();
        if (d == null) {
            map.put("classNum", null);
        } else {
            map.put("classNum", d);
        }
        String e = (String) selectClass.getValue();
        if (e == null || e.equals("全部")) {
            map.put("classes", null);
        } else {
            map.put("classes", e);
        }
        return map;
    }

    @FXML
    public void select() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, ? extends Object> map = selectData();
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectSpecial", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        if (dataList.size() == 0) {
            Label label = new Label("暂无课程");
            label.setMaxSize(450, 30);
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(20));
            label.setLayoutX(37.0);
            label.setLayoutY(102);
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            stackPanes.get(0).getChildren().add(label);
        }
        deleteNode();
        pagination.setCurrentPageIndex(0);
        deleteNode();
        setPagination(dataList);
        addLabel(dataList);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectSpecial"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @FXML
    public void saveInfo() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        if (course_name.getText() == null) ;
        map.put("course_name", course_name.getText());
        map.put("credit", credit.getText());
        map.put("num", num.getText());
        map.put("course_type", (String) type.getValue());
        map.put("book", book.getText());
        map.put("extracurricular", extra.getText());
        map.put("id", id.getText());
        map.put("classes", classes.getText());
        map.put("teacher", teacher.getText());
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/updateInfo", dataRequest);
        String data1 = data.getMsg();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(data1);
        alert.showAndWait();
        //刷新
        int a = pagination.getCurrentPageIndex();
        if (a == 0) {
            pagination.setCurrentPageIndex(1);
            pagination.setCurrentPageIndex(0);
        } else {
            pagination.setCurrentPageIndex(0);
            pagination.setCurrentPageIndex(a);
        }
    }

    @FXML
    public void deleteCourse() throws IOException, InterruptedException {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setContentText("您是否要删除该课程");
        // 设置对话框的按钮类型
        ButtonType buttonTypeOK = new ButtonType("确定");
        ButtonType buttonTypeCancel = new ButtonType("取消");
        alert.getButtonTypes().setAll(buttonTypeOK, buttonTypeCancel);
        // 显示弹窗并等待按钮点击事件
        Optional<ButtonType> result = alert.showAndWait();
        // 处理按钮点击事件
        if (result.isPresent() && result.get() == buttonTypeOK) {
            DataRequest dataRequest = new DataRequest();
            Map<String, String> map = new HashMap<>();
            map.put("id", id.getText());
            dataRequest.setData(map);
            Result data = HttpRequestUtils.courseField("/course/deleteCourse", dataRequest);
            String data1 = data.getMsg();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setHeaderText(data1);
            alert1.showAndWait();
        } else {
            alert.close();
        }
        int a = pagination.getCurrentPageIndex();
        if (a == 0) {
            pagination.setCurrentPageIndex(1);
            pagination.setCurrentPageIndex(0);
        } else {
            pagination.setCurrentPageIndex(0);
            pagination.setCurrentPageIndex(a);
        }

    }

    @FXML
    public void addCourse() throws IOException, InterruptedException {
        try {
            // 加载新的FXML文件
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource("course-add-view.fxml"));
            Parent root = fxmlLoader.load();
            // 创建新的Stage
            Stage newStage = new Stage();
            newStage.initStyle(StageStyle.DECORATED);
            newStage.setTitle("添加课程界面");
            newStage.setScene(new Scene(root));
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
