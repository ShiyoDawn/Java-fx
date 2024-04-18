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
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.OptionItem;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;
import org.w3c.dom.Text;

import javax.sql.CommonDataSource;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreTableController {

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

    private List<OptionItem> courseList;
    private List<OptionItem> studentList;

    private ArrayList<Map> scoreList = new ArrayList<Map>(); // 学生信息列表数据
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
        dataRequest.add("studentId",studentId);
        dataRequest.add("courseId",courseId);
        result = HttpRequestUtils.request("/score/getScoreList",dataRequest); //从后台获取所有学生信息列表集合
        if(result != null && result.getCode()== 0) {
            scoreList = (ArrayList<Map>)result.getData();
        }
        //setTableViewData();
    }

    /*private void setTableViewData() {
        observableList.clear();
        Map map;
        Button editButton;
        for (int j = 0; j < scoreList.size(); j++) {
            map = scoreList.get(j);
            editButton = new Button("编辑");
            editButton.setId("edit"+j);
            editButton.setOnAction(e->{
                editItem(((Button)e.getSource()).getId());
            });
            map.put("edit",editButton);
            observableList.addAll(FXCollections.observableArrayList(map));
        }
        dataTableView.setItems(observableList);
    }*/
    /*

    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = scoreList.get(j);
        showEditStage();
        scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();

    }

    public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        Result res;
        Integer studentId = CommonMethod.getInteger(data,"studentId");
        if(studentId == null) {
            MessageDialog.showDialog("没有选中学生不能添加保存！");
            return;
        }
        Integer courseId = CommonMethod.getInteger(data,"courseId");
        if(courseId == null) {
            MessageDialog.showDialog("没有选中课程不能添加保存！");
            return;
        }
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        req.add("scoreId",CommonMethod.getInteger(data,"scoreId"));
        req.add("mark",CommonMethod.getInteger(data,"mark"));
        res = HttpRequestUtils.request("/api/score/scoreSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
        }
    }
    */
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
    /*@FXML
    public void initialize() {

        studentNumColumn.setCellValueFactory(new MapValueFactory("studentNum"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("studentName"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("courseNum"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("courseName"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));

        DataRequest req =new DataRequest();
        studentList = HttpRequestUtils.requestOptionItemList("/student/getStudentList",req); //从后台获取所有学生信息列表集合
        courseList = HttpRequestUtils.requestOptionItemList("/course/selectAll",req); //从后台获取所有学生信息列表集合
        OptionItem item = new OptionItem();
        item.setId(0);item.setValue(null);item.setTitle("请选择学生");
        studentComboBox.getItems().addAll(item);
        studentComboBox.getItems().addAll(studentList);
        item.setId(0);item.setValue(null);item.setTitle("请选择课程");
        courseComboBox.getItems().addAll(item);
        courseComboBox.getItems().addAll(courseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }*/

}
