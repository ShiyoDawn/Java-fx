package org.example.javafx.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;

import java.util.Map;

public class PersonController {

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


    public void initData(Map<String, String> selectedStudent) {
    }
}
