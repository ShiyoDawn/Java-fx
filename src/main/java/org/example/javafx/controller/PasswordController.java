package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.util.Map;

public class PasswordController {


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
    private void onApplyButtonClick() {
        User user = AppStore.getUser();
        String originPassword = originPasswordTextField.getText();
        String newPassword = newPasswordTextField.getText();
        String confirmPassword = confirmPasswordTextField.getText();
        if(!newPassword.equals(confirmPassword)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("两次输入的密码不一致，请重新输入!");
            return;
        }
        if(newPassword.equals(originPassword)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("新旧密码一致，请重新输入新密码!");
            return;
        }
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
    public void initialize() {
        User user= AppStore.getUser();
        String truePassword = user.getPassword();
        newPasswordTextField.setDisable(true);
        confirmPasswordTextField.setDisable(true);
        applyButton.setDisable(true);
        checkNew.setVisible(false);
        checkOriginal.setVisible(false);
        originPasswordTextField.setOnAction(e->{
            checkOriginal.setVisible(true);
            if(originPasswordTextField.getText().equals(truePassword)){
                newPasswordTextField.setDisable(false);
                confirmPasswordTextField.setDisable(false);
                checkOriginal.setText("密码正确！");
                checkOriginal.setTextFill(javafx.scene.paint.Color.GREEN);
            }else{
                checkOriginal.setText("密码错误！");
                checkOriginal.setTextFill(javafx.scene.paint.Color.RED);
            }
        });
        confirmPasswordTextField.setOnAction(e->{
            checkNew.setVisible(true);
            if(newPasswordTextField.getText().equals(confirmPasswordTextField.getText())){
                checkNew.setText("密码一致！");
                applyButton.setDisable(false);
                checkNew.setTextFill(javafx.scene.paint.Color.GREEN);
            }else{
                checkNew.setText("密码不一致！");
                applyButton.setDisable(true);
                checkNew.setTextFill(javafx.scene.paint.Color.RED);
            }
        });
    }
}
