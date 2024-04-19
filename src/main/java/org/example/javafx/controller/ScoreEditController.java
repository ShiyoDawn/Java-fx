package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.OptionItem;
import org.example.javafx.request.OptionItemList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreEditController {

    @FXML
    private ComboBox<OptionItem> courseComboBox;

    @FXML
    private TextField markField;

    @FXML
    private ComboBox<OptionItem> studentComboBox;

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

    private List<OptionItem> studentList;
    private List<OptionItem> courseList;

    public void setScoreTableController(ScoreTableController scoreTableController) {
        this.scoreTableController = scoreTableController;
    }

    //-----------------------------------------------------
    @FXML
    private void cancelButtonClick(ActionEvent actionEvent) {

    }

    private Integer scoreId;
    @FXML
    private void okButtonClick(ActionEvent actionEvent){
        Map data = new HashMap();
        Object student=studentComboBox.getSelectionModel().getSelectedItem();
        if(student != null) {
            data.put("studentName",student.toString());
        }
        Object course = courseComboBox.getSelectionModel().getSelectedItem();
        if(course != null) {
            data.put("courseName",course.toString());
        }
        data.put("id",scoreId);
        data.put("mark",markField.getText());
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
        studentList.add(cancelStudent);
        Map cancelCourse=new HashMap();
        cancelCourse.put("cancelCourse","请选择课程");
        courseList.add(cancelCourse);

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
    }

}
