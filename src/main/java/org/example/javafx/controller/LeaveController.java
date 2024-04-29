package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Data;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

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
    private Tab viewTab;

    @FXML
    private Tab applyTab;

    @FXML
    private DatePicker comeBackDatePicker;

    @FXML
    private TableColumn<Map, String> destinationColumn;

    @FXML
    private TextField destinationTextfield;

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
    private Button saveButton;

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

    //---------------------------------------------------------
    private List<Map> leaveList=new ArrayList<>();

    private ObservableList<Map> observableList= FXCollections.observableArrayList();


    //---------------------------------------------------------
    @FXML
    private void onQueryButtonClick() {
        if(studentInfoTextField.getText()=="请输入学号或姓名"||studentInfoTextField.getText()==null){
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入学生信息");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
            return;
        }else {
            DataRequest dataRequest=new DataRequest();
            Result result=new Result();
            dataRequest.add("student_num",studentInfoTextField.getText());
            result=HttpRequestUtils.request("/leave/selectByStudentNum",dataRequest);
            if(result==null){
                dataRequest.add("student_name",studentInfoTextField.getText());
                result=HttpRequestUtils.request("/leave/selectByStudentName",dataRequest);
                if(result==null){
                    Stage confirmStage=new Stage();
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
            }else{
                setTableViewData(result);
            }
        }
    }

    @FXML
    private void onResetViewButtonClick(){
        studentInfoTextField.setText("请输入学号或姓名");
        Result result=new Result();
        result=HttpRequestUtils.request("/leave/getLeaveList",new DataRequest());
        setTableViewData(result);
    }

    public void setTableViewData(Result result) {
        observableList.clear();
        if(result.getData() instanceof Map){
            Map leaveMap=(Map) result.getData();
            observableList.add(leaveMap);
        }
        else if(result.getData() instanceof ArrayList){
            leaveList=(ArrayList) result.getData();
            for(Map leaveMap: leaveList){
                observableList.add(leaveMap);
            }
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    private void onApplyButtonClick(ActionEvent event) {
        System.out.println("apply");
        Integer cnt=0;
        if(studentIdTextField.getText()==""){
            cnt++;
        }
        if(studentNameTextField.getText()==""){
            cnt++;
        }
        if(studentTeleTextField.getText()==""){
            cnt++;
        }
        if(instituteTextField.getText()==""){
            cnt++;
        }
        if(instructorNameTextfield.getText()==""){
            cnt++;
        }
        if(instructorTeleTextField.getText()==""){
            cnt++;
        }
        if(majorTextField.getText()==""){
            cnt++;
        }
        if(reasonTextField.getText()==""){
            cnt++;
        }
        if(destinationTextfield.getText()==""){
            cnt++;
        }
        if(finalCheckBox.isSelected()){
            cnt++;
        }
        if(goOutTypeComboBox.getSelectionModel().getSelectedItem()==null||goOutTypeComboBox.getSelectionModel().getSelectedItem().toString()=="请选择外出类型"){
            cnt++;
        }
        if(reasonComboBox.getSelectionModel().getSelectedItem()==null||goOutTypeComboBox.getSelectionModel().getSelectedItem().toString()=="请选择外出事由"){
            cnt++;
        }
        if(cnt==0){
            LocalDate goOutDate=goOutDatePicker.getValue();
            LocalDate comeBackDate=comeBackDatePicker.getValue();
            if(goOutDate.isAfter(comeBackDate)){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请选择正确的时间");
            }
            String msg=CommonMethod.alertButton("提交");
            if(msg=="确认"){
                DataRequest dataRequest=new DataRequest();
                dataRequest.add("student_num",studentIdTextField.getText());
                dataRequest.add("student_name",studentNameTextField.getText());
                dataRequest.add("leave_type",goOutTypeComboBox.getSelectionModel().getSelectedItem().toString());
                dataRequest.add("leave_reason",reasonComboBox.getSelectionModel().getSelectedItem().toString());
                dataRequest.add("destination",destinationTextfield.getText());
                dataRequest.add("time",goOutDatePicker.getValue().toString()+"~"+comeBackDatePicker.getValue().toString());
                dataRequest.add("status","未处理");
                System.out.println(dataRequest.getData());
                HttpRequestUtils.request("/leave/insertLeave",dataRequest);
            }
        }else{
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("还有"+cnt+"个必填项未填，请补充完整信息后提交");
        }
    }

    @FXML
    private void onResetButtonClick(ActionEvent event) {
        goOutTypeComboBox.setValue("请选择请假类型");
        reasonComboBox.setValue("请选择请假事由");
        goOutDatePicker.setValue(LocalDate.now());
        comeBackDatePicker.setValue(LocalDate.now());
        studentIdTextField.setText("");
        studentNameTextField.setText("");
        studentTeleTextField.setText("");
        instituteTextField.setText("");
        instructorNameTextfield.setText("");
        instructorTeleTextField.setText("");
        majorTextField.setText("");
        reasonTextField.setText("");
        destinationTextfield.setText("");
        finalCheckBox.setSelected(false);
    }

    @FXML
    private void onSaveButtonClick(ActionEvent event) {

    }

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_num"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        leaveTypeColumn.setCellValueFactory(new MapValueFactory<>("leave_type"));
        leaveReasonColumn.setCellValueFactory(new MapValueFactory<>("leave_reason"));
        destinationColumn.setCellValueFactory(new MapValueFactory<>("destination"));
        timeColumn.setCellValueFactory(new MapValueFactory<>("time"));
        statusColumn.setCellValueFactory(new MapValueFactory<>("status"));






        //---------------------------------
        //以下是申请界面的;
        goOutTypeComboBox.setValue("请选择请假类型");
        reasonComboBox.setValue("请选择请假事由");
        String[] goOutType={"请选择请假类型","临时外出","固定外出","毕业离校","生病请假"};
        String[] reason={"请选择请假事由","跨校区学习,实验等","外出实习等常规业务","外出办事","假期离校","外出就诊","毕业离校"};
        List goOutTypeList=new ArrayList<>();
        List reasonList=new ArrayList();
        for(int i=0;i<5;i++){
            Map map=new HashMap();
            map.put(i+"",goOutType[i]);
            goOutTypeList.add(map.get(i+""));
        }
        for(int i=0;i<7;i++){
            Map map=new HashMap();
            map.put(i+"",reason[i]);
            reasonList.add(map.get(i+""));
        }
        System.out.println(goOutTypeList);
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
