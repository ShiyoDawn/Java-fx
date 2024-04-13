package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrameController {

    private Map<String, Map> tabMap = new HashMap<String,Map>();

    @FXML
    BorderPane borderPane;
    @FXML
    Label userLabel;
    @FXML
    VBox vBox = new VBox(5);

    @FXML
    HBox tabBox = new HBox();

    @FXML
    Button dashBoardButton = new Button();

    @FXML
    Button courseCenterButton = new Button();

    @FXML
    public void initialize() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane dashboard = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(dashboard);
        userLabel.setText(AppStore.getUser().getPerson_num() + "/" + AppStore.confirmType(AppStore.getUser()));



        //TODO 发送请求，根据用户type获取tab

//        Iterator<String> iterator = tabMap.keySet().iterator();
//        while (iterator.hasNext()) {
//            String key = iterator.next();
//            Map tab = tabMap.get(key);
//        }


        setTabChange(dashBoardButton,"dashboard-view.fxml");
        setTabChange(courseCenterButton,"student-view.fxml");
    }


    public void getTabMap(){};

    protected void setTabChange(Button button, String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource(url));
        BorderPane newPane = new BorderPane(fxmlLoader.load());
        button.setOnAction(e ->
                borderPane.setCenter(newPane)
        );
    }





}
