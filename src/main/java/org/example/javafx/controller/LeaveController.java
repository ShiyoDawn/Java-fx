package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import lombok.Data;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.time.LocalDate;
import java.util.*;

public class LeaveController {

    @FXML
    private TextField ageTextField;

    @FXML
    private Button applyButton;

    @FXML
    private TextField destinationTextfield;

    @FXML
    private DatePicker goOutDatePicker;

    @FXML
    private DatePicker comeBackDatePicker;

    @FXML
    private ComboBox goOutTypeComboBox;

    @FXML
    private TextField instituteTextField;

    @FXML
    private TextField instructorNameTextfield;

    @FXML
    private TextField instructorTeleTextField;

    @FXML
    private TextField majorTextField;

    @FXML
    private ComboBox reasonComboBox;

    @FXML
    private TextField reasonTextField;

    @FXML
    private Button resetButton;

    @FXML
    private Button saveButton;

    @FXML
    private TextField studentIdTextField;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField studentTeleTextField;

    @FXML
    private CheckBox finalCheckBox;

    @FXML
    private void onApplyButtonClick(ActionEvent event) {
        Integer cnt=0;
        if(studentIdTextField.getText()==""){
            cnt++;
        }
        if(studentNameTextField.getText()==""){
            cnt++;
        }
        if(studentTeleTextField.getText()==""){
            cnt++;
        }
        if(instituteTextField.getText()==""){
            cnt++;
        }
        if(instructorNameTextfield.getText()==""){
            cnt++;
        }
        if(instructorTeleTextField.getText()==""){
            cnt++;
        }
        if(majorTextField.getText()==""){
            cnt++;
        }
        if(reasonTextField.getText()==""){
            cnt++;
        }
        if(destinationTextfield.getText()==""){
            cnt++;
        }
        if(finalCheckBox.isSelected()){
            cnt++;
        }
        if(goOutTypeComboBox.getSelectionModel().getSelectedItem()==null||goOutTypeComboBox.getSelectionModel().getSelectedItem().toString()=="请选择外出类型"){
            cnt++;
        }
        if(reasonComboBox.getSelectionModel().getSelectedItem()==null||goOutTypeComboBox.getSelectionModel().getSelectedItem().toString()=="请选择外出事由"){
            cnt++;
        }
        if(cnt==0){
            LocalDate goOutDate=goOutDatePicker.getValue();
            LocalDate comeBackDate=comeBackDatePicker.getValue();
            if(goOutDate.isAfter(comeBackDate)){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请选择正确的时间");
            }
            String msg=CommonMethod.alertButton("提交");
            if(msg=="确认"){
                DataRequest dataRequest=new DataRequest();
                //HttpRequestUtils.request("/leave/insertLeave",dataRequest);
            }
        }
    }

    @FXML
    private void onResetButtonClick(ActionEvent event) {
        goOutTypeComboBox.setValue("请选择外出类型");
        reasonComboBox.setValue("请选择外出事由");
        goOutDatePicker.setValue(LocalDate.now());
        comeBackDatePicker.setValue(LocalDate.now());
        studentIdTextField.setText("");
        studentNameTextField.setText("");
        studentTeleTextField.setText("");
        instituteTextField.setText("");
        instructorNameTextfield.setText("");
        instructorTeleTextField.setText("");
        majorTextField.setText("");
        reasonTextField.setText("");
        destinationTextfield.setText("");
        finalCheckBox.setSelected(false);
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) {

    }

    @FXML
    private void initialize(){
        goOutTypeComboBox.setValue("请选择请假类型");
        reasonComboBox.setValue("请选择请假事由");
        String[] goOutType={"请选择请假类型","临时外出","固定外出","毕业离校","生病请假"};
        String[] reason={"请选择请假事由","跨校区学习,实验等","外出实习等常规业务","外出办事","假期离校","外出就诊","毕业离校"};
        List goOutTypeList=new ArrayList<>();
        List reasonList=new ArrayList();
        for(int i=0;i<5;i++){
            Map map=new HashMap();
            map.put(i+"",goOutType[i]);
            goOutTypeList.add(map.get(i+""));
        }
        for(int i=0;i<7;i++){
            Map map=new HashMap();
            map.put(i+"",reason[i]);
            reasonList.add(map.get(i+""));
        }
        System.out.println(goOutTypeList);
        goOutTypeComboBox.getItems().addAll(goOutTypeList);
        reasonComboBox.getItems().addAll(reasonList);
        goOutDatePicker.setValue(LocalDate.now());
        comeBackDatePicker.setValue(LocalDate.now());
        goOutDatePicker.setEditable(false);
        comeBackDatePicker.setEditable(false);
    }
}
