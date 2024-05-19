package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class CourseHomeworkController {
    static String lesson_id;
    static String week;
    static String week_time;
    static String time_sort;
    static String course_id;
    @FXML
    TableView tableView;
    @FXML
    CheckBox only;
    @FXML
    Button select;
    @FXML
    TableColumn idColumn;
    @FXML
    TableColumn person_numColumn;
    @FXML
    TableColumn student_nameColumn;
    @FXML
    TableColumn classes;
    @FXML
    TableColumn diliver;
    @FXML
    TableColumn time;
    @FXML
    TableColumn rank;
    @FXML
    TableColumn attend;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        DataRequest dataRequest5 = new DataRequest();
        Map<String, String> map5 = new HashMap<>();
        map5.put("course_id", course_id);
        map5.put("week", week);
        map5.put("week_time", week_time);
        map5.put("time_sort", time_sort);
        dataRequest5.setData(map5);
        Result data5 = HttpRequestUtils.courseField("/lesson/selectSpecific", dataRequest5);
        List<Map<String, ? extends Object>> dataList5 = new Gson().fromJson(data5.getData().toString(), List.class);
        lesson_id = String.valueOf(dataList5.get(0).get("id"));
        idColumn.setCellValueFactory(new MapValueFactory<>("student_id"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        classes.setCellValueFactory(new MapValueFactory<>("classes"));
        diliver.setCellValueFactory(new MapValueFactory<>("diliver"));
        time.setCellValueFactory(new MapValueFactory<>("time"));
        rank.setCellValueFactory(new MapValueFactory<>("rank"));
//        attend.setCellValueFactory(new MapValueFactory<>("attend"));
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("id", String.valueOf(CourseSpecificViewController.id));
        map.put("lesson_id", lesson_id);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectStudentCourse2", dataRequest);
        AtomicReference<List<Map>> dataList = new AtomicReference<>(new Gson().fromJson(data.getData().toString(), List.class));
        //改科学计数法
        for (Map<String, String> a : dataList.get()) {
            BigDecimal bigDecimal = new BigDecimal(String.valueOf(a.get("person_num")));
            String originalNumber = bigDecimal.toPlainString();
            a.put("person_num", originalNumber);
        }
        tableView.setItems(FXCollections.observableList(dataList.get()));
        only.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) {
                dataList.set(CommonMethod.filter(dataList.get(), "diliver", "已提交"));
                tableView.setItems(FXCollections.observableList(dataList.get()));
            } else {
                try {
                    initialize();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void selectC() throws IOException, InterruptedException {
        Map<String, String> map = new HashMap<>();
        Map<String, String> selectedItem = (Map<String, String>) tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            if (selectedItem.get("diliver").equals("已提交")) {
                try {
                    CourseTeacherLookController.student_id = String.valueOf(selectedItem.get("student_id"));
                    CourseTeacherLookController.lesson_id = lesson_id;
                    // 加载新的FXML文件
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("course-teacher-look.fxml"));
                    Parent root = fxmlLoader.load();
                    // 创建新的Stage
                    Stage newStage = new Stage();
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("查看作业");
                    newStage.setScene(new Scene(root));
                    newStage.setResizable(false);
                    newStage.show();
                    Node node = select.getScene().getRoot();
                    Window window = node.getScene().getWindow();
                    window.hide(); // 关闭窗口
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("该学生还未提交作业");
                alert.showAndWait();
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择一个学生");
            alert.showAndWait();
        }

    }
}
