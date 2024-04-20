package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.OptionItem;
import org.example.javafx.request.OptionItemList;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreEditController {

    @FXML
    private ComboBox courseComboBox;

    @FXML
    private TextField markField;

    @FXML
    private ComboBox studentComboBox;

    @FXML
    private AnchorPane editAnchorPane;

    @FXML
    private BorderPane editBorderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Button okButton;

    //-----------------------------------------------------

    private ScoreTableController scoreTableController;

    private List studentList;
    private List courseList;

    public void setScoreTableController(ScoreTableController scoreTableController) {
        this.scoreTableController = scoreTableController;
    }

    //-----------------------------------------------------
    @FXML
    private void cancelButtonClick(ActionEvent actionEvent) {
        // 关闭对话框或窗口(取消按钮)
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    private Integer scoreId;
    @FXML
    private void okButtonClick(ActionEvent actionEvent){
        DataRequest dataRequest=new DataRequest();
        String student_name=null;
        String course_name=null;
        Object student=studentComboBox.getSelectionModel().getSelectedItem();
        Object course=courseComboBox.getSelectionModel().getSelectedItem();
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
        if(markField.getText()==null){
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text=new Text("请输入分数");
            HBox hBox=new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene=new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
            return;
        }
        if (student_name != null && course_name != null)
        {
            DataRequest stuDataRequest = new DataRequest();
            stuDataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            Map map = (Map) result.getData();
            Double student_id = Double.parseDouble(map.get("id").toString());

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("course_name", course_name);
            result = HttpRequestUtils.request("/course/selectCourseByName", courDataRequest);
            map = (Map) result.getData();
            Double course_id = Double.parseDouble(map.get("id").toString());

            dataRequest.add("student_id", student_id);
            dataRequest.add("course_id", course_id);
            dataRequest.add("mark",markField.getText());
            HttpRequestUtils.request("/score/insertScore",dataRequest);
        }else if(student_name == null && course_name != null){
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text=new Text("请输入学生");
            HBox hBox=new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene=new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }else if(student_name != null && course_name == null){
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text=new Text("请输入课程");
            HBox hBox=new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene=new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }else{
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text=new Text("请输入学生和课程");
            HBox hBox=new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene=new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }

    }


    public void initialize(){

        //有问题，显示不出来
        DataRequest req=new DataRequest();
        List studentList=new ArrayList();
        List courseList=new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList",req); //从后台获取所有学生信息列表集合
        Result courseResult = HttpRequestUtils.request("/course/selectAll",req); //从后台获取所有学生信息列表集合

        Map cancelStudent=new HashMap();
        cancelStudent.put("cancelStudent","请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        Map cancelCourse=new HashMap();
        cancelCourse.put("cancelCourse","请选择课程");
        courseList.add(cancelCourse.get("cancelCourse"));

        List<Map> studentMap=(List<Map>) studentResult.getData();
        List<Map> courseMap=(List<Map>) courseResult.getData();
        for(Map student:studentMap){
            studentList.add(student.get("student_name"));
        }
        for(Map course:courseMap){
            courseList.add(course.get("course_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
        //markField.setDisable(false);
    }

}
