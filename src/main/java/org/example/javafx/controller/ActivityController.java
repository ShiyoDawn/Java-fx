package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ActivityController {

    @FXML
    private TableView<Map> practiceTableView;

    @FXML
    private TableView<Map> dailyTableView;

    @FXML
    private AnchorPane dailyAnchor;

    @FXML
    private AnchorPane practiceAnchor;

    @FXML
    private Button dailyUpdateButton;

    @FXML
    private TableColumn<Map, String> dailyActivityNameColumn;

    @FXML
    private TextField dailyActivityNameEditTextField;

    @FXML
    private TableColumn<Map, String> dailyActivityTypeColumn;

    @FXML
    private ComboBox dailyActivityTypeComboBox;

    @FXML
    private Button dailyAddButton;

    @FXML
    private Button dailyConfirmButton;

    @FXML
    private TableColumn<Map, String> dailyDateColumn;

    @FXML
    private Button dailyDeleteButton;

    @FXML
    private DatePicker dailyFromDatePicker;

    @FXML
    private TableColumn<Map, String> dailyIdColumn;

    @FXML
    private Button dailyQueryButton;

    @FXML
    private Button dailyResetButton;

    @FXML
    private Button dailyResetEditButton;

    @FXML
    private TableColumn<Map, String> dailyScoreColumn;

    @FXML
    private TextField dailyScoreEditTextField;

    @FXML
    private TextField dailyStudentInfoTextField;

    @FXML
    private TableColumn<Map, String> dailyStudentNameColumn;

    @FXML
    private TextField dailyStudentNameEditTextField;

    @FXML
    private TableColumn<Map, String> dailyStudentNumColumn;

    @FXML
    private TextField dailyStudentNumEditTextField;

    @FXML
    private DatePicker dailyToDatePicker;

    @FXML
    private Label one;

    @FXML
    private TableColumn<Map, String> practiceActivityNameColumn;

    @FXML
    private TextField practiceActivityNameEditTextField;

    @FXML
    private TableColumn<Map, String> practiceActivityTypeColumn;

    @FXML
    private ComboBox practiceActivityTypeComboBox;

    @FXML
    private Button practiceAddButton;

    @FXML
    private Button practiceConfirmButton;

    @FXML
    private TableColumn<Map, String> practiceDateColumn;

    @FXML
    private Button practiceDeleteButton;

    @FXML
    private DatePicker practiceFromDatePicker;

    @FXML
    private TableColumn<Map, String> practiceIdColumn;

    @FXML
    private Button practiceQueryButton;

    @FXML
    private Button practiceResetButton;

    @FXML
    private TableColumn<Map, String> practiceScoreColumn;

    @FXML
    private TextField practiceScoreEditTextField;

    @FXML
    private TextField practiceStudentInfoTextField;

    @FXML
    private TableColumn<Map, String> practiceStudentNameColumn;

    @FXML
    private TextField practiceStudentNameEditTextField;

    @FXML
    private TableColumn<Map, String> practiceStudentNumColumn;

    @FXML
    private TextField practiceStudentNumEditTextField;

    @FXML
    private DatePicker practiceToDatePicker;

    @FXML
    private Button practiceUpdateButton;

    @FXML
    private Button practiceResetEditButton;

    @FXML
    private Label two;

    //---------------------------------------------------------

    private ObservableList<Map> practiceObservableList = FXCollections.observableArrayList();
    private ObservableList<Map> dailyObservableList = FXCollections.observableArrayList();
    private List<Map> practiceList = new ArrayList<>();
    private List<Map> dailyList = new ArrayList<>();

    private static String[] practiceType = {"请选择活动类型", "学术科技", "培训讲座", "科技成果", "创新项目", "校外实习"};

    private static String[] dailyType = {"请选择活动类型", "体育活动", "外出旅游", "文艺演出", "聚会"};

    //---------------------------------------------------------


    @FXML
    void onDailyAddButtonClick() {
        dailyConfirmButton.setText("添加");
        onDailyResetEditButtonClick();
    }

    @FXML
    void onDailyConfirmButtonClick() {
        Map map = dailyTableView.getSelectionModel().getSelectedItem();
        DataRequest rawDataRequest = new DataRequest();
        if (map != null) {
            rawDataRequest.add("student_num", map.get("student_num").toString());
            rawDataRequest.add("student_name", map.get("student_name").toString());
            rawDataRequest.add("activity_name", map.get("activity_name").toString());
            rawDataRequest.add("activity_type", map.get("activity_type").toString());
            rawDataRequest.add("date", map.get("date").toString());
            rawDataRequest.add("score", map.get("score").toString());
        }
        if (dailyConfirmButton.getText() == "修改") {
            if (dailyStudentNumEditTextField.getText() != "" && dailyStudentNameEditTextField.getText() != "" && dailyActivityNameEditTextField.getText() != "" && dailyActivityTypeComboBox.getValue() != "请选择活动类型" && dailyFromDatePicker.getValue() != null && dailyToDatePicker.getValue() != null && dailyScoreEditTextField.getText() != "") {
                if (dailyFromDatePicker.getValue().isAfter(dailyToDatePicker.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("日期填写有误，请重新填写！");
                    alert.showAndWait();
                    return;
                }
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", dailyStudentNumEditTextField.getText());
                dataRequest.add("student_name", dailyStudentNameEditTextField.getText());
                dataRequest.add("activity_name", dailyActivityNameEditTextField.getText());
                dataRequest.add("activity_type", dailyActivityTypeComboBox.getValue());
                dataRequest.add("date", dailyFromDatePicker.getValue().toString() + "~" + dailyToDatePicker.getValue().toString());
                dataRequest.add("score", dailyScoreEditTextField.getText());
                String msg = CommonMethod.alertButton("修改");
                if (msg == "确认") {
                    Result result = HttpRequestUtils.request("/activity/deleteActivity", rawDataRequest);
                    result = HttpRequestUtils.request("/activity/insertActivity", dataRequest);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请填写完整信息");
                alert.showAndWait();
            }
        } else if (dailyConfirmButton.getText() == "添加") {
            if (dailyStudentNumEditTextField.getText() != "" && dailyStudentNameEditTextField.getText() != "" && dailyActivityNameEditTextField.getText() != "" && dailyActivityTypeComboBox.getValue() != "请选择活动类型" && dailyFromDatePicker.getValue() != null && dailyToDatePicker.getValue() != null && dailyScoreEditTextField.getText() != "") {
                if(practiceFromDatePicker.getValue().isAfter(practiceToDatePicker.getValue())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("日期填写有误，请重新填写！");
                    alert.showAndWait();
                    return;
                }
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", dailyStudentNumEditTextField.getText());
                dataRequest.add("student_name", dailyStudentNameEditTextField.getText());
                dataRequest.add("activity_name", dailyActivityNameEditTextField.getText());
                dataRequest.add("activity_type", dailyActivityTypeComboBox.getValue());
                dataRequest.add("date", dailyFromDatePicker.getValue().toString() + "~" + dailyToDatePicker.getValue().toString());
                dataRequest.add("score", dailyScoreEditTextField.getText());
                String msg = CommonMethod.alertButton("添加");
                if (msg == "确认") {
                    Result result = HttpRequestUtils.request("/activity/insertActivity", dataRequest);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请填写完整信息");
                alert.showAndWait();

            }
        }
        onDailyQueryButtonClick();
        onDailyResetEditButtonClick();
    }

    @FXML
    void onDailyDeleteButtonClick() {
        List<Map> selected = dailyTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择一条记录");
            alert.showAndWait();
        } else if (selected.size() == 1) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_num", selected.get(0).get("student_num").toString());
            dataRequest.add("student_name", selected.get(0).get("student_name").toString());
            dataRequest.add("activity_name", selected.get(0).get("activity_name").toString());
            dataRequest.add("activity_type", selected.get(0).get("activity_type").toString());
            dataRequest.add("date", selected.get(0).get("date").toString());
            dataRequest.add("score", selected.get(0).get("score").toString());
            String msg = CommonMethod.alertButton("删除");
            if (msg == "确认") {
                Result result = HttpRequestUtils.request("/activity/deleteActivity", dataRequest);
                onDailyQueryButtonClick();
                onDailyResetEditButtonClick();
            }
        } else {
            String msg = CommonMethod.alertButton("删除");
            if (msg == "确认") {
                for (Map map : selected) {
                    DataRequest dataRequest = new DataRequest();
                    dataRequest.add("student_num", map.get("student_num").toString());
                    dataRequest.add("student_name", map.get("student_name").toString());
                    dataRequest.add("activity_name", map.get("activity_name").toString());
                    dataRequest.add("activity_type", map.get("activity_type").toString());
                    dataRequest.add("date", map.get("date").toString());
                    dataRequest.add("score", map.get("score").toString());
                    Result result = HttpRequestUtils.request("/activity/deleteActivity", dataRequest);
                }
                onDailyQueryButtonClick();
                onDailyResetEditButtonClick();
            }
        }
    }

    @FXML
    void onDailyUpdateButtonClick() {
        Map map = dailyTableView.getSelectionModel().getSelectedItem();
        if (AppStore.getUser().getUser_type_id() == 3) {
            dailyStudentNumEditTextField.setDisable(false);
            dailyStudentNameEditTextField.setDisable(false);
            dailyActivityNameEditTextField.setDisable(false);
            dailyActivityTypeComboBox.setDisable(false);
            dailyFromDatePicker.setDisable(false);
            dailyToDatePicker.setDisable(false);
        }
        if (map != null) {
            dailyConfirmButton.setText("修改");
            dailyStudentNumEditTextField.setText(map.get("student_num").toString());
            dailyStudentNameEditTextField.setText(map.get("student_name").toString());
            dailyActivityNameEditTextField.setText(map.get("activity_name").toString());
            dailyActivityTypeComboBox.setValue(map.get("activity_type").toString());
            dailyFromDatePicker.setValue(LocalDate.parse(map.get("date").toString().split("~")[0]));
            dailyToDatePicker.setValue(LocalDate.parse(map.get("date").toString().split("~")[1]));
            dailyScoreEditTextField.setText(map.get("score").toString());
            dailyStudentNumEditTextField.setDisable(true);
            dailyStudentNameEditTextField.setDisable(true);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择一条记录");
            alert.showAndWait();
        }
    }

    @FXML
    void onDailyQueryButtonClick() {
        String studentInfo = dailyStudentInfoTextField.getText();
        if (dailyStudentInfoTextField.getText() == "" || dailyStudentInfoTextField.getText() == "请输入学生信息") {
            Result result = HttpRequestUtils.request("/activity/getActivityList", new DataRequest());
            setDailyTableViewData(result);
            return;
        }
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("student_num", studentInfo);
        List list = CommonMethod.filter(dailyList, "student_num", studentInfo);
        Result result = new Result();
        if (list.size() == 0) {
            dataRequest.add("student_name", studentInfo);
            list = CommonMethod.filter(dailyList, "student_name", studentInfo);
            if (list.size() == 0) {
                return;
            } else {
                result.setData(list);
            }
        } else {
            result.setData(list);
        }
        setDailyTableViewData(result);
        onDailyResetEditButtonClick();
    }

    @FXML
    void onDailyResetButtonClick() {
        dailyStudentInfoTextField.setText("");
        dailyStudentInfoTextField.setPromptText("请输入学生信息");
        dailyStudentNumEditTextField.setDisable(false);
        dailyStudentNameEditTextField.setDisable(false);
        if (AppStore.getUser().getUser_type_id() == 3) {
            dailyStudentNumEditTextField.setDisable(true);
            dailyStudentNameEditTextField.setDisable(true);
            dailyActivityTypeComboBox.setDisable(true);
            dailyActivityNameEditTextField.setDisable(true);
            dailyFromDatePicker.setDisable(true);
            dailyToDatePicker.setDisable(true);
            dailyScoreEditTextField.setDisable(true);
        }
        dailyStudentNumEditTextField.setText("");
        dailyStudentNameEditTextField.setText("");
        dailyActivityNameEditTextField.setText("");
        dailyActivityTypeComboBox.setValue("请选择活动类型");
        dailyFromDatePicker.setValue(LocalDate.now());
        dailyToDatePicker.setValue(LocalDate.now());
        dailyScoreEditTextField.setText("");


        Result result = HttpRequestUtils.request("/activity/getActivityList", new DataRequest());
        setDailyTableViewData(result);
    }

    @FXML
    void onDailyResetEditButtonClick() {
        dailyStudentNumEditTextField.setDisable(false);
        dailyStudentNameEditTextField.setDisable(false);
        if (AppStore.getUser().getUser_type_id() == 3) {
            dailyStudentNumEditTextField.setDisable(true);
            dailyStudentNameEditTextField.setDisable(true);
            dailyActivityTypeComboBox.setDisable(true);
            dailyActivityNameEditTextField.setDisable(true);
            dailyFromDatePicker.setDisable(true);
            dailyToDatePicker.setDisable(true);
            dailyScoreEditTextField.setDisable(true);
        }
        dailyStudentNumEditTextField.setText("");
        dailyStudentNameEditTextField.setText("");
        dailyActivityNameEditTextField.setText("");
        dailyActivityTypeComboBox.setValue("请选择活动类型");
        dailyFromDatePicker.setValue(LocalDate.now());
        dailyToDatePicker.setValue(LocalDate.now());
        dailyScoreEditTextField.setText("");
        dailyConfirmButton.setText("添加");
    }


    public void setDailyTableViewData(Result result) {
        dailyObservableList.clear();
        dailyList = (List<Map>) result.getData();
        if (AppStore.getUser().getUser_type_id() == 3) {
            dailyList = CommonMethod.filter(dailyList, "student_num", AppStore.getUser().getPerson_num());
        }
        for (Map map : dailyList) {
            for (String s : dailyType) {
                if (map.get("activity_type").equals(s)) {
                    dailyObservableList.add(map);
                    break;
                }
            }
        }
        dailyTableView.setItems(dailyObservableList);
    }

    //-------------------------------------------
    @FXML
    void onPracticeAddButtonClick() {
        practiceConfirmButton.setText("添加");
        onPracticeResetEditButtonClick();
    }

    @FXML
    void onPracticeConfirmButtonClick() {
        Map map = practiceTableView.getSelectionModel().getSelectedItem();
        DataRequest rawDataRequest = new DataRequest();
        if (map != null) {
            rawDataRequest.add("student_num", map.get("student_num").toString());
            rawDataRequest.add("student_name", map.get("student_name").toString());
            rawDataRequest.add("activity_name", map.get("activity_name").toString());
            rawDataRequest.add("activity_type", map.get("activity_type").toString());
            rawDataRequest.add("date", map.get("date").toString());
            rawDataRequest.add("score", map.get("score").toString());
        }
        if (practiceConfirmButton.getText() == "修改") {
            if (practiceStudentNumEditTextField.getText() != "" && practiceStudentNameEditTextField.getText() != "" && practiceActivityNameEditTextField.getText() != "" && practiceActivityTypeComboBox.getValue() != "请选择活动类型" && practiceFromDatePicker.getValue() != null && practiceToDatePicker.getValue() != null && practiceScoreEditTextField.getText() != "") {
                if (practiceFromDatePicker.getValue().isAfter(practiceToDatePicker.getValue())) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("日期填写有误，请重新填写！");
                    alert.showAndWait();
                    return;
                }
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", practiceStudentNumEditTextField.getText());
                dataRequest.add("student_name", practiceStudentNameEditTextField.getText());
                dataRequest.add("activity_name", practiceActivityNameEditTextField.getText());
                dataRequest.add("activity_type", practiceActivityTypeComboBox.getValue());
                dataRequest.add("date", practiceFromDatePicker.getValue().toString() + "~" + practiceToDatePicker.getValue().toString());
                dataRequest.add("score", practiceScoreEditTextField.getText());
                String msg = CommonMethod.alertButton("修改");
                if (msg == "确认") {
                    Result result = HttpRequestUtils.request("/activity/deleteActivity", rawDataRequest);
                    result = HttpRequestUtils.request("/activity/insertActivity", dataRequest);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请填写完整信息");
                alert.showAndWait();
            }
        } else if (practiceConfirmButton.getText() == "添加") {
            if (practiceStudentNumEditTextField.getText() != "" && practiceStudentNameEditTextField.getText() != "" && practiceActivityNameEditTextField.getText() != "" && practiceActivityTypeComboBox.getValue() != "请选择活动类型" && practiceFromDatePicker.getValue() != null && practiceToDatePicker.getValue() != null && practiceScoreEditTextField.getText() != "") {
                if(practiceFromDatePicker.getValue().isAfter(practiceToDatePicker.getValue())){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("日期填写有误，请重新填写！");
                    alert.showAndWait();
                    return;
                }
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", practiceStudentNumEditTextField.getText());
                dataRequest.add("student_name", practiceStudentNameEditTextField.getText());
                dataRequest.add("activity_name", practiceActivityNameEditTextField.getText());
                dataRequest.add("activity_type", practiceActivityTypeComboBox.getValue());
                dataRequest.add("date", practiceFromDatePicker.getValue().toString() + "~" + practiceToDatePicker.getValue().toString());
                dataRequest.add("score", practiceScoreEditTextField.getText());
                String msg = CommonMethod.alertButton("添加");
                if (msg == "确认") {
                    Result result = HttpRequestUtils.request("/activity/insertActivity", dataRequest);
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请填写完整信息");
                alert.showAndWait();
            }

        }
        onPracticeQueryButtonClick();
        onPracticeResetEditButtonClick();
    }

    @FXML
    void onPracticeDeleteButtonClick() {
        List<Map> selected = practiceTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择一条记录");
            alert.showAndWait();
        } else if (selected.size() == 1) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_num", selected.get(0).get("student_num").toString());
            dataRequest.add("student_name", selected.get(0).get("student_name").toString());
            dataRequest.add("activity_name", selected.get(0).get("activity_name").toString());
            dataRequest.add("activity_type", selected.get(0).get("activity_type").toString());
            dataRequest.add("date", selected.get(0).get("date").toString());
            dataRequest.add("score", selected.get(0).get("score").toString());
            String msg = CommonMethod.alertButton("删除");
            if (msg == "确认") {
                Result result = HttpRequestUtils.request("/activity/deleteActivity", dataRequest);
                onPracticeQueryButtonClick();
            }
        } else {
            String msg = CommonMethod.alertButton("删除");
            if (msg == "确认") {
                for (Map map : selected) {
                    DataRequest dataRequest = new DataRequest();
                    dataRequest.add("student_num", map.get("student_num").toString());
                    dataRequest.add("student_name", map.get("student_name").toString());
                    dataRequest.add("activity_name", map.get("activity_name").toString());
                    dataRequest.add("activity_type", map.get("activity_type").toString());
                    dataRequest.add("date", map.get("date").toString());
                    dataRequest.add("score", map.get("score").toString());
                    Result result = HttpRequestUtils.request("/activity/deleteActivity", dataRequest);
                }
                onPracticeQueryButtonClick();
            }
        }
    }

    @FXML
    void onPracticeQueryButtonClick() {
        String studentInfo = practiceStudentInfoTextField.getText();
        if (practiceStudentInfoTextField.getText() == "" || practiceStudentInfoTextField.getText() == "请输入学生信息") {
            Result result = HttpRequestUtils.request("/activity/getActivityList", new DataRequest());
            setPracticeTableViewData(result);
            onPracticeResetButtonClick();
            return;
        }
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("student_num", studentInfo);
        List list = CommonMethod.filter(practiceList, "student_num", studentInfo);
        Result result = new Result();
        if (list.size() == 0) {
            dataRequest.add("student_name", studentInfo);
            list = CommonMethod.filter(practiceList, "student_name", studentInfo);
            if (list.size() == 0) {
                return;
            } else {
                result.setData(list);
            }
        } else {
            result.setData(list);
        }
        setPracticeTableViewData(result);
        onPracticeResetEditButtonClick();
    }

    @FXML
    void onPracticeResetButtonClick() {
        practiceStudentInfoTextField.setText("");
        practiceStudentInfoTextField.setPromptText("请输入学生信息");
        practiceStudentNumEditTextField.setDisable(false);
        practiceStudentNameEditTextField.setDisable(false);
        if (AppStore.getUser().getUser_type_id() == 3) {
            practiceStudentNumEditTextField.setDisable(true);
            practiceStudentNameEditTextField.setDisable(true);
            practiceActivityTypeComboBox.setDisable(true);
            practiceActivityNameEditTextField.setDisable(true);
            practiceFromDatePicker.setDisable(true);
            practiceToDatePicker.setDisable(true);
            practiceScoreEditTextField.setDisable(true);
        }
        practiceStudentNumEditTextField.setText("");
        practiceStudentNameEditTextField.setText("");
        practiceActivityNameEditTextField.setText("");
        practiceActivityTypeComboBox.setValue("请选择活动类型");
        practiceFromDatePicker.setValue(LocalDate.now());
        practiceToDatePicker.setValue(LocalDate.now());
        practiceScoreEditTextField.setText("");

        Result result = HttpRequestUtils.request("/activity/getActivityList", new DataRequest());
        setPracticeTableViewData(result);
    }

    @FXML
    void onPracticeUpdateButtonClick() {
        Map map = practiceTableView.getSelectionModel().getSelectedItem();
        if (AppStore.getUser().getUser_type_id() == 3) {
            practiceActivityTypeComboBox.setDisable(true);
            practiceActivityNameEditTextField.setDisable(true);
            practiceFromDatePicker.setDisable(true);
            practiceToDatePicker.setDisable(true);
            practiceScoreEditTextField.setDisable(true);
        }
        if (map != null) {
            practiceStudentNumEditTextField.setDisable(true);
            practiceStudentNameEditTextField.setDisable(true);
            practiceStudentNumEditTextField.setText(map.get("student_num").toString());
            practiceStudentNameEditTextField.setText(map.get("student_name").toString());
            practiceActivityNameEditTextField.setText(map.get("activity_name").toString());
            practiceActivityTypeComboBox.setValue(map.get("activity_type").toString());
            practiceFromDatePicker.setValue(LocalDate.parse(map.get("date").toString().split("~")[0]));
            practiceToDatePicker.setValue(LocalDate.parse(map.get("date").toString().split("~")[1]));
            practiceScoreEditTextField.setText(map.get("score").toString());
            practiceStudentNumEditTextField.setDisable(true);
            practiceStudentNameEditTextField.setDisable(true);
            practiceConfirmButton.setText("修改");
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择一条记录");
            alert.showAndWait();
        }
    }

    @FXML
    void onPracticeResetEditButtonClick() {
        practiceStudentNumEditTextField.setDisable(false);
        practiceStudentNameEditTextField.setDisable(false);
        if (AppStore.getUser().getUser_type_id() == 3) {
            practiceStudentNumEditTextField.setDisable(true);
            practiceStudentNameEditTextField.setDisable(true);
            practiceActivityTypeComboBox.setDisable(true);
            practiceActivityNameEditTextField.setDisable(true);
            practiceFromDatePicker.setDisable(true);
            practiceToDatePicker.setDisable(true);
            practiceScoreEditTextField.setDisable(true);
        }
        practiceStudentNumEditTextField.setText("");
        practiceStudentNameEditTextField.setText("");
        practiceActivityNameEditTextField.setText("");
        practiceActivityTypeComboBox.setValue("请选择活动类型");
        practiceFromDatePicker.setValue(LocalDate.now());
        practiceToDatePicker.setValue(LocalDate.now());
        practiceScoreEditTextField.setText("");
        practiceConfirmButton.setText("添加");
    }

    public void setPracticeTableViewData(Result result) {
        practiceObservableList.clear();
        practiceList = (List<Map>) result.getData();
        if (AppStore.getUser().getUser_type_id() == 3) {
            practiceList = CommonMethod.filter(practiceList, "student_num", AppStore.getUser().getPerson_num());
        }
        for (Map map : practiceList) {
            for (String s : practiceType) {
                if (map.get("activity_type").equals(s)) {
                    practiceObservableList.add(map);
                    break;
                }
            }
        }
        practiceTableView.setItems(practiceObservableList);
    }


    @FXML
    public void initialize() {

        User user = AppStore.getUser();
        if (user.getUser_type_id() == 3) {
            one.setVisible(false);
            dailyAddButton.setVisible(false);
            dailyDeleteButton.setVisible(false);
            dailyUpdateButton.setVisible(false);
            dailyConfirmButton.setVisible(false);
            dailyQueryButton.setVisible(false);
            dailyResetButton.setVisible(false);
            dailyResetEditButton.setVisible(false);
            dailyStudentInfoTextField.setVisible(false);
            dailyStudentNumEditTextField.setDisable(true);
            dailyStudentNameEditTextField.setDisable(true);
            dailyActivityNameEditTextField.setDisable(true);
            dailyActivityTypeComboBox.setDisable(true);
            dailyFromDatePicker.setDisable(true);
            Text text = new Text("( 提醒：若要对日常活动进行增加/修改/删除操作，请联系您的老师或管理员 )");
            text.setFont(javafx.scene.text.Font.font(14));
            text.setFill(Color.RED);
            text.setLayoutX(100);
            text.setLayoutY(24);
            dailyAnchor.getChildren().add(text);


            two.setVisible(false);
            practiceAddButton.setVisible(false);
            practiceDeleteButton.setVisible(false);
            practiceUpdateButton.setVisible(false);
            practiceConfirmButton.setVisible(false);
            practiceQueryButton.setVisible(false);
            practiceResetButton.setVisible(false);
            practiceResetEditButton.setVisible(false);
            practiceStudentInfoTextField.setVisible(false);
            practiceStudentNumEditTextField.setDisable(true);
            practiceStudentNameEditTextField.setDisable(true);
            practiceActivityNameEditTextField.setDisable(true);
            practiceActivityTypeComboBox.setDisable(true);
            practiceFromDatePicker.setDisable(true);

            Text text1 = new Text("( 提醒：若要对创新实践进行增加/修改/删除操作，请联系您的老师或管理员 )");
            text1.setFont(javafx.scene.text.Font.font(14));
            text1.setFill(Color.RED);
            text1.setLayoutX(100);
            text1.setLayoutY(24);
            practiceAnchor.getChildren().add(text1);
        }


        dailyIdColumn.setVisible(false);
        practiceIdColumn.setVisible(false);
        practiceConfirmButton.setText("添加");
        dailyConfirmButton.setText("添加");
        //创新实践
        practiceIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        practiceStudentNumColumn.setCellValueFactory(new MapValueFactory<>("student_num"));
        practiceStudentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        practiceActivityNameColumn.setCellValueFactory(new MapValueFactory<>("activity_name"));
        practiceActivityTypeColumn.setCellValueFactory(new MapValueFactory<>("activity_type"));
        practiceDateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        practiceScoreColumn.setCellValueFactory(new MapValueFactory<>("score"));
        practiceActivityTypeComboBox.getItems().addAll(practiceType);


        //日常活动
        dailyIdColumn.setCellValueFactory(new MapValueFactory<>("id"));
        dailyStudentNumColumn.setCellValueFactory(new MapValueFactory<>("student_num"));
        dailyStudentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        dailyActivityNameColumn.setCellValueFactory(new MapValueFactory<>("activity_name"));
        dailyActivityTypeColumn.setCellValueFactory(new MapValueFactory<>("activity_type"));
        dailyDateColumn.setCellValueFactory(new MapValueFactory<>("date"));
        dailyScoreColumn.setCellValueFactory(new MapValueFactory<>("score"));
        dailyActivityTypeComboBox.getItems().addAll(dailyType);


        dailyTableView.setOnMouseClicked(e -> {
            Map map = dailyTableView.getSelectionModel().getSelectedItem();
            if (map != null) {
                onDailyUpdateButtonClick();
                if (user.getUser_type_id() == 3) {
                    dailyActivityTypeComboBox.setDisable(true);
                    dailyActivityNameEditTextField.setDisable(true);
                    dailyFromDatePicker.setDisable(true);
                    dailyToDatePicker.setDisable(true);
                    dailyScoreEditTextField.setDisable(true);
                }
            }
        });
        practiceTableView.setOnMouseClicked(e -> {
            Map map = practiceTableView.getSelectionModel().getSelectedItem();
            if (map != null) {
                onPracticeUpdateButtonClick();
                if (user.getUser_type_id() == 3) {
                    practiceActivityTypeComboBox.setDisable(true);
                    practiceActivityNameEditTextField.setDisable(true);
                    practiceFromDatePicker.setDisable(true);
                    practiceToDatePicker.setDisable(true);
                    practiceScoreEditTextField.setDisable(true);
                }
            }
        });

        dailyTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        practiceTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onDailyResetButtonClick();
        onPracticeResetButtonClick();
    }

}
