package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class PersonTableController {

    @FXML
    private TableColumn birthdayColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn emailColumn;

    @FXML
    private CheckBox fuzzySearchCheckBox;

    @FXML
    private TableColumn genderColumn;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn identityColumn;

    @FXML
    private TableColumn identity_numberColumn;

    @FXML
    private Button insertButton;

    @FXML
    private TableColumn person_nameColumn;

    @FXML
    private TableColumn person_numColumn;

    @FXML
    private TableColumn phone_numberColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox selectChoiceComboBox;

    @FXML
    private TextField selectTextField;

    @FXML
    private Button statisticButton;

    @FXML
    private TableView tableView;


    @FXML
    private Button upadateButton;

    @FXML
    private TableColumn user_typeColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_nameColumn.setCellValueFactory(new MapValueFactory<>("person_name"));
        genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
        phone_numberColumn.setCellValueFactory(new MapValueFactory<>("phone_number"));
        identityColumn.setCellValueFactory(new MapValueFactory<>("identity"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
        user_typeColumn.setCellValueFactory(new MapValueFactory<>("user_type"));
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
        identity_numberColumn.setCellValueFactory(new MapValueFactory<>("identity_number"));
        selectChoiceComboBox.getItems().addAll("姓名","性别","联系电话","身份","工号","出生日期","用户类型","邮箱","身份证号");
        Result personList = HttpRequestUtils.request("/person/getAll",new DataRequest());
        List<Map> persons = (List<Map>) personList.getData();
        if (persons==null) {
            return;
        }
        tableView.setItems(FXCollections.observableList(persons));
    }
    @FXML
    void onDeleteButtonClickAction() {

    }

    @FXML
    void onFuzzySearchStartAction() {

    }

    @FXML
    void onInsertButtonClickAction() {
        try {
            // 加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/person-add.fxml"));
            Parent root = loader.load();

            // 创建新的Stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("添加人员");
            stage.setScene(new Scene(root));

            // 显示新窗口
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onResetButtonAction() {

    }

    @FXML
    void onSelectButtonAction() {

    }

    @FXML
    void onSelectChoiceComboBoxAction() {

    }

    @FXML
    void onStatisticButtonClickAction() {

    }

    @FXML
    void onUpdateButtonClickAction() {
        try {
            // 加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/person-update.fxml"));
            Parent root = loader.load();

            // 创建新的Stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("添加人员");
            stage.setScene(new Scene(root));

            // 显示新窗口
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
