package org.example.javafx.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;
import org.example.javafx.util.ElementsTool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainFrameController {
    Node center;

    @FXML
    BorderPane borderPane;

    @FXML
    HBox header;

    @FXML
    Label userLabel;
    @FXML
    VBox vBox = new VBox(10);

    @FXML Button minButton;

    @FXML Button closeButton;
    @FXML HBox topBox;

    @FXML Button resizeButton;

    @FXML
    ComboBox searchBox;

    @FXML
    Button dashBoardButton = new Button();

    @FXML Button connectButton;

    @FXML
    Label statueLabel;

    @FXML
    Button searchTabButton;

    @FXML Button changeButton;

    List<Map<String,String>> menuList;

    List menuListOnlyName = new ArrayList<>();

    Map<Button,Integer> buttons = new HashMap<>();

    @FXML
    Button change;

    public Stage mainStage=new Stage();


    @FXML
    public void initialize() throws IOException, InterruptedException {
        change.setOnAction(e -> {
            String msg= CommonMethod.alertButton("退出登录");
            Stage stage=(Stage) borderPane.getScene().getWindow();
            if(stage.isShowing()){
                mainStage.close();
                mainStage=new Stage();
            }
            if(msg=="确认"){
                stage.close();
                var fxmlLoader = new FXMLLoader(MainApplication.class.getResource("base/login-view.fxml"));
                try {
                    Scene scene = new Scene(fxmlLoader.load(), 670, 440);
                    scene.setFill(Color.TRANSPARENT);
                    mainStage.setScene(scene);
                    mainStage.setTitle("登录");
                    LoginController loginController=fxmlLoader.getController();
                    loginController.initialize(mainStage);

                    mainStage.initStyle(StageStyle.TRANSPARENT); // 修改窗口样式
                    mainStage.show();
                } catch (IOException E) {
                    throw new RuntimeException(E);
                }
            }
        });

        menuList = new HttpRequestUtils().getMenu(AppStore.getUser().getUser_type_id());
        //加载仪表盘界面为初始界面
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        BorderPane dashboard = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(dashboard);
        Result result=new Result();
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_num", AppStore.getUser().getPerson_num());
        result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
        Map map = (Map) result.getData();
        userLabel.setText("欢迎您:  "+map.get("person_name") + (AppStore.confirmType(AppStore.getUser())=="学生"?"同学":AppStore.confirmType(AppStore.getUser())=="教师"?"老师":"管理员"));
        userLabel.setTextFill(Color.WHITE);
        setTabChange(dashBoardButton, "dashboard-view.fxml");
        stageMove();
        connectButton.setOnAction(e -> {
            try {
                FXMLLoader messageFxml = new FXMLLoader();
                messageFxml.setLocation(MainApplication.class.getResource("connect.fxml"));
                Parent root = messageFxml.load();
                // 创建新的Stage
                Stage newStage = new Stage();
                newStage.initStyle(StageStyle.DECORATED);
                newStage.setTitle("联系我们");
                newStage.setScene(new Scene(root));
                newStage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        ElementsTool tool = new ElementsTool();
        tool.setCloseButton(closeButton);
        tool.setMinButton(minButton);
        /*tool.setResizeButton(resizeButton);
        resizeButton.setDisable(true);
        resizeButton.setTextFill(Color.WHITE);*/
        /*// 尝试实现全屏功能
        Scale scale=new Scale();
        double initialWidth=MainApplication.getMainStage().getWidth();
        double initialHeight=MainApplication.getMainStage().getHeight();

        MainApplication.getMainStage().widthProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double scaleFactor=Math.min(screenBounds.getMaxX() / initialWidth, screenBounds.getMaxY() / initialHeight);
            scale.setX(scaleFactor-1.1);
        });
        MainApplication.getMainStage().heightProperty().addListener((observable, oldValue, newValue) -> {
            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            double scaleFactor=Math.min(screenBounds.getMaxX() / initialWidth, screenBounds.getMaxY() / initialHeight);
            scale.setY(scaleFactor-1.1);
        });

        resizeButton.setOnAction(e->{
            MainApplication.getMainStage().setFullScreen(!MainApplication.getMainStage().isFullScreen());
            borderPane.getTransforms().add(scale);
            System.out.println(scale.getX()+" "+scale.getY());
        });

        MainApplication.getMainStage().addEventHandler(KeyEvent.KEY_PRESSED, e->{
            if(e.getCode()== KeyCode.ESCAPE){
                if(MainApplication.getMainStage().isFullScreen()){
                    MainApplication.getMainStage().setFullScreen(false);
                    Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
                    MainApplication.getMainStage().widthProperty().addListener((observable, oldValue, newValue) -> {
                        double scaleFactor=Math.min(initialHeight/screenBounds.getMaxX(), initialWidth/screenBounds.getMaxY());
                        scale.setX(scaleFactor);
                    });
                    MainApplication.getMainStage().heightProperty().addListener((observable, oldValue, newValue) -> {
                        double scaleFactor=Math.min(initialHeight/screenBounds.getMaxX(), initialWidth/screenBounds.getMaxY());
                        scale.setY(scaleFactor);
                    });
                    borderPane.getTransforms().add(scale);
                }
                System.out.println(scale.getX()+" "+scale.getY());
            }
        });*/

        //初始化页面切换
        if (vBox.getChildren().size() > 1){
            vBox.getChildren().remove(1,vBox.getChildren().size() - 1);
        }
        for (int i = 1; i < menuList.size(); i++) {
            Button newButton = new Button(menuList.get(i).get("name"));
            buttons.put(newButton,0);
            newButton.setPrefHeight(40);
            newButton.setPrefWidth(90);
            vBox.getChildren().add(i,newButton);
            setTabChange(newButton,menuList.get(i).get("url"));
        }

        vBox.setStyle("-fx-background-color: rgba(189,21,21,0);");


        searchBox.setEditable(true);

        for (int i = 0; i < menuList.size(); i++) {
            menuListOnlyName.add(menuList.get(i).get("name"));
        }
        searchBox.getItems().addAll(menuListOnlyName);
        searchTabButton.setOnAction(e ->
        {
            try {
                setSearchBox();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        changeButton.setOnAction(e -> {
            try {
                tryChange(searchBox.getEditor().getText());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        //根据选入comboBox的文本跳转到目标页面

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


    protected void setTabChange(Button button, String url){


        button.setOnAction(e -> {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(MainApplication.class.getResource(url));
            BorderPane newPane = null;
            try {
                newPane = new BorderPane(fxmlLoader.load());
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            borderPane.setCenter(newPane);
        });
    }


    protected void tabChange(String url) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource(url));
        BorderPane newPane = new BorderPane(fxmlLoader.load());
        borderPane.setCenter(newPane);
    }

    protected void setSearchBox() throws IOException {
        String target = searchBox.getEditor().getText();
        if(target == ""){
            searchBox.getItems().clear();
            searchBox.getItems().addAll(menuListOnlyName);
            searchBox.show();
            return;
        }
        searchBox.getItems().clear();
        //根据可查看的页面获取模糊查询List
        HttpRequestUtils httpRequestUtils = new HttpRequestUtils();
        List<Map<String,String>> list = httpRequestUtils.searchMenu(AppStore.getUser().getUser_type_id(),target);
        List menus = new ArrayList<Map<String,String>>();
        for (int i = 0; i < list.size(); i++) {
            menus.add(list.get(i).get("name"));
        }
        searchBox.show();
        searchBox.getItems().addAll(menus);
        if (menus.get(0).equals("未找到相关页面"))
        searchBox.show();
    }


    double x1;
    double y1;
    double x_stage;
    double y_stage;
    private void stageMove() {
        header.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override public void handle(MouseEvent m) {
                //计算
                MainApplication.getMainStage().setX(x_stage + m.getScreenX() - x1);
                MainApplication.getMainStage().setY(y_stage + m.getScreenY() - y1);
            }
        });
        header.setOnDragEntered(null);
        header.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override public void handle(MouseEvent m) {

                //按下鼠标后，记录当前鼠标的坐标
                x1 = m.getScreenX();
                y1 = m.getScreenY();
                x_stage = MainApplication.getMainStage().getX();
                y_stage = MainApplication.getMainStage().getY();
            }
        });
    }
    public Node getCenter(){
        center = borderPane.getCenter();
        return center;
    }

}
