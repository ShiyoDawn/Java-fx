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

public class ElementsTool {
    public void setCloseButton(Button closeButton) {
        //关闭按钮初始化
        closeButton.setTextFill(Color.RED);
        BackgroundFill backgroundFill = new BackgroundFill(Color.RED, new CornerRadii(0), Insets.EMPTY);
        Background background = new Background(backgroundFill);
        closeButton.setBackground(background);
        closeButton.setOpacity(0.4);

        closeButton.setOnAction(e -> {
                MainApplication.getMainStage().close();
        });

        closeButton.setOnMouseEntered(e -> {
            closeButton.setTextFill(Color.WHITE);
            closeButton.setOpacity(1);
        });

        closeButton.setOnMouseExited(e -> {
            closeButton.setTextFill(Color.RED);
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
        button.setStyle("-fx-background-color:rgb(0,255,208,0.1)");
        button.setOnMouseEntered(e ->{
            button.setStyle("-fx-background-color:rgb(0,255,208,0.4)");
        });
        button.setOnMouseExited(e -> {
            button.setStyle("-fx-background-color:rgb(0,255,208,0.1)");
        });
    }
}
