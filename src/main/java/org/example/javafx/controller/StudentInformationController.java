package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class StudentInformationController {

    @FXML
    private Label GPALabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label admission_dateLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Label collegeLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button export_to_PDFButton;

    @FXML
    private Label father_nameLabel;

    @FXML
    private Label father_phone_numberLabel;

    @FXML
    private Label father_workLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label identityLabel;

    @FXML
    private Label identity_numberLabel;

    @FXML
    private Label majorLabel;

    @FXML
    private Label mother_nameLabel;

    @FXML
    private Label mother_phone_numberLabel;

    @FXML
    private Label mother_workLabel;

    @FXML
    private ImageView personImage;

    @FXML
    private Label person_idLabel;

    @FXML
    private Label person_nameLabel;

    @FXML
    private TextArea personal_profileLabel;

    @FXML
    private Label phone_numberLabel;

    @FXML
    public void initData(Map<String, String> selectedStudent) {
        String studentId = selectedStudent.get("id");
        if (studentId == null || studentId.isEmpty()) {
            System.out.println("Error: No student ID provided.");
            return;
        }
        DataRequest req = new DataRequest();
        req.add("id", studentId);
        Result studentResult = HttpRequestUtils.request("/student/getStudentInfo", req);
        if (studentResult != null && studentResult.getCode() == 200) {
            Map<String, Object> studentInfo = (Map<String, Object>) studentResult.getData();
            person_idLabel.setText((String)studentInfo.get("person_num"));
            person_nameLabel.setText((String) studentInfo.get("student_name"));
            genderLabel.setText((String) studentInfo.get("gender"));
            birthdayLabel.setText((String) studentInfo.get("birthday"));
            identityLabel.setText((String) studentInfo.get("identity"));
            identity_numberLabel.setText((String) studentInfo.get("identity_number"));
            phone_numberLabel.setText((String) studentInfo.get("phone_number"));
            emailLabel.setText((String) studentInfo.get("email"));
            //admission_dateLabel.setText((String) studentInfo.get("admission_date"));
            //collegeLabel.setText((String) studentInfo.get("college"));
            majorLabel.setText((String) studentInfo.get("major"));
            classLabel.setText((String) studentInfo.get("class"));
            degreeLabel.setText((String) studentInfo.get("degree"));
            //GPALabel.setText(studentInfo.get("GPA").toString());
            //personal_profileLabel.setText((String) studentInfo.get("personalProfile"));
            List<Map<String, Object>> studentFamilies = (List<Map<String, Object>>) studentInfo.get("studentFamilies");
            if (studentFamilies != null && !studentFamilies.isEmpty()) {
                Map<String, Object> firstFamilyMember = studentFamilies.get(0); // 假设只取第一个家庭成员信息
                father_nameLabel.setText((String) firstFamilyMember.get("name"));
                father_phone_numberLabel.setText((String) firstFamilyMember.get("phone"));
                father_workLabel.setText((String) firstFamilyMember.get("job"));
                addressLabel.setText((String) firstFamilyMember.get("address"));
                if (studentFamilies.size() > 1) {
                    Map<String, Object> secondFamilyMember = studentFamilies.get(1);
                    mother_nameLabel.setText((String) secondFamilyMember.get("name"));
                    mother_phone_numberLabel.setText((String) secondFamilyMember.get("phone"));
                    mother_workLabel.setText((String) secondFamilyMember.get("job"));
                }
            }

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText((studentResult != null && studentResult.getMsg() != null) ? studentResult.getMsg() : "Failed to fetch student information.");
            alert.showAndWait();
        }
        try {
            // 将"path_to_your_image"改为实际图片文件的路径
            FileInputStream inputStream = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\b_ceeab09a7a242063992bbc5b656ffba3.jpg");
            Image image = new Image(inputStream);
            personImage.setImage(image);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("错误：找不到图片文件。");
            alert.showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    void onExitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }
}
