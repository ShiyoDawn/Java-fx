package org.example.javafx.util;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import org.example.javafx.MainApplication;


import static javafx.application.Platform.exit;

public class ElementsTool {
    public void setCloseButton(Button closeButton) {
        //关闭按钮初始化
        closeButton.setTextFill(Color.WHITE);
        BackgroundFill backgroundFill = new BackgroundFill(Color.CADETBLUE, new CornerRadii(0), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        closeButton.setBackground(background);
        closeButton.setOpacity(0.6);

        closeButton.setOnAction(e -> {
                MainApplication.getMainStage().close();
                exit();
        });

        closeButton.setOnMouseEntered(e -> {
            closeButton.setTextFill(Color.WHITE);
            BackgroundFill back = new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY);
            Background background1 = new Background(back);
            closeButton.setBackground(background1);
            closeButton.setOpacity(1);
        });

        closeButton.setOnMouseExited(e -> {
            BackgroundFill back = new BackgroundFill(Color.CADETBLUE, new CornerRadii(0), Insets.EMPTY);
            Background background1 = new Background(back);
            closeButton.setBackground(background1);
            closeButton.setOpacity(0.4);
        });
    }

    public void setMinButton(Button minButton) {

        minButton.setTextFill(Color.rgb(147, 249, 185));
        BackgroundFill backgroundFill = new BackgroundFill(Paint.valueOf("#93F9B9"), new CornerRadii(0), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        minButton.setBackground(background);
        minButton.setOpacity(0.4);


        minButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApplication.getMainStage().setIconified(true);
            }
        });

        minButton.setOnMouseEntered(e -> {
            minButton.setTextFill(Color.WHITE);
            minButton.setOpacity(1);
        });

        minButton.setOnMouseExited(e -> {
            minButton.setTextFill(Color.rgb(147, 249, 185));
            minButton.setOpacity(0.4);
        });

    }

    public void setResizeButton(Button resizeButton){
        resizeButton.setTextFill(Color.rgb(110, 252, 255));
        resizeButton.setStyle(
                "-fx-background-color:#6efcff;" + //设置背景颜色
                        "-fx-background-radius:0 0 5 5;"     //设置背景圆角
        );
        resizeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApplication.getMainStage().setResizable(true);
            }
        });

        resizeButton.setOnMouseEntered(e -> {
            resizeButton.setTextFill(Color.WHITE);
        });

        resizeButton.setOnMouseExited(e -> {
            resizeButton.setTextFill(Color.rgb(110, 252, 255));
        });

    }

    public void setButtonStyle01(Button button){
        button.setStyle("-fx-background-color:rgb(0,255,208,0.7)");
        button.setOnMouseEntered(e ->{
            button.setStyle("-fx-background-color:rgb(0,255,208)");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color:rgb(0,255,208,0.7)");
        });
    }

    public static void setEventButton1(Button button) {
        button.setStyle("-fx-pref-height: 30px; -fx-background-color: #ff6200; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-pref-height: 30px; -fx-background-color: #e55c04; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
        button.setOnMouseExited(e ->  button.setStyle("-fx-pref-height: 30px; -fx-background-color: #ff6200; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
    }

    public static void setEventButton2(Button button) {
        button.setStyle("-fx-pref-height: 30px; -fx-background-color: #66c77e; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-pref-height: 30px; -fx-background-color:#53a167; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
        button.setOnMouseExited(e ->  button.setStyle("-fx-pref-height: 30px; -fx-background-color: #66c77e; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
    }

    public static void setEventButton3(Button button) {
        button.setStyle("-fx-pref-height: 30px; -fx-background-color: #cb4040; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;");
        button.setOnMouseEntered(e -> button.setStyle("-fx-pref-height: 30px; -fx-background-color:#a15353; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
        button.setOnMouseExited(e ->  button.setStyle("-fx-pref-height: 30px; -fx-background-color: #cb4040; " +
                "-fx-pref-width: 235px; -fx-text-fill: white;-fx-background-radius: 5px;"));
    }

}
