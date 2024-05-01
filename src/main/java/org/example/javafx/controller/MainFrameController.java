package org.example.javafx.controller;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrameController {

    @FXML
    BorderPane borderPane;
    @FXML
    Label userLabel;
    @FXML
    VBox vBox = new VBox(5);

    @FXML
    ComboBox searchBox;

    @FXML
    Button dashBoardButton = new Button();

//    @FXML
//    Button courseCenterButton = new Button();
//
//    @FXML
//    Button studentCenterButton = new Button();
//
//    @FXML
//    Button activityCenterButton = new Button();
//
//    @FXML
//    Button userCenterButton = new Button();
//
//    @FXML
//    Button scoreCenterButton = new Button();
//
//    @FXML
//    Button gloryCenterButton = new Button();

    @FXML
    Label statueLabel;

    @FXML
    Button searchButton;

    List<Map<String,String>> menuList;

    List menuListOnlyName = new ArrayList<>();

    @FXML
    public void initialize() throws IOException, InterruptedException {

        menuList = new HttpRequestUtils().getMenu(AppStore.getUser().getUser_type_id());
        System.out.println(menuList);
        //加载仪表盘界面为初始界面
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane dashboard = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(dashboard);
        userLabel.setText(AppStore.getUser().getPerson_num() + "/" + AppStore.confirmType(AppStore.getUser()));
        setTabChange(dashBoardButton, "dashboard-view.fxml");


        //初始化页面切换
        for (int i = 1; i < menuList.size(); i++) {
            Button newButton = new Button(menuList.get(i).get("name"));
            newButton.setPrefHeight(35);
            newButton.setPrefWidth(100);
            vBox.getChildren().add(i,newButton);
            setTabChange(newButton,menuList.get(i).get("url"));
        }



        searchBox.setEditable(true);


        for (int i = 0; i < menuList.size(); i++) {
            menuListOnlyName.add(menuList.get(i).get("name"));
        }
        searchBox.getItems().addAll(menuListOnlyName);
        searchButton.setOnAction(e ->
        {
            try {
                setSearchBox();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });


        //根据选入comboBox的文本跳转到目标页面
        searchBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                try {
                    tryChange(newValue);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //load complete
        statueLabel.setText("加载完成");
    }

    private void tryChange(String newValue) throws IOException {
        for (int i = 0; i < menuList.size(); i++){
            Map<String,String> menu = menuList.get(i);
            if (newValue.equals(menu.get("name"))) {
                tabChange(menu.get("url"));
                return;
            }
        }
    }


    protected void setTabChange(Button button, String url) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource(url));
        BorderPane newPane = new BorderPane(fxmlLoader.load());
        button.setOnAction(e ->
                borderPane.setCenter(newPane)
        );
    }


    protected void tabChange(String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource(url));
        BorderPane newPane = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(newPane);
    }

    protected void setSearchBox() throws IOException {
        //示例
        String target = searchBox.getEditor().getText();
//        if (target != "") {
//            searchBox.getItems().clear();
//        } else {
//            tryChange(target);
//
//            return;
//        }
        if(target == ""){
            searchBox.getItems().clear();
            searchBox.getItems().addAll(menuListOnlyName);
            searchBox.show();
            return;
        }
        searchBox.getItems().clear();
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
        List<Map<String,String>> list = httpRequestUtils.searchMenu(AppStore.getUser().getUser_type_id(),target);
        List menus = new ArrayList<Map<String,String>>();
        for (int i = 0; i < list.size(); i++) {
            menus.add(list.get(i).get("name"));
        }
        searchBox.getItems().addAll(menus);
        if (menus.get(0).equals("未找到相关页面"))
            searchBox.setEditable(false);
        searchBox.show();
    }


}
