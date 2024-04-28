package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;

import java.net.URL;
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
    private TextArea editTextArea;

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

    //-------------------------------------------------

    private List<Map> gloryList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表

    //-------------------------------------------------
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        onCancelClick();
        editTabPane.setVisible(true);
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        editConfirmButton.setText("增加荣誉");
        onQueryButtonClick();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        onCancelClick();
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        System.out.println(selected);
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
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_name", student_name);
                dataRequest.add("glory_name", glory_name);
                HttpRequestUtils.request("/glory/deleteGlory", dataRequest);
            }
        }
        onQueryButtonClick();
    }

    Result tmpResult;

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        onCancelClick();
        Map selected = dataTableView.getSelectionModel().getSelectedItem();
        editConfirmButton.setText("修改荣誉");
        editTabPane.setVisible(true);
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        if (selected != null) {
            DataRequest dataRequest = new DataRequest();
            String student_name = CommonMethod.getString(selected, "student_name");
            String glory_name = CommonMethod.getString(selected, "glory_name");
            dataRequest.add("student_name", student_name);
            dataRequest.add("glory_name", glory_name);
            Result result = null;
            System.out.println(dataRequest.getData());
            result = HttpRequestUtils.request("/glory/selectByStudentAndGlory", dataRequest);
            Map map = (Map) result.getData();
            tmpResult = HttpRequestUtils.request("/glory/selectByStudentAndGlory", dataRequest);
            studentEditComboBox.setValue(map.get("student_name"));
            studentEditComboBox.setEditable(false);
            studentEditComboBox.setDisable(true);
            gloryUpdateTextField.setText(map.get("glory_name").toString());
            gloryLevelUpdateTextField.setText(map.get("glory_level").toString());
            gloryTypeEditComboBox.setValue(selected.get("glory_type"));
        }

    }

    @FXML
    private void onQueryButtonClick() {
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
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/glory/selectByStudentName", dataRequest);
            if (result == null) {
                observableList.clear();
                return;
            }
        } else if (student_name == null) {
            result = HttpRequestUtils.request("/glory/getGloryList", new DataRequest());
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
        studentComboBox.setValue("请选择学生");
        result = HttpRequestUtils.request("/glory/getGloryList", new DataRequest());
        result = HttpRequestUtils.request("/glory/getGloryList", new DataRequest());
        setTableViewData(result);
    }

    private void setTableViewData(Result result) {
        observableList.clear();
        if (result.getData() instanceof Map) {
            Map gloryMap = (Map) result.getData();
            System.out.println(gloryMap);
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
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入荣誉名称");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
            return;
        }
        if (gloryLevelUpdateTextField.getText() == "") {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入荣誉级别");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
            return;
        }
        if (student_name != null && glory_type != null) {
            System.out.println(student_name);
            dataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", dataRequest);
            Map map = (Map) result.getData();
            System.out.println(map);
            Integer student_id = Integer.parseInt(map.get("id").toString());
            dataRequest.add("student_id", student_id);
            dataRequest.add("glory_name", gloryUpdateTextField.getText());
            dataRequest.add("glory_type", glory_type);
            if (tmpResult != null) {
                dataRequest.add("raw_glory_name", ((Map) tmpResult.getData()).get("glory_name"));
            }
            dataRequest.add("glory_level", gloryLevelUpdateTextField.getText());
            if (editConfirmButton.getText() == "修改荣誉") {
                String msg = CommonMethod.alertButton("修改");
                if(msg=="确认"){
                    HttpRequestUtils.request("/glory/updateGlory",dataRequest);
                }
            } else if (editConfirmButton.getText() == "增加荣誉") {
                String msg = CommonMethod.alertButton("增加");
                if(msg=="确认"){
                    HttpRequestUtils.request("/glory/updateGlory",dataRequest);
                }
                System.out.println(dataRequest.getData());
            }
            System.out.println(dataRequest.getData());
        } else if (student_name == null && glory_type != null) {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入学生");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        } else if (student_name != null && glory_type == null) {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入荣誉类型");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        } else if (student_name == null && glory_type == null) {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入学生和荣誉类型");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }
        onQueryButtonClick();
    }


    @FXML
    public void initialize() {
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_id"));  //设置列值工程属性
        gloryNameColumn.setCellValueFactory(new MapValueFactory<>("glory_name"));
        gloryTypeColumn.setCellValueFactory(new MapValueFactory<>("glory_type"));
        gloryLevelColumn.setCellValueFactory(new MapValueFactory<>("glory_level"));

        editTabPane.setVisible(false);
        editTextArea.setVisible(false);

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
            studentList.add(student.get("student_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        studentEditComboBox.getItems().addAll(studentList);
        gloryTypeEditComboBox.getItems().addAll(gloryTypeList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onResetButtonClick();

    }
}
