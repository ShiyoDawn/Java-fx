package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.time.LocalDate;
import java.util.Map;
import java.util.regex.Pattern;

public class FeeAddController {

    public Map absenceInfo;

    public void setAbsenceInfo(Map absenceInfo) {
        this.absenceInfo = absenceInfo;
    }

    public Map getAbsenceInfo() {
        return absenceInfo;
    }


    @FXML
    private TextArea activityDetailTextField;

    @FXML
    private TextField activityTextField;

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;


    @FXML
    private TextField moneyTextField;

    @FXML
    private Button resetButton;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField studentNumTextField;

    @FXML
    private TextField idTextField;

    @FXML
    private DatePicker fromDate;

    @FXML
    private DatePicker toDate;

    @FXML
    private void onApplyButtonClick() {
        if (studentNameTextField.getText() == "" || studentNumTextField.getText() == "" || moneyTextField.getText() == "" || activityTextField.getText() == "" || activityDetailTextField.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请填写完整信息");
            alert.showAndWait();
            return;
        }
        if(moneyTextField.getText().charAt(0)!='-'&&moneyTextField.getText().charAt(0)!='+'&&moneyTextField.getText()!="0"){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("金额格式错误");
            alert.showAndWait();
            return;
        }
        if(moneyTextField.getText()!="0"){
            if(!Pattern.matches("^\\d+(\\.\\d+)?$",moneyTextField.getText().substring(1))){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("金额格式错误");
                alert.showAndWait();
                return;
            }
        }
        if(fromDate.getValue().isAfter(toDate.getValue())){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("日期错误");
            alert.showAndWait();
            return;
        }
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("person_num",studentNumTextField.getText());
        Result result=new Result();
        result=HttpRequestUtils.request("/person/selectByPersonNum",dataRequest);
        if(result==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("学生不存在");
            alert.showAndWait();
            return;
        }else{
            if(result.getCode()==404){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("学生不存在");
                alert.showAndWait();
                return;
            }
            Map map=(Map)result.getData();
            if(!(map.get("person_name").equals(studentNameTextField.getText()))){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("输入的学生姓名与学号不匹配");
                alert.showAndWait();
                return;
            }
        }
        String msg = CommonMethod.alertButton("提交");
        if (msg == "确认") {
            dataRequest = new DataRequest();
            dataRequest.add("id", Integer.parseInt(idTextField.getText()) + 1);
            dataRequest.add("student_name", studentNameTextField.getText());
            dataRequest.add("student_num", studentNumTextField.getText());
            dataRequest.add("date", fromDate.getValue().toString()+"~"+toDate.getValue().toString());
            dataRequest.add("money", moneyTextField.getText());
            dataRequest.add("activity", activityTextField.getText());
            dataRequest.add("activity_detail", activityDetailTextField.getText());
            HttpRequestUtils.request("/fee/insertFee", dataRequest);
            onCancelButtonClick();
        }
    }

    @FXML
    private void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onResetButtonClick() {
        studentNameTextField.setText("");
        studentNumTextField.setText("");
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        moneyTextField.setText("");
        activityTextField.setText("");
        activityDetailTextField.setText("");
        if(AppStore.getUser().getUser_type_id()==3){
            studentNameTextField.setText(absenceInfo.get("student_name").toString());
            studentNumTextField.setText(absenceInfo.get("student_num").toString());
            studentNameTextField.setDisable(true);
            studentNumTextField.setDisable(true);
        }
    }

    @FXML
    public void initialize(Map map) {
        // TODO
        absenceInfo=map;
        idTextField.setVisible(false);
        idTextField.setText(map.get("id").toString());
        studentNameTextField.setText("");
        studentNumTextField.setText("");
        fromDate.setValue(LocalDate.now());
        toDate.setValue(LocalDate.now());
        moneyTextField.setText("");
        activityTextField.setText("");
        activityDetailTextField.setText("");
        User user= AppStore.getUser();
        if(user.getUser_type_id()==3){
            studentNameTextField.setText(absenceInfo.get("student_name").toString());
            studentNumTextField.setText(absenceInfo.get("student_num").toString());
            studentNameTextField.setDisable(true);
            studentNumTextField.setDisable(true);
        }
    }
}
