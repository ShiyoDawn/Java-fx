package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;

import java.io.IOException;
import java.util.concurrent.Callable;

public class LoginController {

    @FXML
    VBox mainBox;

    @FXML
    HBox header;

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
    double x1;
    double y1;
    double x_stage;
    double y_stage;



    public void initialize() {

        header.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent m) {
                //计算
                MainApplication.getMainStage().setX(x_stage + m.getScreenX() - x1);
                MainApplication.getMainStage().setY(y_stage + m.getScreenY() - y1);
            }
        });
        header.setOnDragEntered(null);
        header.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent m) {

                //按下鼠标后，记录当前鼠标的坐标
                x1 = m.getScreenX();
                y1 = m.getScreenY();
                x_stage = MainApplication.getMainStage().getX();
                y_stage = MainApplication.getMainStage().getY();
            }
        });
    }




}