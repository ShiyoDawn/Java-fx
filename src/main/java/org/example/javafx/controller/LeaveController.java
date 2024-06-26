package org.example.javafx.controller;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import lombok.Data;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

public class LeaveController {

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private TextField ageTextField;

    @FXML
    private Button applyButton;

    @FXML
    private Button checkButton;

    @FXML
    private Tab viewTab;

    @FXML
    private Tab applyTab;

    @FXML
    private DatePicker comeBackDatePicker;

    @FXML
    private TableColumn<Map, String> destinationColumn;

    @FXML
    private TextField destinationTextField;

    @FXML
    private CheckBox finalCheckBox;

    @FXML
    private DatePicker goOutDatePicker;

    @FXML
    private ComboBox goOutTypeComboBox;

    @FXML
    private TableColumn<Map, String> idColumn;

    @FXML
    private TextField instituteTextField;

    @FXML
    private TextField instructorNameTextfield;

    @FXML
    private TextField instructorTeleTextField;

    @FXML
    private TableColumn<Map, String> leaveReasonColumn;

    @FXML
    private TableColumn<Map, String> leaveTypeColumn;

    @FXML
    private TextField majorTextField;

    @FXML
    private ComboBox reasonComboBox;

    @FXML
    private TextField reasonTextField;

    @FXML
    private Button resetButton;


    @FXML
    private TableColumn<Map, String> statusColumn;

    @FXML
    private TextField studentIdTextField;

    @FXML
    private TableColumn<Map, String> studentNameColumn;

    @FXML
    private TextField studentNameTextField;

    @FXML
    private TableColumn<Map, String> studentNumColumn;

    @FXML
    private TextField studentTeleTextField;

    @FXML
    private TableColumn<Map, String> timeColumn;

    @FXML
    private Button resetViewButton;

    @FXML
    private Button queryButton;

    @FXML
    private TextField studentInfoTextField;

    @FXML
    private Text one;

    @FXML
    private Label two;

    @FXML
    private Text three;

    @FXML
    private Text four;

    //---------------------------------------------------------
    private List<Map> leaveList = new ArrayList<>();

    private ObservableList<Map> observableList = FXCollections.observableArrayList();


    //---------------------------------------------------------
    @FXML
    private void onQueryButtonClick() {
        if (studentInfoTextField.getText() == "请输入学号或姓名" || studentInfoTextField.getText() == "") {
            onResetViewButtonClick();
        } else {
            DataRequest dataRequest = new DataRequest();
            //Result result = new Result();
            //dataRequest.add("student_num", studentInfoTextField.getText());
            List<Map> result = CommonMethod.filter(leaveList, "student_num", studentInfoTextField.getText());
            if (result == null || result.size() == 0) {
                result = CommonMethod.filter(leaveList, "student_name", studentInfoTextField.getText());
                if (result == null || result.size() == 0) {
                    Stage confirmStage = new Stage();
                    confirmStage.setWidth(250);
                    confirmStage.setHeight(150);
                    //取消放大（全屏）按钮
                    confirmStage.setResizable(false);
                    confirmStage.setResizable(false);
                    Text text = new Text("无该同学请假信息，请重新输入");
                    HBox hBox = new HBox(text);
                    hBox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(hBox);
                    confirmStage.setScene(scene);
                    confirmStage.show();
                    return;
                } else {
                    observableList.clear();
                    for (Map map : result) {
                        observableList.add(map);
                    }
                    dataTableView.setItems(observableList);
                }
            } else {
                observableList.clear();
                for (Map map : result) {
                    observableList.add(map);
                }
                dataTableView.setItems(observableList);
            }

            /*result = HttpRequestUtils.request("/leave/selectByStudentNum", dataRequest);
            if (result == null) {
                dataRequest.add("student_name", studentInfoTextField.getText());
                result = HttpRequestUtils.request("/leave/selectByStudentName", dataRequest);
                if (result == null) {
                    Stage confirmStage = new Stage();
                    confirmStage.setWidth(250);
                    confirmStage.setHeight(150);
                    //取消放大（全屏）按钮
                    confirmStage.setResizable(false);
                    confirmStage.setResizable(false);
                    Text text = new Text("请输入正确的学生信息");
                    HBox hBox = new HBox(text);
                    hBox.setAlignment(Pos.CENTER);
                    Scene scene = new Scene(hBox);
                    confirmStage.setScene(scene);
                    confirmStage.show();
                    return;
                }
                setTableViewData(result);
            } else {
                System.out.println(result.getData());
                setTableViewData(result);
            }*/
        }
    }

