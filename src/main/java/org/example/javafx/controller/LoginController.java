package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;

import java.io.IOException;

public class LoginController {

    @FXML
    private TextField user;

    @FXML
    private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick() throws IOException {
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
        String userName = user.getText();
        String password = passwordField.getText();
        try{
            Result result = httpRequestUtils.login(new LoginRequest(userName, password));
            User nowUser = new Gson().fromJson(result.getData().toString(),User.class);
            AppStore.setUser(nowUser);
        }
        catch(Exception e) {
            System.out.println("未知错误无法登录");
            return;
        }

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/mainframe.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1600, 900);
        AppStore.setMainFrameController((MainFrameController) fxmlLoader.getController());
        MainApplication.resetStage("教学管理系统", scene);


    }


}