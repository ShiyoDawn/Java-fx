package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.*;
import java.util.Base64;
import java.util.Map;

public class PersonUpdateController {

    @FXML
    private DatePicker birthdayDatePicker;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField departmentTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private CheckBox femaleCheckBox;

    @FXML
    private TextField person_nameTextField;

    @FXML
    private TextField identityTextField;

    @FXML
    private TextField identity_numberTextField;

    @FXML
    private ImageView imageView;

    @FXML
    private CheckBox maleCheckBox;

    @FXML
    private TextField person_numTextField;

    @FXML
    private TextField phone_numberTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button uploadPhotoButton;

    @FXML
    private ComboBox user_typeComboBox;

    @FXML
    private Label infoLabel;
    private String id;


    @FXML
    public void initialize(Map<String, Object> selectedPerson) {
        person_numTextField.setDisable(true);
        id = (String) selectedPerson.get("id");
        person_numTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                person_numTextField.setText(oldValue);
            } else if (newValue.length() < 12) {
                infoLabel.setText("请输入12位工号");
                infoLabel.setTextFill(Color.RED);
            } else if (newValue.length() == 12) {
                infoLabel.setText("已满12位");
                infoLabel.setTextFill(Color.GREEN);
            } else {
                person_numTextField.setText(oldValue);
            }
        });
        identity_numberTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                identity_numberTextField.setText(oldValue);
            } else if (newValue.length() < 18) {
                infoLabel.setText("请输入18位身份证号");
                infoLabel.setTextFill(Color.RED);
            } else if (newValue.length() == 18) {
                infoLabel.setText("已满18位");
                infoLabel.setTextFill(Color.GREEN);
            } else {
                identity_numberTextField.setText(oldValue);
            }
        });
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
        if (selectedPerson != null) {
            person_nameTextField.setText(selectedPerson.get("person_name").toString());
            phone_numberTextField.setText(selectedPerson.get("phone_number").toString());
            identityTextField.setText(selectedPerson.get("identity").toString());
            person_numTextField.setText(selectedPerson.get("person_num").toString());
            birthdayDatePicker.getEditor().setText(selectedPerson.get("birthday").toString());
            user_typeComboBox.setValue(selectedPerson.get("user_type").toString());
            emailTextField.setText(selectedPerson.get("email").toString());
            identity_numberTextField.setText(selectedPerson.get("identity_number").toString());
            departmentTextField.setText(selectedPerson.get("department").toString());
            if (selectedPerson.get("gender").toString().equals("男")) {
                maleCheckBox.setSelected(true);
            } else if (selectedPerson.get("gender").toString().equals("女")) {
                femaleCheckBox.setSelected(true);
            }
//            User user = AppStore.getUser();
//            DataRequest dataRequest = new DataRequest();
//            dataRequest.add("person_num", person_numTextField.getText());
//            Result result = HttpRequestUtils.request("/user/getPhoto", dataRequest);
//            if(result.getCode()==-1){
//                FileInputStream fileInputStream = null;
//                try {
//                    fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\image\\nobodyPhoto.png");
//                } catch (FileNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//                Image image = new Image(fileInputStream);
//                imageView.setImage(image);
//            }
//            if (result != null) {
//                if (result.getCode() != -1) {
//                    String str = result.getData().toString();
//                    byte[] data = Base64.getDecoder().decode(str);
//                    if (data != null) {
//                        Image image1 = new Image(new ByteArrayInputStream(data));
//                        imageView.setImage(image1);
//                    }
//                }
//            }


        }
        FileInputStream fileInputStream = null;
        try {
            InputStream in = getClass().
                    getResourceAsStream("/org/example/javafx/image/nobodyPhoto.png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Image image = new Image(fileInputStream);
        imageView.setImage(image);
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_num", person_numTextField.getText());
        Result result = HttpRequestUtils.request("/user/getPhoto", dataRequest);
        if (result != null) {
            if (result.getCode() != -1) {
                String str = result.getData().toString();
                byte[] data = Base64.getDecoder().decode(str);
                if (data != null) {
                    Image image1 = new Image(new ByteArrayInputStream(data));
                    imageView.setImage(image1);
                }
            }
        }
    }

    @FXML
    void onCancelButtonClickAction(ActionEvent event) {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public boolean validateInput() {
        return !person_nameTextField.getText().trim().isEmpty() &&
                !phone_numberTextField.getText().trim().isEmpty() &&
                !identityTextField.getText().trim().isEmpty() &&
                !person_numTextField.getText().trim().isEmpty() &&
                !birthdayDatePicker.getEditor().getText().trim().isEmpty() &&
                user_typeComboBox.getValue() != null && !user_typeComboBox.getValue().toString().trim().isEmpty() &&
                !emailTextField.getText().trim().isEmpty() &&
                !identity_numberTextField.getText().trim().isEmpty() &&
                !departmentTextField.getText().trim().isEmpty()
                && (maleCheckBox.isSelected() || femaleCheckBox.isSelected());
    }

    @FXML
    void onSaveButtonClickAction(ActionEvent event) {
        if (!validateInput()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("请填写所有项");
            alert.showAndWait();
            return;
        }
        // 验证学号
        if (person_numTextField.getText().trim().length() != 12) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("学号必须是12位");
            alert.showAndWait();
            return;
        }

        // 验证身份证号
        if (identity_numberTextField.getText().trim().length() != 18) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("身份证号必须是18位");
            alert.showAndWait();
            return;
        }
        // 验证邮箱地址
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!emailTextField.getText().trim().matches(emailRegex)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("邮箱地址格式不合法");
            alert.showAndWait();
            return;
        }
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_name", person_nameTextField.getText().trim());
        dataRequest.add("phone_number", phone_numberTextField.getText().trim());
        dataRequest.add("identity", identityTextField.getText().trim());
        dataRequest.add("person_num", person_numTextField.getText().trim());
        dataRequest.add("birthday", birthdayDatePicker.getEditor().getText().trim());
        dataRequest.add("user_type", user_typeComboBox.getValue().toString().trim());
        dataRequest.add("email", emailTextField.getText().trim());
        dataRequest.add("identity_number", identity_numberTextField.getText().trim());
        dataRequest.add("department", departmentTextField.getText().trim());
        dataRequest.add("id", id);
        if (maleCheckBox.isSelected()) {
            dataRequest.add("gender_id", "1");
            dataRequest.add("gender", "男");
        }
        if (femaleCheckBox.isSelected()) {
            dataRequest.add("gender_id", "2");
            dataRequest.add("gender", "女");
        }
        Result result = HttpRequestUtils.request("/person/updatePerson", dataRequest);
        if (result.getCode() == 200) {
            Stage stage = (Stage) saveButton.getScene().getWindow();
            stage.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setHeaderText("修改成功");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("修改失败");
            alert.showAndWait();
        }

    }

    @FXML
    private void onUploadPhotoButtonClick(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg"));
        File selectedFile = fileChooser.showOpenDialog(uploadPhotoButton.getScene().getWindow());
        if (selectedFile != null) {
            HttpRequestUtils.uploadFile("/user/uploadPhoto", selectedFile.getPath(), "photo", person_numTextField.getText());
            person_numTextField.setDisable(true);
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", person_numTextField.getText());
            String str = HttpRequestUtils.request("/user/getPhoto", dataRequest).getData().toString();
            byte[] data = Base64.getDecoder().decode(str);
            if (data != null) {
                Image image1 = new Image(new ByteArrayInputStream(data));
                imageView.setImage(image1);
            }
        }
    }

}
