package org.example.javafx.util;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import org.example.javafx.MainApplication;

public class ElementsTool {
    public void setCloseButton(Button closeButton) {
        //关闭按钮初始化
        closeButton.setTextFill(Color.RED);
        closeButton.setStyle(
                "-fx-background-color:#ff0000;"+         //设置背景颜色
                        "-fx-background-radius:0 0 0 5;"     //设置背景圆角
        );

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApplication.getMainStage().close();
            }
        });

        closeButton.setOnMouseEntered(e -> {
            closeButton.setTextFill(Color.WHITE);
        });

        closeButton.setOnMouseExited(e -> {
            closeButton.setTextFill(Color.RED);
        });
    }

    public void setMinButton(Button closeButton) {

        //按钮初始化
        closeButton.setStyle(
                 "-fx-background-color:#93f9b9;" + //设置背景颜色
                        "-fx-background-radius:0 0 5 5;"     //设置背景圆角
        );

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainApplication.getMainStage().setIconified(true);
            }
        });

        closeButton.setOnMouseEntered(e -> {
            closeButton.setTextFill(Color.WHITE);
        });

        closeButton.setOnMouseExited(e -> {
            closeButton.setTextFill(Color.rgb(147, 249, 185));
        });
    }
}
