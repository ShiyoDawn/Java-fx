package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import static jdk.internal.org.jline.utils.Colors.s;

public class StudentGloryController {

    @FXML
    private Button addButton;

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private Button deleteButton;

    @FXML
    private AnchorPane editAnchorPane;

    @FXML
    private Button editButton;

    @FXML
    private Button editCancelButton;

    @FXML
    private Button editConfirmButton;

    @FXML
    private TabPane editTabPane;


    @FXML
    private AnchorPane gloryAnchorPane;

    @FXML
    private BorderPane gloryBorderPane;


    @FXML
    private TableColumn<Map, String> gloryNameColumn;

    @FXML
    private TableColumn<Map, String> gloryTypeColumn;

    @FXML
    private TableColumn<Map, String> gloryLevelColumn;

    @FXML
    private ComboBox gloryTypeEditComboBox;

    @FXML
    private TextField gloryUpdateTextField;

    @FXML
    private TableColumn<Map, String> id;

    @FXML
    private Button queryButton;

    @FXML
    private Button resetButton;

    @FXML
    private ComboBox studentComboBox;

    @FXML
    private ComboBox studentEditComboBox;

    @FXML
    private TableColumn<Map, String> studentNameColumn;

    @FXML
    private TableColumn<Map, String> studentNumColumn;

    @FXML
    private TextField gloryLevelUpdateTextField;

    @FXML
    private Label one;

    @FXML
    private TabPane tabpane;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Tab viewTab;

    //-------------------------------------------------

