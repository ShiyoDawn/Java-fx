package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javafx.AppStore;
import org.example.javafx.HelloApplication;
import org.example.javafx.request.HttpRequest;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    protected void onLoginButtonClick() throws IOException {
//        showText();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("base/mainframe.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        AppStore.setMainFrameController((MainFrameController) fxmlLoader.getController());
        HelloApplication.resetStage("教学管理系统", scene);
        HttpRequest httpRequest = new HttpRequest();
        httpRequest.login();
    }

    protected void showText() {
        System.out.println(user.getText() + password.getText());
    }


}