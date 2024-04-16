package org.example.javafx.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Course;
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

    @FXML
    Button courseCenterButton = new Button();

    @FXML
    Button studentCenterButton = new Button();

    @FXML
    Button activityCenterButton = new Button();

    @FXML
    Button userCenterButton = new Button();

    @FXML
    Button scoreCenterButton = new Button();

    @FXML
    Label statueLabel;

    @FXML
    Button searchButton;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        //加载仪表盘界面为初始界面
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane dashboard = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(dashboard);
        userLabel.setText(AppStore.getUser().getPerson_num() + "/" + AppStore.confirmType(AppStore.getUser()));
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
        Course course = new Course("1");
        DataRequest dataRequest = new DataRequest();
        httpRequestUtils.getCourse(dataRequest);


        //TODO 发送请求，根据用户type获取tab

        //初始化页面切换
        setTabChange(dashBoardButton, "dashboard-view.fxml");
        setTabChange(courseCenterButton, "course-view.fxml");
        setTabChange(studentCenterButton, "student-view.fxml");
        setTabChange(activityCenterButton, "activity-view.fxml");
        setTabChange(userCenterButton, "user-view.fxml");
        //setTabChange(scoreCenterButton,"score-view.fxml");

        searchBox.setEditable(true);
        List list = new ArrayList<>();
        list.add("仪表盘");
        list.add("课程管理");
        list.add("学生管理");
        list.add("实践活动");
        list.add("用户中心");
        //list.add("分数管理");
        searchBox.getItems().addAll(list);
        searchButton.setOnAction(e ->
        {
            try {
                setSearchBox();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        searchBox.valueProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                try {
                    if (newValue.equals("学生管理")) {
                        tabChange("student-view.fxml");
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        //load complete
        statueLabel.setText("加载完成");
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
        if (target != "") {
            searchBox.getItems().clear();
        } else if (target.equals("学生管理")) {
            tabChange("student-view.fxml");
            return;
        }
        searchBox.show();

        //TODO 模糊查找根据target请求目标
        searchBox.getItems().addAll();
    }


}
