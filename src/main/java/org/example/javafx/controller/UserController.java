package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
    private void onEditPasswordButtonClick(ActionEvent event) {

    }

    @FXML
    private void onLogOutButtonClick(ActionEvent event) {

    }

    @FXML
    private void onResetButtonClick() {
        User user = AppStore.getUser();
        Integer length = user.getPassword().length();
        passwordLabel.setText("".join("", List.of("*".repeat(length))));
    }

    @FXML
    public void initialize() {
        try {
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\css\\nobodyPhoto.jpg");
            Image image = new Image(fileInputStream);
            photoView.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        User user = AppStore.getUser();
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
        if(user.getUser_type_id()==3){
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
        }else if(user.getUser_type_id()==1){
            nameLabel.setText("admin");
            userTypeLabel.setText("管理员");
        }

        onResetButtonClick();

    }

}
