package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.HelloApplication;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField user;

    @FXML
    private PasswordField password;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        showText();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-frame-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 400, 200);
        AppStore.setMainFrameController((MainFrameController) fxmlLoader.getController());
        HelloApplication.resetStage("教学管理系统", scene);
    }

    protected void showText() {
        System.out.println(user.getText() + password.getText());
    }


}