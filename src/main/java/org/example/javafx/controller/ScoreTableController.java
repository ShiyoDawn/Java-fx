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
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreTableController {
    //显示元素

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private ComboBox<OptionItem> courseComboBox;


    @FXML
    private TableColumn<Map,String> courseNameColumn;

    @FXML
    private TableColumn<Map,String> courseNumColumn;

    @FXML
    private TableColumn<Map,String> creditColumn;

    @FXML
    private TableColumn<Map,String> id;

    @FXML
    private TableColumn<Map,String> markColumn;

    @FXML
    private TableColumn<Map,String> rankingColumn;

    @FXML
    private ComboBox<OptionItem> studentComboBox;

    @FXML
    private TableColumn<Map,String> studentNameColumn;

    @FXML
    private TableColumn<Map,String> studentNumColumn;

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button queryButton;

    @FXML
    private AnchorPane scoreAnchorPane;

    @FXML
    private BorderPane scoreBorderPane;


    //------------------------------------------------------------
    private List<OptionItem> courseList;
    private List<OptionItem> studentList;

    private List<Map> scoreList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList= FXCollections.observableArrayList();  // TableView渲染列表
    public List<OptionItem> getStudentList() {
        return studentList;
    }
    public List<OptionItem> getCourseList() {
        return courseList;
    }

    private ScoreEditController scoreEditController=null;

    private ScoreTableController scoreTableController=null;
    private Stage stage=null;

    //------------------------------------------------------------
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        Stage editStage=new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try{
            FXMLLoader fxmlLoader=new FXMLLoader();
            URL url=getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent=fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("增添学生分数");
            editStage.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        Stage editStage=new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try{
            FXMLLoader fxmlLoader=new FXMLLoader();
            URL url=getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent=fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("删除学生分数");
            editStage.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        Stage editStage=new Stage();
        //取消放大（全屏）按钮
        editStage.setResizable(false);
        try{
            FXMLLoader fxmlLoader=new FXMLLoader();
            URL url=getClass().getResource("/org/example/javafx/score-edit.fxml");
            fxmlLoader.setLocation(url);
            Parent parent=fxmlLoader.load();
            editStage.setScene(new Scene(parent));
            editStage.setTitle("修改学生分数");
            editStage.show();
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void onQueryButtonClick(){
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem optionItem;
        optionItem = studentComboBox.getSelectionModel().getSelectedItem();
        if(optionItem != null)
            studentId = Integer.parseInt(optionItem.getValue());
        optionItem = courseComboBox.getSelectionModel().getSelectedItem();
        if(optionItem != null)
            courseId = Integer.parseInt(optionItem.getValue());
        Result result;
        DataRequest dataRequest =new DataRequest();
        dataRequest.add("student_id",studentId);
        dataRequest.add("course_id",courseId);
        System.out.println(dataRequest.getData());
        result = HttpRequestUtils.request("/score/getScoreList",dataRequest); //从后台获取所有学生信息列表集合
        setTableViewData(result);
    }

    //显示成绩总表
    private void setTableViewData(Result result) {
        observableList.clear();
        scoreList=(List<Map>) result.getData();
        Map map;
        Button editButton;
        Integer index=0;
        for (Map scoremap:scoreList) {
            index++;
            editButton = new Button("编辑");
            editButton.setId("edit"+index);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            scoremap.put("edit",editButton);
            observableList.addAll(FXCollections.observableArrayList(scoremap));
        }
        dataTableView.setItems(observableList);
    }

    //在每个“分数”后面加上编辑按钮
    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = scoreList.get(j);
        showEditStage();
        //scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        Result res;
        Integer studentId = CommonMethod.getInteger(data,"student_id");
        if(studentId == null) {
            return;
        }
        Integer courseId = CommonMethod.getInteger(data,"course_id");
        if(courseId == null) {
            return;
        }
        DataRequest req =new DataRequest();
        req.add("student_id",studentId);
        req.add("course_id",courseId);
        req.add("score_id",CommonMethod.getInteger(data,"score_id"));
        req.add("mark",CommonMethod.getInteger(data,"mark"));
        System.out.println(req.getData());
        res = HttpRequestUtils.request("/score/scoreSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
        }
    }

    public void showEditStage() {
        try {
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("score-edit.fxml"));
            Stage stage=new Stage();
            Scene scene=null;

            scene = new Scene(fxmlLoader.load(), 350, 260);
            stage = new Stage();
            stage.initOwner(MainApplication.getMainStage());
            stage.initModality(Modality.NONE);
            stage.setAlwaysOnTop(true);
            stage.setScene(scene);
            stage.setTitle("成绩已录入对话框！");
            stage.setOnCloseRequest(event ->{
                MainApplication.setCanClose(true);
            });
            scoreEditController = (ScoreEditController) fxmlLoader.getController();
            scoreEditController.setScoreTableController(this);
            scoreEditController.init();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @FXML
    public void initialize() {

        studentNumColumn.setCellValueFactory(new MapValueFactory("student_id"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("course_id"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("course_name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        rankingColumn.setCellValueFactory(new MapValueFactory<>("ranking"));

        //因为调不来怎么只显示课程名/学生名所以先放着
        DataRequest req =new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList",req); //从后台获取所有学生信息列表集合
        Result courseResult = HttpRequestUtils.request("/course/selectAll",req); //从后台获取所有学生信息列表集合
        studentComboBox.getItems().addAll((List)studentResult.getData());
        courseComboBox.getItems().addAll((List)courseResult.getData());
        /*for(OptionItem student:studentList){
            studentComboBox.getItems().add();
        }
        for(OptionItem course:courseList){
            courseComboBox.getItems().add();
        }
        */
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }

}
