package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MainFrameController {

    private Map<String, Map> tabMap = new HashMap<String,Map>();

    @FXML
    BorderPane borderPane;
    @FXML
    Label userLabel;
    @FXML
    Label statueLabel;
    @FXML
    VBox vBox = new VBox(5);

    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane childPane = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(childPane);
        String userInfo = AppStore.getUser().getPerson_num() + "/" + AppStore.confirmType(AppStore.getUser());

        //TODO 发送请求，根据用户type获取tab
        FXMLLoader fxmlLoader2 = new FXMLLoader();
        fxmlLoader2.setLocation(MainApplication.class.getResource("table-view.fxml"));
        AnchorPane anchorPane = new AnchorPane();
        Map<String,String> map = new HashMap<>();
        map.put("title","table");
        tabMap.put("title",map);
        userLabel.setText(userInfo);
        statueLabel.setText("加载完成");

        // 1 pixel padding between child nodes only
        vBox.setPadding(new Insets(1));


        vBox.getChildren().add(new Button());

//        Iterator<String> iterator = tabMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            Map tab = tabMap.get(key);
//        }
    }


    public void getTabMap(){};




}
