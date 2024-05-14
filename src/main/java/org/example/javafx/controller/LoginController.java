package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;
import org.example.javafx.util.ElementsTool;

import java.io.IOException;
import java.util.concurrent.Callable;

public class LoginController {

    @FXML
    VBox mainBox;

    @FXML
    HBox header;
    @FXML
    public Button closeButton;

    @FXML
    public Button minButton;

    @FXML
    private TextField user;

    @FXML
    private PasswordField passwordField;

    @FXML Button chooseAdmin;
    @FXML Button chooseStu;
    @FXML Button chooseTea;



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
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("用户名或密码错误");
            alert.showAndWait();
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

        //header移动初始化
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
        ElementsTool tool = new ElementsTool();
        tool.setCloseButton(closeButton);
        tool.setMinButton(minButton);

        tool.setButtonStyle01(chooseStu);
        tool.setButtonStyle01(chooseAdmin);
        tool.setButtonStyle01(chooseTea);

        chooseAdmin.setOnAction(e -> {
            user.setText("admin");
            passwordField.setText("123456");
        });

        chooseStu.setOnAction(e -> {
            user.setText("202300300001");
            passwordField.setText("22222222");
        });

        chooseTea.setOnAction(e -> {
//            user.setText("202300300001");
//            passwordField.setText("22222222");
        });
    }




}