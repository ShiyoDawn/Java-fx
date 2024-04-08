package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Data;

import java.io.IOException;


public class MainApplication extends Application {

    private static Stage mainStage;
    private static double stageWidth = 1200;
    private static double stageHeight = 800;

    private static boolean canClose=true;
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 320, 240);
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
        mainStage = stage;
    }

    public static void main(String[] args) {
        launch();
    }

    public static void resetStage(String name, Scene scene) {
        if(stageWidth > 0) {
            mainStage.setWidth(stageWidth);
            mainStage.setHeight(stageHeight);
            mainStage.setX(200);
            mainStage.setY(80);
        }
        mainStage.setTitle(name);
        mainStage.setScene(scene);
        mainStage.setResizable(false);
        mainStage.show();
    }
    public static void setCanClose(boolean canClose) {
        MainApplication.canClose = canClose;
    }

    public static Stage getMainStage(){
        return mainStage;
    }
}