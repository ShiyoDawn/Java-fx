package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FeeController {

    @FXML
    private TableColumn<Map, String> activityColumn;

    @FXML
    private TableColumn<Map, String> activityDetailColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private TableColumn<Map, String> dateColumn;

    @FXML
    private AnchorPane gloryAnchorPane;

    @FXML
    private BorderPane gloryBorderPane;

    @FXML
    private TableColumn<Map, String> id;

    @FXML
    private TableColumn<Map, String> moneyColumn;

    @FXML
    private Button queryButton;

    @FXML
    private Button resetButton;

    @FXML
    private ComboBox studentComboBox;

    @FXML
    private TableColumn<Map, String> studentNameColumn;

    @FXML
    private TableColumn<Map, String> studentNumColumn;

    //-----------------------------------
    private List<Map> feeList;

    private ObservableList<Map> observableList = FXCollections.observableArrayList();


    //-----------------------------------

    @FXML
    public void onAddButtonClick() {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/org/example/javafx/fee-add.fxml"));
        try {
            Parent parent = fxmlLoader.load();
            Map map = new HashMap();
            map.put("id", String.valueOf(feeList.size()));
            FeeAddController feeAddController = fxmlLoader.getController();
            feeAddController.initialize(map);
            stage.setScene(new Scene(parent));
            stage.setTitle("添加学生费用信息");
            stage.setResizable(false);
            stage.showAndWait();
            onQueryButtonClick();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        onQueryButtonClick();
    }

    @FXML
    public void onDeleteButtonClick() {
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("提示");
            alert.setContentText("请先选择要删除的数据");
            alert.showAndWait();
        } else {
            String msg = CommonMethod.alertButton("删除");
            if (msg == "确认") {
                for (Map feeMap : selected) {
                    DataRequest dataRequest = new DataRequest();
                    dataRequest.add("id", feeMap.get("id"));
                    HttpRequestUtils.request("/fee/deleteFee", dataRequest);
                }
                onQueryButtonClick();
                onQueryButtonClick();
            }
        }
    }

    @FXML
    private void onQueryButtonClick() {
        Result result = null;
        String student_name = null;
        Object student = studentComboBox.getValue();
        if (student != null) {
            student_name = student.toString();
        }
        if (student_name == "请选择学生") {
            studentComboBox.setPromptText("请选择学生");
            student_name = null;
        }
        if (student_name != null) {
            List<Map> studentList = CommonMethod.filter(feeList, "student_name", student_name);
            if (studentList == null || studentList.size() == 0) {
                observableList.clear();
                return;
            } else {
                observableList.clear();
                for (Map studentMap : studentList) {
                    observableList.add(studentMap);
                }
                dataTableView.setItems(observableList);
                return;
            }
        } else if (student_name == null) {
            result = HttpRequestUtils.request("/fee/getFeeList", new DataRequest());
        }
        setTableViewData(result);

    }

    @FXML
    private void onResetButtonClick() {
        Result result = null;
        studentComboBox.setValue(null);
        studentComboBox.setPromptText("请选择学生");
        result = HttpRequestUtils.request("/fee/getFeeList", new DataRequest());
        setTableViewData(result);
    }

    public void setTableViewData(Result result) {
        observableList.clear();
        if (result.getData() instanceof Map) {
            Map feeMap = (Map) result.getData();
            observableList.add(feeMap);
        } else if (result.getData() instanceof ArrayList) {
            //Button editButton;
            feeList = (ArrayList) result.getData();
            for (Map feeMap : feeList) {
                observableList.add(feeMap);
            }
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    public void initialize() {
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory<>("student_num"));
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        dateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        moneyColumn.setCellValueFactory(new MapValueFactory<>("money"));
        activityColumn.setCellValueFactory(new MapValueFactory<>("activity"));
        activityDetailColumn.setCellValueFactory(new MapValueFactory<>("activity_detail"));

        DataRequest dataRequest = new DataRequest();
        List studentList = new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", dataRequest);
        Map cancelStudent = new HashMap();
        cancelStudent.put("cancelStudent", "请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        List<Map> studentListMap = (List<Map>) studentResult.getData();
        for (Map student : studentListMap) {
            studentList.add(student.get("student_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        studentComboBox.setEditable(true);


        onResetButtonClick();
    }
}
