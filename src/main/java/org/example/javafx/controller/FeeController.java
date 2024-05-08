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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
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

    @FXML
    private Label one;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Tab viewTab;

    private static Label label = new Label();

    private static Label feeLabel = new Label();

    private static Label studentBalance = new Label();

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
            if(AppStore.getUser().getUser_type_id()==3){
                User user = AppStore.getUser();
                String student_name = null;
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("person_num", user.getPerson_num());
                Result result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
                Map personMap = (Map) result.getData();
                dataRequest.add("person_id", Integer.parseInt(personMap.get("id").toString().split("\\.")[0]));
                result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
                Map studentMap = (Map) result.getData();
                student_name = studentMap.get("student_name").toString();
                map.put("student_name", student_name);
                map.put("student_num", AppStore.getUser().getPerson_num());
            }
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
        Result tmpResult = new Result();
        User user = AppStore.getUser();
        String person_num = user.getPerson_num();
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("person_num", person_num);
        tmpResult = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
        Map map = (Map) tmpResult.getData();
        String student_name = null;
        if (map != null) {
            dataRequest.add("person_id", Integer.parseInt(map.get("id").toString().split("\\.")[0]));
            tmpResult = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
            map = (Map) tmpResult.getData();
            student_name = map.get("student_name").toString();
        }
        if (result.getData() instanceof Map) {
            Map feeMap = (Map) result.getData();
            if (student_name == null) {
                observableList.add(feeMap);
            } else {
                Double totalFee = 0.0;
                if (feeMap.get("student_name").equals(student_name)) {
                    totalFee = Double.parseDouble(feeMap.get("money").toString().substring(1, feeMap.get("money").toString().length()));
                    observableList.add(feeMap);
                }
                if (totalFee > 0) {
                    label.setText("+" + totalFee);
                } else {
                    label.setText(String.valueOf(totalFee));
                }
                label.setFont(javafx.scene.text.Font.font("System Bold", 15));
                label.setTextFill(javafx.scene.paint.Color.RED);
                label.setLayoutX(440.0);
                label.setLayoutY(20.0);
                if (!anchor.getChildren().contains(label)) {
                    anchor.getChildren().add(label);
                }
            }
        } else if (result.getData() instanceof ArrayList) {
            //Button editButton;
            feeList = (List<Map>) result.getData();
            if (student_name == null) {
                for (Map feeMap : feeList) {
                    observableList.add(feeMap);
                }
            } else {
                Double totalFee = 0.0;
                for (Map feeMap : feeList) {
                    if (feeMap.get("student_name").equals(student_name)) {
                        observableList.add(feeMap);
                        Double fee = Double.parseDouble(feeMap.get("money").toString().substring(1, feeMap.get("money").toString().length()));
                        if (feeMap.get("money").toString().charAt(0) == '+') {
                            totalFee += fee;
                        } else {
                            totalFee -= fee;
                        }
                    }
                }
                if (totalFee > 0) {
                    feeLabel.setText("+" + totalFee);
                } else {
                    feeLabel.setText(String.valueOf(totalFee));
                }
                feeLabel.setFont(javafx.scene.text.Font.font("System Bold", 15));
                feeLabel.setTextFill(javafx.scene.paint.Color.RED);
                feeLabel.setLayoutX(440.0);
                feeLabel.setLayoutY(20.0);
                if (!anchor.getChildren().contains(feeLabel)) {
                    anchor.getChildren().add(feeLabel);
                }
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

        studentComboBox.setEditable(true);
        User user = AppStore.getUser();
        if (user.getUser_type_id() == 3) {

            viewTab.setText("我的生活学习消费");

            deleteButton.setVisible(false);
            studentComboBox.setVisible(false);
            queryButton.setVisible(false);
            id.setVisible(false);

            one.setVisible(false);

            Text text = new Text("活动：");
            text.setFont(javafx.scene.text.Font.font(13));
            text.setLayoutX(700.0);
            text.setLayoutY(35.0);
            anchor.getChildren().add(text);

            TextField textField = new TextField();
            textField.setPromptText("请输入活动信息");
            textField.setLayoutX(755.0);
            textField.setLayoutY(18.0);
            textField.setPrefWidth(110);
            textField.setPrefHeight(23);
            anchor.getChildren().add(textField);

            Button button = new Button("查询");
            button.setLayoutX(900.0);
            button.setLayoutY(14.0);
            button.setPrefWidth(55);
            button.setPrefHeight(32);
            anchor.getChildren().add(button);

            Text tip = new Text("( 提醒：+代表该活动收入,-代表该活动支出 )");
            tip.setFont(javafx.scene.text.Font.font("System Bold", 14));
            tip.setLayoutX(80.0);
            tip.setLayoutY(34.0);
            tip.setFill(javafx.scene.paint.Color.RED);
            anchor.getChildren().add(tip);

            Text balance = new Text("收支：");
            balance.setFont(javafx.scene.text.Font.font("System Bold", 14));
            balance.setLayoutX(400.0);
            balance.setLayoutY(34.0);
            anchor.getChildren().add(balance);


            button.setOnAction(e -> {
                if (textField.getText() == "" || textField.getText() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("请输入活动信息");
                    alert.showAndWait();
                    return;
                }
                String activity = textField.getText();
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("activity", activity);
                List<Map> list = new ArrayList<>();
                list = CommonMethod.filter(feeList, "activity", activity);
                if (list.size() == 0) {
                    list = CommonMethod.filter(feeList, "activity_detail", activity);
                }
                Result result = new Result();
                result.setData(list);
                setTableViewData(result);
            });

            resetButton.setOnAction(e -> {
                textField.setPromptText("请输入活动信息");
                textField.setText("");
                onResetButtonClick();
            });

            addButton.setLayoutX(600.0);

        }

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


        onResetButtonClick();
    }
}
