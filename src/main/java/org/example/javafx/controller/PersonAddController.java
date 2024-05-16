package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Base64;

public class PersonAddController {

    @FXML
    private DatePicker birthdayDataPicker;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField emailTextField;

    @FXML
    private CheckBox femaleCheckBox;

    @FXML
    private TextField identityTextField;

    @FXML
    private TextField identity_numberTextField;

    @FXML
    private CheckBox maleCheckBox;

    @FXML
    private Button okButton;

    @FXML
    private TextField person_nameTextField;

    @FXML
    private TextField person_numTextField;

    @FXML
    private TextField phone_numberTextField;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private ComboBox user_typeComboBox;

    @FXML
    private TextField departmentTextField;

    @FXML
    private ImageView photoView;

    @FXML
    public void initialize() {
        // 只有当user_typeComboBox的项目为空时，才添加这些项目
        if (user_typeComboBox.getItems().isEmpty()) {
            user_typeComboBox.getItems().addAll("学生", "教师", "管理员");
        }
        // 当maleCheckBox被选中时，取消选中femaleCheckBox
        maleCheckBox.setOnAction(event -> {
            if (maleCheckBox.isSelected()) {
                femaleCheckBox.setSelected(false);
            }
        });

        // 当femaleCheckBox被选中时，取消选中maleCheckBox
        femaleCheckBox.setOnAction(event -> {
            if (femaleCheckBox.isSelected()) {
                maleCheckBox.setSelected(false);
            }
        });

        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\css\\nobodyPhoto.png");
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(fileInputStream);
        photoView.setImage(image);
    }


    public boolean validateInput() {
        return !person_nameTextField.getText().trim().isEmpty() &&
                !phone_numberTextField.getText().trim().isEmpty() &&
                !identityTextField.getText().trim().isEmpty() &&
                !person_numTextField.getText().trim().isEmpty() &&
                !birthdayDataPicker.getEditor().getText().trim().isEmpty() &&
                user_typeComboBox.getValue() != null && !user_typeComboBox.getValue().toString().trim().isEmpty() &&
                !emailTextField.getText().trim().isEmpty() &&
                !identity_numberTextField.getText().trim().isEmpty() &&
                !departmentTextField.getText().trim().isEmpty()
                && (maleCheckBox.isSelected() || femaleCheckBox.isSelected());
    }
    @FXML
    void onCancelButtonClickAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    void onOkButtonClickAction() {
        if (!validateInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("请填写所有项");
            alert.showAndWait();
        }
        else  {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_name", person_nameTextField.getText().trim());
            dataRequest.add("phone_number", phone_numberTextField.getText().trim());
            dataRequest.add("identity", identityTextField.getText().trim());
            dataRequest.add("person_num", person_numTextField.getText().trim());
            dataRequest.add("birthday", birthdayDataPicker.getEditor().getText().trim());
            dataRequest.add("user_type", user_typeComboBox.getValue().toString().trim());
            dataRequest.add("email", emailTextField.getText().trim());
            dataRequest.add("identity_number", identity_numberTextField.getText().trim());
            dataRequest.add("department", departmentTextField.getText().trim());
            if (maleCheckBox.isSelected()) {
                dataRequest.add("gender_id", "1");
                dataRequest.add("gender", "男");
            }
            if (femaleCheckBox.isSelected()) {
                dataRequest.add("gender_id", "2");
                dataRequest.add("gender", "女");
            }
            Result result = HttpRequestUtils.request("/person/addPerson", dataRequest);
            if (result.getCode() == 200){
                Stage stage = (Stage) cancelButton.getScene().getWindow();
                stage.close();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("提示");
                alert.setHeaderText("添加成功");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("添加失败");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void onUploadPhotoButtonClickAction() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());
        if (selectedFile != null) {
            HttpRequestUtils.uploadFile("/user/uploadPhoto", selectedFile.getPath(), "photo", person_numTextField.getText());
            person_numTextField.setDisable(true);
            User user = AppStore.getUser();
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num",person_numTextField.getText());
            String str = HttpRequestUtils.request("/user/getPhoto", dataRequest).getData().toString();
            byte[] data = Base64.getDecoder().decode(str);
            if (data != null) {
                Image image1 = new Image(new ByteArrayInputStream(data));
                photoView.setImage(image1);
            }
        }
    }

}
