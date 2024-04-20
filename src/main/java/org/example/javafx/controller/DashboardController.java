package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.LoginRequest;

import java.io.IOException;
import java.util.List;

public class DashboardController {
    @FXML
    BorderPane borderPane;
    @FXML
    MenuBar menuBar;
    @FXML
    GridPane gridPane;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        DataRequest dataRequest1 = new DataRequest();
        Result data = HttpRequestUtils.courseField("/lesson/selectLesson",dataRequest1);
        List dataList = (List) data.getData();
        int row = 2;
        int col = 1;
        for (Object a : dataList) {
            System.out.println("a = " + a.toString());
            String[] b = a.toString().split(",");
            String course_name = b[1].substring(11);
            Label label = new Label(course_name);
            gridPane.add(label, col, row);
            col++;
            if (col == 8) {
                col = 1;
                row++;
            }
        }
    }

}