    private List<Map> gloryList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    //-------------------------------------------------
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        onCancelClick();
        editTabPane.setVisible(true);
        editConfirmButton.setText("增加荣誉");
        onQueryButtonClick();
        if(!id.isVisible()&&(studentComboBox.getValue()==null||studentComboBox.getValue().toString().equals("请选择学生"))){
            id.setVisible(true);
        }
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        onCancelClick();
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择要删除的荣誉！");
            alert.showAndWait();
            return;
        }
        String msg = CommonMethod.alertButton("删除");
        if (msg == "确认") {
            for (Map gloryMap : selected) {
                String student_name = CommonMethod.getString(gloryMap, "student_name");
                String glory_name = CommonMethod.getString(gloryMap, "glory_name");
                String glory_type = CommonMethod.getString(gloryMap, "glory_type");
                String glory_level = CommonMethod.getString(gloryMap, "glory_level");
                String student_num = CommonMethod.getString(gloryMap, "student_num");
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_name", student_name);
                dataRequest.add("glory_name", glory_name);
                dataRequest.add("glory_type", glory_type);
                dataRequest.add("glory_level", glory_level);
                dataRequest.add("student_num", student_num);
                HttpRequestUtils.request("/glory/deleteGlory", dataRequest);
            }
        }
        onQueryButtonClick();
        if(!id.isVisible()&&(studentComboBox.getValue()==null||studentComboBox.getValue().toString().equals("请选择学生"))){
            id.setVisible(true);
        }
    }

    Result tmpResult;

    @FXML
    private void onEditButtonClick() {
        onCancelClick();
        Map selected = dataTableView.getSelectionModel().getSelectedItem();
        if(selected==null){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择要修改的荣誉！");
            alert.showAndWait();
            return;
        }
        editConfirmButton.setText("修改荣誉");
        editTabPane.setVisible(true);
        if (selected != null) {
            DataRequest dataRequest = new DataRequest();
            String student_num = CommonMethod.getString(selected, "student_num");
            String glory_name = CommonMethod.getString(selected, "glory_name");
            studentEditComboBox.setValue(student_num + "-" + CommonMethod.getString(selected, "student_name"));
            studentEditComboBox.setEditable(false);
            studentEditComboBox.setDisable(true);
            gloryUpdateTextField.setText(glory_name);
            gloryLevelUpdateTextField.setText(CommonMethod.getString(selected, "glory_level"));
            gloryTypeEditComboBox.setValue(CommonMethod.getString(selected, "glory_type"));
        }

    }

    @FXML
    private void onQueryButtonClick() {
        id.setVisible(false);
        String student_name = null;
        Object student = studentComboBox.getSelectionModel().getSelectedItem();
        Result result = null;
        if (student != null) {
            student_name = student.toString();
        }
        if (student_name == "请选择学生") {
            studentComboBox.setValue("请选择学生");
            student_name = null;
        }
        if (student_name != null) {
            //DataRequest dataRequest = new DataRequest();
            //dataRequest.add("student_name", student_name);
            //result = HttpRequestUtils.request("/glory/selectByStudentName", dataRequest);
            List<Map> ans = CommonMethod.filter(gloryList, "student_num", student_name.split("-")[0]);
            if (ans == null || ans.size() == 0) {
                observableList.clear();
                return;
            } else {
                observableList.clear();
                for (Map map : ans) {
                    observableList.add(map);
                }
                dataTableView.setItems(observableList);
                return;
            }
        } else if (student_name == null) {
            result = HttpRequestUtils.request("/glory/getGloryList", new DataRequest());
        }
        if (result == null) {
            return;
        }
        setTableViewData(result);
    }

    @FXML
    private void onResetButtonClick() {
        Result result = null;
        id.setVisible(true);
        studentComboBox.setValue(null);
        studentComboBox.setPromptText("请选择学生");
        User user = AppStore.getUser();
        if (user.getUser_type_id() == 3) {
            DataRequest dataRequest = new DataRequest();
            String person_num = user.getPerson_num();
            dataRequest.add("person_num", person_num);
            result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
            Map map = (Map) result.getData();
            dataRequest.add("person_id", Integer.parseInt(map.get("id").toString().split("\\.")[0]));
            result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
            map = (Map) result.getData();
            String student_name = map.get("student_name").toString();
            dataRequest.add("student_num", person_num);
            result = HttpRequestUtils.request("/glory/selectByStudentNum", dataRequest);
        } else {
            result = HttpRequestUtils.request("/glory/getGloryList", new DataRequest());
        }
        onCancelClick();
        setTableViewData(result);
    }

    private void setTableViewData(Result result) {
        observableList.clear();
        if (result.getData() instanceof Map) {
            Map gloryMap = (Map) result.getData();
            observableList.add(gloryMap);
        } else if (result.getData() instanceof ArrayList) {
            gloryList = (ArrayList<Map>) result.getData();
            for (Map gloryMap : (ArrayList<Map>) gloryList) {
                observableList.add(gloryMap);
            }
        }
        dataTableView.setItems(observableList);
    }

    @FXML
    private void onCancelClick() {
        studentEditComboBox.setValue("请选择学生");
        gloryTypeEditComboBox.setValue("请选择荣誉类型");
        gloryUpdateTextField.setText("");
        gloryLevelUpdateTextField.setText("");
        editConfirmButton.setText("增加荣誉");
        studentEditComboBox.setDisable(false);
    }

    @FXML
    private void onConfirmClick(ActionEvent event) {
        DataRequest dataRequest = new DataRequest();
        String student_name = null;
        String glory_type = null;
        Object student = studentEditComboBox.getSelectionModel().getSelectedItem();
        Object glory = gloryTypeEditComboBox.getSelectionModel().getSelectedItem();
        Result result = null;
        if (student != null) {
            student_name = student.toString();
        }
        if (glory != null) {
            glory_type = glory.toString();
        }
        if (student_name == "请选择学生") {
            studentEditComboBox.setValue("请选择学生");
            student_name = null;
        }
        if (glory_type == "请选择荣誉类型") {
            gloryTypeEditComboBox.setValue("请选择荣誉类型");
            glory_type = null;
        }
        if (gloryUpdateTextField.getText() == "") {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入荣誉名称");
            alert.showAndWait();
            return;
        }
        if (gloryLevelUpdateTextField.getText() == "") {
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入荣誉等级");
            alert.showAndWait();
            return;
        }
        if (student_name != null && glory_type != null) {
            Map map=dataTableView.getSelectionModel().getSelectedItem();
            DataRequest dataRequest1=new DataRequest();
            if(map!=null){
                dataRequest1.add("student_name",CommonMethod.getString(map,"student_name"));
                dataRequest1.add("student_num",CommonMethod.getString(map,"student_num"));
            }else{
                dataRequest1.add("student_name",student_name.split("-")[1]);
                dataRequest1.add("student_num",student_name.split("-")[0]);
            }
            if(map!=null){
                dataRequest1.add("raw_glory_name",CommonMethod.getString(map,"glory_name"));
                dataRequest1.add("raw_glory_type",CommonMethod.getString(map,"glory_type"));
                dataRequest1.add("raw_glory_level",CommonMethod.getString(map,"glory_level"));
            }
            if(gloryUpdateTextField.getText()==null||gloryUpdateTextField.getText().equals("")||gloryTypeEditComboBox.getValue()==null||gloryTypeEditComboBox.getValue().toString().equals("请选择荣誉类型")||gloryLevelUpdateTextField.getText()==null||gloryLevelUpdateTextField.getText().equals("")){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("请填写完整信息");
                alert.showAndWait();
            }
            dataRequest1.add("glory_name", gloryUpdateTextField.getText());
            dataRequest1.add("glory_type", gloryTypeEditComboBox.getValue());
            dataRequest1.add("glory_level", gloryLevelUpdateTextField.getText());
            if (editConfirmButton.getText() == "修改荣誉") {
                String msg = CommonMethod.alertButton("修改");
                if (msg == "确认") {
                    HttpRequestUtils.request("/glory/updateGlory", dataRequest1);
                }
            } else if (editConfirmButton.getText() == "增加荣誉") {
                String msg = CommonMethod.alertButton("增加");
                if (msg == "确认") {
                    HttpRequestUtils.request("/glory/insertGlory", dataRequest1);
                }
            }
        } else if (student_name == null && glory_type != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选中学生");
            alert.showAndWait();
        } else if (student_name != null && glory_type == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选中荣誉类型");
            alert.showAndWait();
        } else if (student_name == null && glory_type == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选中学生和荣誉类型");
            alert.showAndWait();
        }
        studentComboBox.setValue("");
        studentComboBox.setPromptText("请选择学生");
        onResetButtonClick();
        onQueryButtonClick();
        if(!id.isVisible()&&(studentComboBox.getValue()==null||studentComboBox.getValue().toString().equals("请选择学生"))){
            id.setVisible(true);
        }
    }


    @FXML
    public void initialize() {
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_num"));  //设置列值工程属性
        gloryNameColumn.setCellValueFactory(new MapValueFactory<>("glory_name"));
        gloryTypeColumn.setCellValueFactory(new MapValueFactory<>("glory_type"));
        gloryLevelColumn.setCellValueFactory(new MapValueFactory<>("glory_level"));

        editTabPane.setVisible(false);

        User user = AppStore.getUser();
        if (user.getUser_type_id() == 3) {

            viewTab.setText("我的荣誉");

            addButton.setDisable(true);
            deleteButton.setDisable(true);
            editButton.setDisable(true);
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            one.setVisible(false);
            studentComboBox.setVisible(false);
            queryButton.setVisible(false);
            id.setVisible(false);

            Text text = new Text("( 提醒：若要添加/修改您所获得的荣誉，请通知联系管理员或老师进行操作 )");
            text.setLayoutX(89.0);
            text.setLayoutY(34.0);
            text.setFill(javafx.scene.paint.Color.valueOf("#e21e1e"));
            text.setFont(javafx.scene.text.Font.font("System Bold", 14.0));
            anchor.getChildren().add(text);

            Text glory = new Text("荣誉:");
            glory.setLayoutX(710.0);
            glory.setLayoutY(34.0);
            text.setFont(javafx.scene.text.Font.font("System Bold", 14.0));
            anchor.getChildren().add(glory);

            TextField textField = new TextField();
            textField.setPromptText("请输入荣誉信息");
            textField.setLayoutX(755.0);
            textField.setLayoutY(14.0);
            textField.setPrefHeight(28.0);
            textField.setPrefWidth(130.0);
            anchor.getChildren().add(textField);


            Button gloryButton = new Button("查询");
            gloryButton.setLayoutX(900.0);
            gloryButton.setLayoutY(14.0);
            gloryButton.setPrefHeight(32.0);
            gloryButton.setPrefWidth(55.0);
            gloryButton.setOnAction(event -> {
                List<Map> list = new ArrayList<>();
                if (textField.getText() == "" || textField.getText() == null) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("请输入荣誉信息后查询");
                    alert.showAndWait();
                    return;
                }
                list = CommonMethod.filter(gloryList, "glory_name", textField.getText());
                if (list.size() == 0) {
                    list = CommonMethod.filter(gloryList, "glory_type", textField.getText());
                    if (list.size() == 0) {
                        list = CommonMethod.filter(gloryList, "glory_level", textField.getText());
                    }
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
            anchor.getChildren().add(gloryButton);

        }

        DataRequest dataRequest = new DataRequest();
        List studentList = new ArrayList();
        List gloryTypeList = new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", dataRequest);

        Map cancelStudent = new HashMap();
        cancelStudent.put("cancelStudent", "请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        Map cancelGloryType = new HashMap();
        cancelGloryType.put("cancelGloryType", "请选择荣誉类型");
        gloryTypeList.add(cancelGloryType.get("cancelGloryType"));
        List<Map> studentMap = (List<Map>) studentResult.getData();
        String[] strings = {"体育赛事", "科技创新", "学术竞赛", "评奖评优"};
        for (int i = 0; i < 4; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(i + "", strings[i]);
            gloryTypeList.add(map.get("" + i));
        }
        for (Map student : studentMap) {
            DataRequest dataRequest1=new DataRequest();
            dataRequest1.add("id",student.get("person_id"));
            Result result = HttpRequestUtils.request("/person/selectById", dataRequest1);
            Map map=(Map) result.getData();
            studentList.add(map.get("person_num")+"-"+student.get("student_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        studentEditComboBox.getItems().addAll(studentList);
        gloryTypeEditComboBox.getItems().addAll(gloryTypeList);

        dataTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1 && e.getButton() == MouseButton.PRIMARY) {
                Map map = dataTableView.getSelectionModel().getSelectedItem();
                if (map != null) {
                    onEditButtonClick();
                }
            }
        });

        studentComboBox.setEditable(true);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onResetButtonClick();
    }
}
