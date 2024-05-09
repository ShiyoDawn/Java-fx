package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.util.Map;

public class PasswordController {

    public Map passwordMap;

    public Map getPasswordMap() {
        return passwordMap;
    }

    public void setPasswordMap(Map passwordMap) {
        this.passwordMap = passwordMap;
    }

    @FXML
    private Button applyButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField confirmPasswordTextField;

    @FXML
    private TextField newPasswordTextField;

    @FXML
    private TextField originPasswordTextField;

    @FXML
    private Label checkOriginal;

    @FXML
    private Label checkNew;

    @FXML
    private Label checkConfirm;

    @FXML
    private void onApplyButtonClick() {
        User user = AppStore.getUser();
        String originPassword = originPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        String msg= CommonMethod.alertButton("修改密码");
        if(msg=="确认"){
            DataRequest dataRequest=new DataRequest();
            dataRequest.add("password",newPassword);
            dataRequest.add("person_num",user.getPerson_num());
            HttpRequestUtils.request("/user/updatePassword",dataRequest);
            onCancelButtonClick();
        }
    }

    @FXML
    private void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void initialize(Map map) {
        passwordMap=map;
        String truePassword = map.get("password").toString();
        newPasswordTextField.setDisable(true);
        confirmPasswordTextField.setDisable(true);
        applyButton.setDisable(true);
        checkNew.setVisible(false);
        checkConfirm.setVisible(false);
        checkOriginal.setVisible(false);
        originPasswordTextField.setOnAction(e->{
            checkOriginal.setVisible(true);
            if(originPasswordTextField.getText().equals(truePassword)){
                newPasswordTextField.setDisable(false);
                checkOriginal.setText("密码正确！");
                checkOriginal.setTextFill(javafx.scene.paint.Color.GREEN);
            }else{
                checkNew.setVisible(false);
                checkConfirm.setVisible(false);
                checkOriginal.setText("密码错误！");
                newPasswordTextField.setText("");
                newPasswordTextField.setDisable(true);
                confirmPasswordTextField.setText("");
                confirmPasswordTextField.setDisable(true);
                applyButton.setDisable(true);
                checkOriginal.setTextFill(javafx.scene.paint.Color.RED);
            }
        });
        newPasswordTextField.setOnAction(e->{
            checkNew.setVisible(true);
            if(newPasswordTextField.getText().equals(originPasswordTextField.getText())) {
                checkNew.setText("新旧密码一致，请重新输入新密码!");
                confirmPasswordTextField.setText("");
                confirmPasswordTextField.setDisable(true);
                checkConfirm.setVisible(false);
                applyButton.setDisable(true);
                checkNew.setTextFill(Color.RED);
            }else if(newPasswordTextField.getText().length()<6||newPasswordTextField.getText().length()>16){
                checkNew.setText("密码位数不符合规范!");
                confirmPasswordTextField.setText("");
                confirmPasswordTextField.setDisable(true);
                checkConfirm.setVisible(false);
                applyButton.setDisable(true);
                checkNew.setTextFill(Color.RED);
            }else{
                confirmPasswordTextField.setDisable(false);
                checkNew.setText("密码符合规范！");
                checkNew.setTextFill(javafx.scene.paint.Color.GREEN);
            }
        });
        confirmPasswordTextField.setOnAction(e->{
            checkConfirm.setVisible(true);
            if(newPasswordTextField.getText().equals(confirmPasswordTextField.getText())){
                checkConfirm.setText("两次密码一致！");
                applyButton.setDisable(false);
                checkConfirm.setTextFill(javafx.scene.paint.Color.GREEN);
            }else{
                checkConfirm.setText("两次密码不一致！");
                applyButton.setDisable(true);
                checkConfirm.setTextFill(javafx.scene.paint.Color.RED);
            }
        });
    }
}
