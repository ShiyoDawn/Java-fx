package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.OptionItem;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ScoreTableController {
    //显示元素

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private ComboBox courseComboBox;


    @FXML
    private TableColumn<Map, String> courseNameColumn;

    @FXML
    private TableColumn<Map, String> courseNumColumn;

    @FXML
    private TableColumn<Map, String> creditColumn;

    @FXML
    private TableColumn<Map, String> id;

    @FXML
    private TableColumn<Map, String> markColumn;

    @FXML
    private TableColumn<Map, String> operateColumn;

    @FXML
    private ComboBox studentComboBox;

    @FXML
    private TableColumn<Map, String> studentNameColumn;

    @FXML
    private TableColumn<Map, String> studentNumColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button queryButton;
    @FXML
    private Button resetButton;

    @FXML
    private AnchorPane scoreAnchorPane;

    @FXML
    private BorderPane scoreBorderPane;


    //------------------------------------------------------------
    private List courseList;
    private List studentList;

    private List scoreList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    public List getStudentList() {
        return studentList;
    }

    public List getCourseList() {
        return courseList;
    }

    private ScoreEditController scoreEditController = null;

    private ScoreTableController scoreTableController = null;
    private Stage stage = null;

    //------------------------------------------------------------
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Stage editStage = new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent = fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("增添学生分数");
            editStage.show();
            //scoreEditController.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showEditStage();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Stage editStage = new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent = fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("删除学生分数");
            editStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showEditStage();
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        Stage editStage = new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent = fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("修改学生分数");
            editStage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        showEditStage();
    }

    @FXML
    private void onQueryButtonClick() {
        String student_name = null;
        String course_name = null;
        Object student = studentComboBox.getSelectionModel().getSelectedItem();
        Object course = courseComboBox.getSelectionModel().getSelectedItem();
        Result result = null;
        if (student != null)
            student_name = student.toString();

        if (course != null)
            course_name = course.toString();

        if(student_name == "请选择学生"){
            studentComboBox.setValue("请选择学生");
            student_name = null;
        }
        if(course_name == "请选择课程"){
            courseComboBox.setValue("请选择课程");
            course_name = null;
        }

        System.out.println(student_name+" "+course_name);
        if (student_name != null && course_name != null)
        {
            DataRequest stuDataRequest = new DataRequest();
            stuDataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
            Map map = (Map) result.getData();
            Double student_id = Double.parseDouble(map.get("id").toString());

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("course_name", course_name);
            result = HttpRequestUtils.request("/course/selectCourseByName", courDataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
            map = (Map) result.getData();
            Double course_id = Double.parseDouble(map.get("id").toString());

            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_id", student_id);
            dataRequest.add("course_id", course_id);
            result = HttpRequestUtils.request("/score/selectByStudentAndCourse", dataRequest);
            if(result==null){
                observableList.clear();
                return;
            }

        } else if (student_name != null && course_name == null)
        {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_name",student_name);
            result = HttpRequestUtils.request("/score/selectByStudentName",dataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
        }
        else if(student_name == null && course_name != null)
        {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("course_name",course_name);
            result = HttpRequestUtils.request("/score/selectByCourseName",dataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
        }
        else if(student_name == null && course_name == null)
        {
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        }
        if(result==null){
            return;
        }
        setTableViewData(result);
    }

    @FXML
    private void onResetButtonClick(){
        Result result=null;
        studentComboBox.setValue("请选择学生");
        courseComboBox.setValue("请选择课程");
        result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        setTableViewData(result);
    }

    //显示成绩总表
    private void setTableViewData(Result result) {
        observableList.clear();
        if (result.getData() instanceof Map) {
            Map scoreMap = (Map) result.getData();
            Button editButton;
            Integer index = 1;
            editButton = new Button("编辑");
            editButton.setId("edit" + index);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            scoreMap.put("edit", editButton);
            observableList.addAll(FXCollections.observableArrayList(scoreMap));
        } else if (result.getData() instanceof ArrayList) {
            Button editButton;
            Integer index = 1;
            scoreList = (ArrayList) result.getData();
            for (Map scoremap : (ArrayList<Map>) scoreList) {
                index++;
                editButton = new Button("编辑");
                editButton.setId("edit" + index);
                editButton.setOnAction(e -> {
                    editItem(((Button) e.getSource()).getId());
                });
                scoremap.put("edit", editButton);
                observableList.addAll(FXCollections.observableArrayList(scoremap));
            }
        }
        dataTableView.setItems(observableList);
    }

    //在每个“分数”后面加上编辑按钮
    public void editItem(String name) {
        if (name == null)
            return;
        int j = Integer.parseInt(name.substring(4, name.length()));
        //Map data = scoreList.get();
        showEditStage();
        //scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if (!"ok".equals(cmd))
            return;
        Result res;
        Integer studentId = CommonMethod.getInteger(data, "student_id");
        if (studentId == null) {
            return;
        }
        Integer courseId = CommonMethod.getInteger(data, "course_id");
        if (courseId == null) {
            return;
        }
        DataRequest req = new DataRequest();
        req.add("student_id", studentId);
        req.add("course_id", courseId);
        req.add("score_id", CommonMethod.getInteger(data, "score_id"));
        req.add("mark", CommonMethod.getInteger(data, "mark"));
        System.out.println(req.getData());
        res = HttpRequestUtils.request("/score/scoreSave", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            onQueryButtonClick();
        }
    }

    public void showEditStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("score-edit.fxml"));
            Stage stage = new Stage();
            Scene scene = null;

            scene = new Scene(fxmlLoader.load(), 350, 260);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("成绩已录入对话框！");
            stage.setOnCloseRequest(event -> {
                MainApplication.setCanClose(true);
            });
            scoreEditController = (ScoreEditController) fxmlLoader.getController();
            scoreEditController.setScoreTableController(this);
            scoreEditController.initialize();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    public void initialize() {
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_id"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("course_id"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("course_name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));

        DataRequest req = new DataRequest();
        List studentList = new ArrayList();
        List courseList = new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req); //从后台获取所有学生信息列表集合
        Result courseResult = HttpRequestUtils.request("/course/selectAll", req); //从后台获取所有学生信息列表集合

        Map cancelStudent=new HashMap();
        cancelStudent.put("cancelStudent","请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        Map cancelCourse=new HashMap();
        cancelCourse.put("cancelCourse","请选择课程");
        courseList.add(cancelCourse.get("cancelCourse"));

        List<Map> studentMap = (List<Map>) studentResult.getData();
        List<Map> courseMap = (List<Map>) courseResult.getData();
        for (Map student : studentMap) {
            studentList.add(student.get("student_name"));
        }
        for (Map course : courseMap) {
            courseList.add(course.get("course_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        Result result;
        result = HttpRequestUtils.request("/score/getScoreList", new DataRequest()); //从后台获取所有学生信息列表集合
        setTableViewData(result);
    }

}
