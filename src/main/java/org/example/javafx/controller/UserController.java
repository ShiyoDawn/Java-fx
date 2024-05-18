package org.example.javafx.controller;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.sql.SQLOutput;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {


    @FXML
    private Label birthdayLabel;

    @FXML
    private Label departmentLabel;

    @FXML
    private Button editPasswordButton;

    @FXML
    private Label emailLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label identityLabel;

    @FXML
    private Button logOutButton;

    @FXML
    private Label nameLabel;

    @FXML
    private Label numLabel;

    @FXML
    private Label passwordLabel;

    @FXML
    private ImageView photoView;

    @FXML
    private Button resetButton;

    @FXML
    private Label statusLabel;

    @FXML
    private Label telephoneLabel;

    @FXML
    private Label userTypeLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Tab viewTab;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private ImageView decorateView;

    @FXML
    public static User user;

    public Stage mainStage=new Stage();



    @FXML
    private void onEditPasswordButtonClick(ActionEvent event) {
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("person_num",AppStore.getUser().getPerson_num());
        Map user=(Map) HttpRequestUtils.request("/user/selectByNum",dataRequest).getData();
        FXMLLoader fxmlLoader = new FXMLLoader();
        URL url = getClass().getResource("/org/example/javafx/password-edit.fxml");
        fxmlLoader.setLocation(url);
        Map map=new HashMap();
        map.put("password",user.get("password"));
        try {
            Parent parent = fxmlLoader.load();
            PasswordController passwordController = fxmlLoader.getController();
            passwordController.initialize(map);
            Stage stage = new Stage();
            stage.setScene(new Scene(parent));
            stage.setTitle("修改密码");
            stage.showAndWait();
            onResetButtonClick();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    @FXML
    private void onLogOutButtonClick(ActionEvent event) {
        String msg= CommonMethod.alertButton("退出登录");
        Stage stage=(Stage) anchor.getScene().getWindow();
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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @FXML
    private void onResetButtonClick() {
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("person_num",AppStore.getUser().getPerson_num());
        Map user=(Map) HttpRequestUtils.request("/user/selectByNum",dataRequest).getData();
        /*DataRequest dataRequest=new DataRequest();
        dataRequest.add("person_num",user.getPerson_num());
        Result result = HttpRequestUtils.request("/user/selectPasswordByNum", dataRequest);
        Integer length = result.getData().toString().length();*/
        Integer length=user.get("password").toString().length();
        passwordLabel.setDisable(false);
        passwordLabel.setText("".join("", List.of("*".repeat(length))));
        passwordLabel.setDisable(true);
    }

    @FXML
    private void onUploadPhotoButtonClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());
        if (selectedFile != null) {
                /*Image image = new Image(new FileInputStream(selectedFile));
                photoView.setImage(image);*/
                /*BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
                byte[] imageBytes = byteArrayOutputStream.toByteArray();
                System.out.println(imageBytes);
                byteArrayOutputStream.close();

                User user = AppStore.getUser();
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("person_num", user.getPerson_num());
                dataRequest.add("photo", imageBytes);
                HttpRequestUtils.request("/person/updatePhoto", dataRequest);*/
            HttpRequestUtils.uploadFile("/user/uploadPhoto", selectedFile.getPath(), "photo", AppStore.getUser().getPerson_num());
            User user = AppStore.getUser();
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", user.getPerson_num());
            String str = HttpRequestUtils.request("/user/getPhoto", dataRequest).getData().toString();
            byte[] data = Base64.getDecoder().decode(str);
            if (data != null) {
                Image image1 = new Image(new ByteArrayInputStream(data));
                photoView.setImage(image1);
            }
            onResetButtonClick();
        }
    }


    @FXML
    public void initialize() {
        User user = AppStore.getUser();
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\image\\nobodyPhoto.png");
            Image image = new Image(fileInputStream);
            photoView.setImage(image);
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", user.getPerson_num());
            Result result = HttpRequestUtils.request("/user/getPhoto", dataRequest);
            if (result != null) {
                if (result.getCode() != -1) {
                    String str = result.getData().toString();
                    byte[] data = Base64.getDecoder().decode(str);
                    if (data != null) {
                        Image image1 = new Image(new ByteArrayInputStream(data));
                        photoView.setImage(image1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        numLabel.setText(user.getPerson_num());
        usernameLabel.setText(user.getPerson_num());
        Integer length = user.getPassword().length();
        passwordLabel.setText("".join("", List.of("*".repeat(length))));
        usernameLabel.setDisable(true);
        passwordLabel.setDisable(true);
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_num", user.getPerson_num());
        Result result = new Result();
        result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
        Map map = (Map) result.getData();
        if (user.getUser_type_id() == 3) {
            genderLabel.setText(map.get("gender_id").toString().equals("1.0") ? "男" : "女");
            userTypeLabel.setText(AppStore.confirmType(user));
            statusLabel.setText(map.get("identity").toString());
            telephoneLabel.setText(map.get("phone_number").toString());
            emailLabel.setText(map.get("email").toString());
            identityLabel.setText(map.get("identity_number").toString());
            departmentLabel.setText(map.get("department").toString());
            birthdayLabel.setText(map.get("birthday").toString());
            dataRequest.add("person_id", map.get("id"));
            result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
            map = (Map) result.getData();
            System.out.println(map);
            nameLabel.setText(map.get("student_name").toString());
        } else if (user.getUser_type_id() == 1) {
            nameLabel.setText("admin");
            userTypeLabel.setText("管理员");
        }
        if(mainStage.isShowing()){
            mainStage.close();
        }

        onResetButtonClick();

    }

}
