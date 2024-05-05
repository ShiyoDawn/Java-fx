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

public class ActivityController {

    @FXML
    private TableColumn cActivity_nameColumn;

    @FXML
    private TableColumn cActivity_typeColumn;

    @FXML
    private Button cAddButton;

    @FXML
    private ComboBox cComboBox;

    @FXML
    private TableColumn cDateColumn;

    @FXML
    private Button cDeleteButton;

    @FXML
    private CheckBox cFuzzySearchCheckBox;

    @FXML
    private TableColumn cIdColumn;

    @FXML
    private TableColumn cNameColumn;

    @FXML
    private Button cResetButton;

    @FXML
    private TableColumn cScoreColumn;

    @FXML
    private Button cSearchButton;

    @FXML
    private TextField cTextField;

    @FXML
    private Button cUpdateButton;

    @FXML
    private TableColumn dActivityColumn;

    @FXML
    private TableColumn dActivity_type;

    @FXML
    private Button dAddButton;

    @FXML
    private ComboBox dComboBox;

    @FXML
    private TableColumn dDaily_idColumn;

    @FXML
    private TableColumn dDateColumn;

    @FXML
    private Button dDeleteButton;

    @FXML
    private CheckBox dFuzzySearchCheckBox;

    @FXML
    private TableColumn dNameColumn;

    @FXML
    private Button dResetButton;

    @FXML
    private TableColumn dScoreColumn;

    @FXML
    private Button dSearchiButton;

    @FXML
    private TextField dTextField;

    @FXML
    private Button dUpdateButton;

    @FXML
    private TableView cTableView;

    @FXML
    public void initialize() {
        cIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        cNameColumn.setCellValueFactory(new MapValueFactory<>("person_name"));
        cActivity_nameColumn.setCellValueFactory(new MapValueFactory<>("activity_name"));
        cDateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        cScoreColumn.setCellValueFactory(new MapValueFactory<>("score"));
        cActivity_typeColumn.setCellValueFactory(new MapValueFactory<>("activity_type_id"));
        cComboBox.getItems().addAll("姓名", "活动类型", "日期", "分数", "活动名称");
        Result result = HttpRequestUtils.request("/evaluate/getEvaluateList", new DataRequest());
        List<Map> list = (List<Map>) result.getData();
        if (list == null || list.isEmpty()) {
            return;
        }
        cTableView.setItems(FXCollections.observableList(list));
    }
    @FXML
    void onCAddButtonClickAction() {
        try {
            // 加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/activity-add.fxml"));
            Parent root = loader.load();

            // 创建新的窗口
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("添加活动");
            stage.setScene(new Scene(root));

            // 显示新窗口
            stage.showAndWait();
            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onCComboBoxAction(ActionEvent event) {

    }

    @FXML
    void onCDeleteButtonClickAction(ActionEvent event) {

    }

    @FXML
    void onCFuzzySearchCheckBoxAction(ActionEvent event) {

    }

    @FXML
    void onCResetAction(ActionEvent event) {

    }

    @FXML
    void onCSearchButtonClickAction(ActionEvent event) {

    }

    @FXML
    void onCUpdateButtonClickAction(ActionEvent event) {

    }
    private void refresh() {
        Result result = HttpRequestUtils.request("/evaluate/getEvaluateList", new DataRequest());
        List<Map> list = (List<Map>) result.getData();
        if (list == null || list.isEmpty()) {
            return;
        }
        cTableView.setItems(FXCollections.observableList(list));
    }

}
