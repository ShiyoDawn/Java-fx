package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.util.Map;

public class FeeAddController {

    public Map absenceInfo;

    public void setAbsenceInfo(Map absenceInfo) {
        this.absenceInfo = absenceInfo;
    }
    public Map getAbsenceInfo(){
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
    private TextField dateTextField;

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
    private void onApplyButtonClick() {
        String msg= CommonMethod.alertButton("提交");
        if(msg=="确认"){
            DataRequest dataRequest=new DataRequest();
            dataRequest.add("id",Integer.parseInt(idTextField.getText())+1);
            dataRequest.add("student_name",studentNameTextField.getText());
            dataRequest.add("student_num",studentNumTextField.getText());
            dataRequest.add("date",dateTextField.getText());
            dataRequest.add("money",moneyTextField.getText());
            dataRequest.add("activity",activityTextField.getText());
            dataRequest.add("activity_detail",activityDetailTextField.getText());
            HttpRequestUtils.request("/fee/insertFee",dataRequest);
        }
        onCancelButtonClick();
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
        dateTextField.setText("");
        moneyTextField.setText("");
        activityTextField.setText("");
        activityDetailTextField.setText("");
    }

    @FXML
    public void initialize(Map map) {
        // TODO
        idTextField.setVisible(false);
        idTextField.setText(map.get("id").toString());
    }
}