    @FXML
    private void onResetViewButtonClick() {
        studentInfoTextField.setText("");
        studentInfoTextField.setPromptText("请输入学号或姓名");
        Result result = new Result();
        result = HttpRequestUtils.request("/leave/getLeaveList", new DataRequest());
        if (AppStore.getUser().getUser_type_id() == 3) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_num", AppStore.getUser().getPerson_num());
            result = HttpRequestUtils.request("/leave/selectByStudentNum", dataRequest);
        } else {
            result = HttpRequestUtils.request("/leave/getLeaveList", new DataRequest());
        }
        leaveList = (List<Map>) result.getData();
        setTableViewData(result);
    }

    public void setTableViewData(Result result) {
        observableList.clear();
        if (result.getData() instanceof Map) {
            Map leaveMap = (Map) result.getData();
            observableList.add(leaveMap);
        } else if (result.getData() instanceof ArrayList) {
            leaveList = (ArrayList) result.getData();
            for (Map leaveMap : leaveList) {
                observableList.add(leaveMap);
            }
        }
        //System.out.println(observableList);
        dataTableView.setItems(observableList);
    }

    @FXML
    private void onApplyButtonClick() {
        Integer cnt = 0;
        if (studentIdTextField.getText() == "") {
            cnt++;
        }
        if (studentNameTextField.getText() == "") {
            cnt++;
        }
        if (studentTeleTextField.getText() == "") {
            cnt++;
        }
        if (instituteTextField.getText() == "") {
            cnt++;
        }
        if (instructorNameTextfield.getText() == "") {
            cnt++;
        }
        if (instructorTeleTextField.getText() == "") {
            cnt++;
        }
        if (majorTextField.getText() == "") {
            cnt++;
        }
        if (reasonTextField.getText() == "") {
            cnt++;
        }
        if (destinationTextField.getText() == "") {
            cnt++;
        }
        if (!finalCheckBox.isSelected()) {
            cnt++;
        }
        if (goOutTypeComboBox.getSelectionModel().getSelectedItem() == null || goOutTypeComboBox.getSelectionModel().getSelectedItem().toString() == "请选择请假类型") {
            cnt++;
        }
        if (reasonComboBox.getSelectionModel().getSelectedItem() == null || reasonComboBox.getSelectionModel().getSelectedItem().toString() == "请选择请假事由") {
            cnt++;
        }
        if (cnt == 0) {
            LocalDate goOutDate = goOutDatePicker.getValue();
            LocalDate comeBackDate = comeBackDatePicker.getValue();
            if (goOutDate.isAfter(comeBackDate)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请选择正确的时间");
                alert.showAndWait();
                return;
            }
            String msg = CommonMethod.alertButton("提交");
            if (msg == "确认") {
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", studentIdTextField.getText());
                dataRequest.add("student_name", studentNameTextField.getText());
                dataRequest.add("leave_type", goOutTypeComboBox.getSelectionModel().getSelectedItem().toString());
                dataRequest.add("leave_reason", reasonComboBox.getSelectionModel().getSelectedItem().toString());
                dataRequest.add("destination", destinationTextField.getText());
                dataRequest.add("time", goOutDatePicker.getValue().toString() + "~" + comeBackDatePicker.getValue().toString());
                dataRequest.add("status", "[未处理]*");
                dataRequest.add("age", ageTextField.getText());
                dataRequest.add("institute", instituteTextField.getText());
                dataRequest.add("major", majorTextField.getText());
                dataRequest.add("instructor_name", instructorNameTextfield.getText());
                dataRequest.add("instructor_tele", instructorTeleTextField.getText());
                dataRequest.add("leave_detailed_reason", reasonTextField.getText());
                dataRequest.add("start_time", goOutDatePicker.getValue().toString());
                dataRequest.add("end_time", comeBackDatePicker.getValue().toString());
                dataRequest.add("student_tele", studentTeleTextField.getText());
                HttpRequestUtils.request("/leave/insertLeave", dataRequest);
                onResetButtonClick();
                onResetViewButtonClick();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("还有" + cnt + "个必填项未填，请补充完整信息后提交");
            alert.showAndWait();
        }
    }

    @FXML
    private void onResetButtonClick() {
        goOutTypeComboBox.setValue("请选择请假类型");
        reasonComboBox.setValue("请选择请假事由");
        goOutDatePicker.setValue(LocalDate.now());
        comeBackDatePicker.setValue(LocalDate.now());
        if (AppStore.getUser().getUser_type_id() != 3) {
            studentIdTextField.setText("");
            studentNameTextField.setText("");
            majorTextField.setText("");
        }
        studentTeleTextField.setText("");
        instituteTextField.setText("");
        instructorNameTextfield.setText("");
        instructorTeleTextField.setText("");
        reasonTextField.setText("");
        destinationTextField.setText("");
        finalCheckBox.setSelected(false);
    }


    @FXML
    private void onCheckButtonClick() {
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选中某行");
            alert.showAndWait();
            return;
        } else if (selected.size() == 1) {
            final String[] msg = new String[1];
            Stage primaryStage = new Stage();
            primaryStage.setResizable(false);
            Button passButton = new Button("通过");
            Button failButton = new Button("不通过");
            Button cancelButton = new Button("取消");
            Text questionText = new Text("请问您是否要通过该学生的请假要求？");
            Text reminderText = new Text("（提醒：后续仍可进行修改）");

            // 设置组件的样式和布局
            passButton.setLayoutX(46);
            passButton.setLayoutY(136);
            failButton.setLayoutX(130);
            failButton.setLayoutY(136);
            cancelButton.setLayoutX(222);
            cancelButton.setLayoutY(136);
            questionText.setLayoutX(20);
            questionText.setLayoutY(67);
            questionText.setFill(javafx.scene.paint.Color.valueOf("#981a1a"));
            reminderText.setLayoutX(72);
            reminderText.setLayoutY(105);

            passButton.setOnAction(p -> {
                msg[0] = "已通过";
                primaryStage.close();
            });

            cancelButton.setOnAction(c -> {
                msg[0] = CommonMethod.getString(selected.get(0), "status");
                primaryStage.close();
            });

            primaryStage.setOnCloseRequest(e -> {
                msg[0] = CommonMethod.getString(selected.get(0), "status");
            });

            failButton.setOnAction(f -> {
                msg[0] = "不批准";
                primaryStage.close();
            });

            // 设置组件的字体
            Font font = new Font(13);
            passButton.setFont(font);
            failButton.setFont(font);
            cancelButton.setFont(font);
            questionText.setFont(new Font(15));
            reminderText.setFont(new Font(14));

            // 创建AnchorPane，并将组件添加到AnchorPane中
            AnchorPane anchorPane = new AnchorPane();
            anchorPane.getChildren().addAll(passButton, failButton, cancelButton, questionText, reminderText);

            // 创建Scene，并将AnchorPane设置为Scene的根节点
            Scene scene = new Scene(anchorPane, 324, 216);

            // 设置Stage的属性
            primaryStage.setTitle("审核界面");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.showAndWait();
            DataRequest dataRequest = new DataRequest();
            String student_num = CommonMethod.getString(selected.get(0), "student_num");
            String student_name = CommonMethod.getString(selected.get(0), "student_name");
            String leave_type = CommonMethod.getString(selected.get(0), "leave_type");
            String leave_reason = CommonMethod.getString(selected.get(0), "leave_reason");
            String destination = CommonMethod.getString(selected.get(0), "destination");
            String time = CommonMethod.getString(selected.get(0), "time");
            String start_time = CommonMethod.getString(selected.get(0), "start_time");
            String end_time = CommonMethod.getString(selected.get(0), "end_time");
            String institute = CommonMethod.getString(selected.get(0), "institute");
            String major = CommonMethod.getString(selected.get(0), "major");
            String instructor_name = CommonMethod.getString(selected.get(0), "instructor_name");
            String instructor_tele = CommonMethod.getString(selected.get(0), "instructor_tele");
            String leave_detailed_reason = CommonMethod.getString(selected.get(0), "leave_detailed_reason");
            String student_tele = CommonMethod.getString(selected.get(0), "student_tele");

            dataRequest.add("student_num", student_num);
            dataRequest.add("student_name", student_name);
            dataRequest.add("leave_type", leave_type);
            dataRequest.add("leave_reason", leave_reason);
            dataRequest.add("destination", destination);
            dataRequest.add("time", time);
            dataRequest.add("start_time", start_time);
            dataRequest.add("end_time", end_time);
            dataRequest.add("institute", institute);
            dataRequest.add("major", major);
            dataRequest.add("instructor_name", instructor_name);
            dataRequest.add("instructor_tele", instructor_tele);
            dataRequest.add("leave_detailed_reason", leave_detailed_reason);
            dataRequest.add("student_tele", student_tele);
            dataRequest.add("status", msg[0]);
            HttpRequestUtils.request("/leave/updateStatus", dataRequest);
            onQueryButtonClick();
        } else if (selected.size() > 1) {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请不要选中多行进行审核");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }
    }

    @FXML
    private void initialize() {
        User user = AppStore.getUser();
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_num"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        leaveTypeColumn.setCellValueFactory(new MapValueFactory<>("leave_type"));
        leaveReasonColumn.setCellValueFactory(new MapValueFactory<>("leave_reason"));
        destinationColumn.setCellValueFactory(new MapValueFactory<>("destination"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));

        //---------------------------------
        dataTableView.setOnMouseClicked(e -> {
            if (e.getButton() == MouseButton.PRIMARY && e.getClickCount() == 2 && dataTableView.getSelectionModel().getSelectedItem() != null) {
                Stage stage = new Stage();
                stage.setResizable(false);
                FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("leave-detail.fxml"));
                try {
                    Parent parent = fxmlLoader.load();
                    LeaveDetailController leaveDetailController = fxmlLoader.getController();
                    leaveDetailController.initialize(dataTableView.getSelectionModel().getSelectedItem());
                    stage.setScene(new Scene(parent));
                    stage.setTitle("请假具体消息");
                    stage.showAndWait();
                    onQueryButtonClick();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        studentInfoTextField.setPromptText("请输入学号或姓名");

        //以下是学生信息相关;
        if (user.getUser_type_id() == 3) {

            viewTab.setText("我的请假");
            applyTab.setText("请假申请");
            idColumn.setVisible(false);
            String person_num = user.getPerson_num();
            //以下是根据学生信息来填入请假界面信息的;
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", person_num);
            Result result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
            Map map = (Map) result.getData();
            instituteTextField.setText(map.get("department").toString());
            studentIdTextField.setText(map.get("person_num").toString());
            studentTeleTextField.setText(map.get("phone_number").toString());
            dataRequest.add("person_id", Integer.parseInt(map.get("id").toString().split("\\.")[0]));
            result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
            map = (Map) result.getData();
            studentNameTextField.setText(map.get("student_name").toString());
            majorTextField.setText(map.get("major").toString());

            studentIdTextField.setDisable(true);
            studentNameTextField.setDisable(true);
            studentInfoTextField.setDisable(true);
            majorTextField.setDisable(true);
            instituteTextField.setDisable(true);
            studentTeleTextField.setDisable(true);

            idColumn.setVisible(false);

            checkButton.setVisible(false);
            queryButton.setVisible(false);
            resetViewButton.setVisible(false);
            studentInfoTextField.setVisible(false);
            one.setVisible(false);
            two.setVisible(false);
            //three.setVisible(false);
            four.setVisible(false);

            /*checkButton.setDisable(true);
            queryButton.setDisable(true);
            resetViewButton.setDisable(true);
            one.setDisable(true);
            two.setDisable(true);
            three.setDisable(true);
            four.setDisable(true);*/

            dataTableView.setPrefHeight(616);
            dataTableView.setLayoutY(32);

            three.setText("( 提醒：左键双击可进入详情界面 )");
            three.setFont(javafx.scene.text.Font.font("System", 14.0));
            three.setLayoutX(41);
            three.setLayoutY(24);
            three.setStrokeType(StrokeType.OUTSIDE);
            three.setStrokeWidth(0.0);
            three.setWrappingWidth(230.0);

        } else {
            applyTab.setText("仅学生需填写");
            applyTab.setDisable(true);
        }
        //以下是申请界面的;
        goOutTypeComboBox.setValue("请选择请假类型");
        reasonComboBox.setValue("请选择请假事由");
        String[] goOutType = {"请选择请假类型", "临时外出", "固定外出", "毕业离校", "生病请假"};
        String[] reason = {"请选择请假事由", "跨校区学习,实验等", "外出实习等常规业务", "外出办事", "假期离校", "外出就诊", "毕业离校"};
        List goOutTypeList = new ArrayList<>();
        List reasonList = new ArrayList();
        for (int i = 0; i < 5; i++) {
            Map map = new HashMap();
            map.put(i + "", goOutType[i]);
            goOutTypeList.add(map.get(i + ""));
        }
        for (int i = 0; i < 7; i++) {
            Map map = new HashMap();
            map.put(i + "", reason[i]);
            reasonList.add(map.get(i + ""));
        }
        goOutTypeComboBox.getItems().addAll(goOutTypeList);
        reasonComboBox.getItems().addAll(reasonList);
        goOutDatePicker.setValue(LocalDate.now());
        comeBackDatePicker.setValue(LocalDate.now());
        goOutDatePicker.setEditable(false);
        comeBackDatePicker.setEditable(false);

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onResetViewButtonClick();

    }
}
