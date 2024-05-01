package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.time.LocalDate;
import java.util.Map;

public class LeaveDetailController {
    public Map absenceInfo;

    public void setAbsenceInfo(Map absenceInfo) {
        this.absenceInfo = absenceInfo;
    }
    public Map getAbsenceInfo(){
        return absenceInfo;
    }

    @FXML
    private TextField idTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private DatePicker comeBackDatePicker;

    @FXML
    private TextField destinationTextfield;

    @FXML
    private Button failButton;

    @FXML
    private CheckBox finalCheckBox;

    @FXML
    private DatePicker goOutDatePicker;

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
    private Button passButton;

    @FXML
    private ComboBox reasonComboBox;

    @FXML
    private TextField reasonTextField;

    @FXML
    private TextField studentIdTextField;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TextField studentTeleTextField;

    @FXML
    void onFailButtonClick() {
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("status","不批准");
        dataRequest.add("id",Integer.parseInt(idTextField.getText()));
        HttpRequestUtils.request("/leave/updateStatus",dataRequest);
        Stage stage=(Stage) passButton.getParent().getScene().getWindow();
        stage.close();
    }

    @FXML
    void onPassButtonClick() {
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("status","已通过");
        dataRequest.add("id",Integer.parseInt(idTextField.getText()));
        HttpRequestUtils.request("/leave/updateStatus",dataRequest);
        Stage stage=(Stage) passButton.getParent().getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize(Map map){
        absenceInfo=map;
        System.out.println(absenceInfo);

        idTextField.setVisible(false);

        idTextField.setText(map.get("id").toString());
        studentIdTextField.setText(map.get("student_num").toString());
        studentNameTextField.setText(map.get("student_name").toString());
        ageTextField.setText(map.get("age").toString());
        instituteTextField.setText(map.get("institute").toString());
        majorTextField.setText(map.get("major").toString());
        instructorNameTextfield.setText(map.get("instructor_name").toString());
        instructorTeleTextField.setText(map.get("instructor_tele").toString());
        reasonTextField.setText(map.get("leave_detailed_reason").toString());
        LocalDate start= LocalDate.parse(map.get("start_time").toString());
        LocalDate end=LocalDate.parse(map.get("end_time").toString());
        goOutDatePicker.setValue(start);
        comeBackDatePicker.setValue(end);
        studentTeleTextField.setText(map.get("student_tele").toString());
        reasonComboBox.setValue(map.get("leave_reason"));
        goOutTypeComboBox.setValue(map.get("leave_type"));
        destinationTextfield.setText(map.get("destination").toString());
        finalCheckBox.setSelected(true);

        studentIdTextField.setDisable(true);
        studentNameTextField.setDisable(true);
        ageTextField.setDisable(true);
        instituteTextField.setDisable(true);
        majorTextField.setDisable(true);
        instructorNameTextfield.setDisable(true);
        instructorTeleTextField.setDisable(true);
        reasonTextField.setDisable(true);
        goOutDatePicker.setDisable(true);
        comeBackDatePicker.setDisable(true);
        studentTeleTextField.setDisable(true);
        reasonComboBox.setDisable(true);
        goOutTypeComboBox.setDisable(true);
        destinationTextfield.setDisable(true);
        finalCheckBox.setDisable(true);
    }
}
