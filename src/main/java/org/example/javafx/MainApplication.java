package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;


public class MainApplication extends Application {

    private static Stage mainStage;
    private static double stageWidth = 1200;
    private static double stageHeight = 800;

    private static boolean canClose=true;
    @Override
    public void start(Stage stage) throws IOException {

        var fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 650, 400);
        stage.setTitle("Hello!");
//        stage.getIcons().add(new Image("css.base.png"));// icon
        stage.initStyle(StageStyle.TRANSPARENT); // 修改窗口样式
        stage.setScene(scene);

        stage.show();

        mainStage = stage;

    }

    public static void resetStage(String name, Scene scene) {
        if(stageWidth > 0) {
            mainStage.setWidth(stageWidth);
            mainStage.setHeight(stageHeight);
            mainStage.setX(200);
            mainStage.setY(80);
            mainStage.setTitle("教务管理系统");// 标题
            scene.setFill(null);
            mainStage.setScene(scene);
            mainStage.show();
        }

    }
    public static void setCanClose(boolean canClose) {
        MainApplication.canClose = canClose;
    }

    public static Stage getMainStage(){
        return mainStage;
    }
}