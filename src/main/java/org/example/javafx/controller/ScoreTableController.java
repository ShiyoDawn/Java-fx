package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
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

public class ScoreTableController  {
    //显示元素

    @FXML
    private TableView<Map> dataTableView ;

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
    private TableColumn<Map, Button> operateColumn;

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

    private List<Map> scoreList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表


    private ScoreEditController scoreEditController = null;

    public static ScoreTableController scoreTableController;

    private Stage stage = null;

    //------------------------------------------------------------
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Stage editStage = new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            URL url = getClass().getResource("/org/example/javafx/score-edit-add.fxml");
            fxmlLoader.setLocation(url);
            Parent parent = fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("增添学生分数");
            editStage.show();
            //scoreEditController.initialize();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //onResetButtonClick();
        showEditStage();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Map selected=dataTableView.getSelectionModel().getSelectedItem();
        if(selected==null){
            Stage editStage = new Stage();
            //取消放大（全屏）按钮
            editStage.setResizable(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/org/example/javafx/score-edit-delete.fxml");
                fxmlLoader.setLocation(url);
                Parent parent = fxmlLoader.load();
                editStage.setScene(new Scene(parent));
                editStage.setTitle("删除学生分数");
                editStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //showEditStage();
            return;
        }
        //System.out.println(selected);
        //onResetButtonClick();
        Integer student_id=CommonMethod.getInteger(selected,"student_id");
        Integer course_id=CommonMethod.getInteger(selected,"course_id");
        System.out.println(student_id+" "+course_id);
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("student_id",student_id);
        dataRequest.add("course_id",course_id);
        CommonMethod.alertButton("/score/deleteAllById",dataRequest,"删除");
        onQueryButtonClick();
        onQueryButtonClick();
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        Map selected=dataTableView.getSelectionModel().getSelectedItem();
        if(selected==null){
            Stage editStage = new Stage();
            //取消放大（全屏）按钮
            editStage.setResizable(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/org/example/javafx/score-edit-update.fxml");
                fxmlLoader.setLocation(url);
                Parent parent = fxmlLoader.load();
                editStage.setScene(new Scene(parent));
                editStage.setTitle("修改学生分数");
                editStage.show();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //onResetButtonClick();
            showEditStage();
            return;
        }
        DataRequest dataRequest=new DataRequest();
        Integer student_id=CommonMethod.getInteger(selected,"student_id");
        Integer course_id=CommonMethod.getInteger(selected,"course_id");
        dataRequest.add("student_id",student_id);
        dataRequest.add("course_id",course_id);
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
            Integer student_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("course_name", course_name);
            result = HttpRequestUtils.request("/course/selectCourseByName", courDataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
            map = (Map) result.getData();
            Integer course_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

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
    public void onResetButtonClick(){
        Result result=null;
        studentComboBox.setValue("请选择学生");
        courseComboBox.setValue("请选择课程");
        result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        setTableViewData(result);
    }

    //显示成绩总表
    public void setTableViewData(Result result) {
        observableList.clear();
        int index=1;
        if (result.getData() instanceof Map) {
            Map scoreMap = (Map) result.getData();
            Button editButton;
            editButton = new Button("编辑");
            editButton.setId("edit"+index);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            index++;
            scoreMap.put("operateColumn", editButton);
            observableList.add(scoreMap);
        } else if (result.getData() instanceof ArrayList) {
            Button editButton;
            scoreList = (ArrayList) result.getData();
            for (Map scoremap : (ArrayList<Map>) scoreList) {
                System.out.println(scoremap);
                editButton = new Button("编辑");
                editButton.setId("edit"+index);
                editButton.setOnAction(e -> {
                    editItem(((Button) e.getSource()).getId());
                });
                scoremap.put("operateColumn", editButton);
                observableList.add(scoremap);
                index++;
            }
        }
        dataTableView.setItems(observableList);
    }

    //在每个“分数”后面加上编辑按钮
    public void editItem(String name) {
        if (name == null)
            return;
        int index = Integer.parseInt(name.substring(4, name.length()));
        Map data = scoreList.get(index);
        showEditStage();
        scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        //stage.close();
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
        req.add("id", CommonMethod.getInteger(data, "id"));
        req.add("mark", CommonMethod.getInteger(data, "mark"));
        System.out.println(req.getData());
        res = HttpRequestUtils.request("/score/getScoreList", req); //从后台获取所有学生信息列表集合
        if (res != null && res.getCode() == 0) {
            onQueryButtonClick();
        }
    }

    public void showEditStage() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("score-edit-add.fxml"));
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
            scoreEditController.initialize();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @FXML
    public void initialize() {
        System.out.println("check");
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
        onQueryButtonClick();
    }
}
