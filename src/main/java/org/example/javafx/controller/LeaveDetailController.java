package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
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

    public Map getAbsenceInfo() {
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
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("status", "不批准");
        dataRequest.add("student_num", studentIdTextField.getText());
        dataRequest.add("student_name", studentNameTextField.getText());
        dataRequest.add("institute", instituteTextField.getText());
        dataRequest.add("major", majorTextField.getText());
        dataRequest.add("instructor_name", instructorNameTextfield.getText());
        dataRequest.add("instructor_tele", instructorTeleTextField.getText());
        dataRequest.add("leave_detailed_reason", reasonTextField.getText());
        dataRequest.add("start_time", goOutDatePicker.getValue().toString());
        dataRequest.add("end_time", comeBackDatePicker.getValue().toString());
        dataRequest.add("student_tele", studentTeleTextField.getText());
        dataRequest.add("leave_reason", reasonComboBox.getValue());
        dataRequest.add("leave_type", goOutTypeComboBox.getValue());
        dataRequest.add("destination", destinationTextfield.getText());
        HttpRequestUtils.request("/leave/updateStatus", dataRequest);
        Stage stage = (Stage) passButton.getParent().getScene().getWindow();
        stage.close();
    }

    @FXML
    void onPassButtonClick() {
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("status", "已通过");
        dataRequest.add("student_num", studentIdTextField.getText());
        dataRequest.add("student_name", studentNameTextField.getText());
        dataRequest.add("institute", instituteTextField.getText());
        dataRequest.add("major", majorTextField.getText());
        dataRequest.add("instructor_name", instructorNameTextfield.getText());
        dataRequest.add("instructor_tele", instructorTeleTextField.getText());
        dataRequest.add("leave_detailed_reason", reasonTextField.getText());
        dataRequest.add("start_time", goOutDatePicker.getValue().toString());
        dataRequest.add("end_time", comeBackDatePicker.getValue().toString());
        dataRequest.add("student_tele", studentTeleTextField.getText());
        dataRequest.add("leave_reason", reasonComboBox.getValue());
        dataRequest.add("leave_type", goOutTypeComboBox.getValue());
        dataRequest.add("destination", destinationTextfield.getText());
        HttpRequestUtils.request("/leave/updateStatus", dataRequest);
        Stage stage = (Stage) passButton.getParent().getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize(Map map) {
        absenceInfo = map;

        if (AppStore.getUser().getUser_type_id() == 3) {
            passButton.setVisible(false);
            failButton.setVisible(false);
            passButton.setDisable(true);
            failButton.setDisable(true);
        }

        idTextField.setVisible(false);
        studentIdTextField.setText(map.get("student_num").toString());
        studentNameTextField.setText(map.get("student_name").toString());
        ageTextField.setText(map.get("age").toString());
        instituteTextField.setText(map.get("institute").toString());
        majorTextField.setText(map.get("major").toString());
        instructorNameTextfield.setText(map.get("instructor_name").toString());
        instructorTeleTextField.setText(map.get("instructor_tele").toString());
        reasonTextField.setText(map.get("leave_detailed_reason").toString());
        LocalDate start = LocalDate.parse(map.get("start_time").toString());
        LocalDate end = LocalDate.parse(map.get("end_time").toString());
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

        studentIdTextField.setOpacity(0.8);
        studentNameTextField.setOpacity(0.8);
        ageTextField.setOpacity(0.8);
        instituteTextField.setOpacity(0.8);
        majorTextField.setOpacity(0.8);
        instructorNameTextfield.setOpacity(0.8);
        instructorTeleTextField.setOpacity(0.8);
        reasonTextField.setOpacity(0.8);
        goOutDatePicker.setOpacity(0.8);
        comeBackDatePicker.setOpacity(0.8);
        studentTeleTextField.setOpacity(0.8);
        reasonComboBox.setOpacity(0.8);
        goOutTypeComboBox.setOpacity(0.8);
        destinationTextfield.setOpacity(0.8);
        finalCheckBox.setOpacity(0.8);

    }
}
