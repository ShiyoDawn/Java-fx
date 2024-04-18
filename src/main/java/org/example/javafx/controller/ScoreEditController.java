package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import org.example.javafx.request.OptionItem;
import org.example.javafx.request.OptionItemList;

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

    private ScoreTableController scoreTableController;

    private List<OptionItem> studentList;
    private List<OptionItem> courseList;

    public void setScoreTableController(ScoreTableController scoreTableController) {
        this.scoreTableController = scoreTableController;
    }
    @FXML
    private void cancelButtonClick(ActionEvent actionEvent) {

    }

    private Integer scoreId;
    @FXML
    private void okButtonClick(ActionEvent actionEvent){
        Map data = new HashMap();
        OptionItem op=studentComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("studentId",Integer.parseInt(op.getValue()));
        }
        op = courseComboBox.getSelectionModel().getSelectedItem();
        if(op != null) {
            data.put("courseId", Integer.parseInt(op.getValue()));
        }
        data.put("scoreId",scoreId);
        data.put("mark",markField.getText());
    }


    public void init(){
        studentList =scoreTableController.getStudentList();
        courseList = scoreTableController.getCourseList();
        studentComboBox.getItems().addAll(studentList );
        courseComboBox.getItems().addAll(courseList);
    }

}
