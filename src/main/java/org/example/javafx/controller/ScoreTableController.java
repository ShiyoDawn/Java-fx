package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
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
    private AnchorPane myAnchorPane;

    @FXML
    private BorderPane myBorderPane;
    @FXML
    private Button text;

    @FXML
    private TableColumn<Map,String> rankingColumn;

    @FXML
    private ComboBox<OptionItem> studentComboBox;

    @FXML
    private TableColumn<Map,String> studentNameColumn;

    @FXML
    private TableColumn<Map,String> studentNumColumn;

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

        Result r=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        //text.setText(r.getData().toString());
        System.out.println(r.getData().toString());
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {

    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {

    }

    /*@FXML
    private void onQueryButtonClick(){
        Integer studentId = 0;
        Integer courseId = 0;
        OptionItem op;
        op = studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            studentId = Integer.parseInt(op.getValue());
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null)
            courseId = Integer.parseInt(op.getValue());
        DataResponse res;
        DataRequest req =new DataRequest();
        req.add("studentId",studentId);
        req.add("courseId",courseId);
        res = HttpRequestUtil.request("/api/score/getScoreList",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            scoreList = (ArrayList<Map>)res.getData();
        }
        setTableViewData();
    }*/

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
    }

    public void editItem(String name){
        if(name == null)
            return;
        int j = Integer.parseInt(name.substring(4,name.length()));
        Map data = scoreList.get(j);
        showEditStage();
        scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();

    }*/

    /*public void doClose(String cmd, Map data) {
        MainApplication.setCanClose(true);
        stage.close();
        if(!"ok".equals(cmd))
            return;
        DataResponse res;
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
        res = HttpRequestUtil.request("/api/score/scoreSave",req); //从后台获取所有学生信息列表集合
        if(res != null && res.getCode()== 0) {
            onQueryButtonClick();
        }
    }*/

    public void showEditStage() {
        try {
            FXMLLoader fxmlLoader=new FXMLLoader();
            fxmlLoader = new FXMLLoader(MainApplication.class.getResource("score-edit-dialog.fxml"));
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
}
