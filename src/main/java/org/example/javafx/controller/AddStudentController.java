package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddStudentController {

    @FXML
    private TableColumn classesColumn;


    @FXML
    private TableColumn departmentColumn;

    @FXML
    private TableColumn gradeColumn;


    @FXML
    private TableColumn idColumn;


    @FXML
    private TableColumn majorColumn;


    @FXML
    private TableColumn person_numColumn;

    @FXML
    private TextField select;

    @FXML
    private TableColumn student_nameColumn;


    @FXML
    private TableView<Map> tableView;

    @FXML
    Button selectButton;


    @FXML
    private ComboBox selectChoiceComboBox;
    @FXML
    Button add;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
        selectChoiceComboBox.getItems().addAll("学号", "姓名", "部门", "班级", "年级", "专业");

        Result studentResult = HttpRequestUtils.request("/student/getStudentList", new DataRequest());
        List<Map> studentList = (List<Map>) studentResult.getData();
        tableView.setItems(FXCollections.observableList(studentList));
    }

    @FXML
    public void onSelectAction() {
        if (selectChoiceComboBox.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择查询种类");
            alert.showAndWait();
        } else {
            String selectedOption = translate((String) selectChoiceComboBox.getSelectionModel().getSelectedItem());
            String searchText = select.getText().trim();

            // 发起请求，获取所有学生数据
            DataRequest req = new DataRequest();
            Result studentResult = HttpRequestUtils.request("/student/getStudentList", req);


            if (studentResult.getCode() == 200) {
                List<Map> studentMap = (List<Map>) studentResult.getData();
                // 根据选择的搜索类型和输入的内容进行过滤
                List<Map> filteredStudents = filterStudents(studentMap, selectedOption, searchText);
                // 更新表格显示
                updateTableView(filteredStudents);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("获取学生数据失败。错误：" + studentResult.getMsg());
                alert.showAndWait();
            }
        }
        // 获取用户选择的搜索类型和输入的内容

    }

    private List<Map> filterStudents(List<Map> studentMap, String selectedOption, String searchText) {
        List<Map> filteredStudents = new ArrayList<>();
        // 根据选择的搜索类型和输入的内容对学生数据进行过滤
        for (Map student : studentMap) {
            String value = (String) student.get(selectedOption); // 根据选择的搜索类型获取对应的值
            if (value != null && value.contains(searchText)) { // 如果该值包含输入的内容，则加入过滤后的列表中
                filteredStudents.add(student);
            }
        }
        return filteredStudents;
    }

    private List<Map> catchStudents(List<Map> studentMap, String selectedOption, String searchText) {
        List<Map> caughtStudents = new ArrayList<>();
        // 根据选择的搜索类型和输入的内容对学生数据进行过滤
        for (Map student : studentMap) {
            String value = (String) student.get(selectedOption); // 根据选择的搜索类型获取对应的值
            if (value != null && value.equals(searchText)) { // 如果该值包含输入的内容，则加入过滤后的列表中
                caughtStudents.add(student);
            }
        }
        return caughtStudents;
    }

    private void updateTableView(List<Map> students) {
        // 更新表格显示
        tableView.setItems(FXCollections.observableList(students));
    }

    @FXML
    public String onSelectChoiceComboBoxAction() {
        String selectedOption = (String) selectChoiceComboBox.getSelectionModel().getSelectedItem();

        // 根据选择的搜索类型设置输入框的提示文本
        switch (selectedOption) {
            case "学号":
                select.setPromptText("请输入学号");
                break;
            case "姓名":
                select.setPromptText("请输入姓名");
                break;
            case "部门":
                select.setPromptText("请输入部门");
                break;
            case "班级":
                select.setPromptText("请输入班级");
                break;
            case "年级":
                select.setPromptText("请输入年级");
                break;
            case "专业":
                select.setPromptText("请输入专业");
                break;
            default:
                break;
        }
        return selectedOption;
    }

    public String translate(String selectedOption) {
        switch (selectedOption) {
            case "学号":
                selectedOption = "person_num";
                break;
            case "姓名":
                selectedOption = "student_name";
                break;
            case "部门":
                selectedOption = "department";
                break;
            case "班级":
                selectedOption = "classes";
                break;
            case "年级":
                selectedOption = "grade";
                break;
            case "专业":
                selectedOption = "major";
                break;
            default:
                // Handle default case
                break;
        }
        return selectedOption;
    }

    public void onResetAction() {
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));

        Result studentResult = HttpRequestUtils.request("/student/getStudentList", new DataRequest());
        List<Map> studentList = (List<Map>) studentResult.getData();

        tableView.setItems(FXCollections.observableList(studentList));
    }

    public void add() throws IOException, InterruptedException {
        Map<String,String> map = new HashMap<>();
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            map.put("course_id",CourseSpecificViewController.id);
            map.put("student_id",selectedItem.get("id"));
            DataRequest dataRequest1 = new DataRequest();
            dataRequest1.setData(map);
            Result data1 = HttpRequestUtils.courseField("/course/selectStudentAndCourse", dataRequest1);
            List<Map<String, String>> dataList = new Gson().fromJson(data1.getData().toString(), List.class);
            if(dataList.size() != 0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("该学生已经在课程里");
                alert.showAndWait();
            } else {
                map.put("student_name",selectedItem.get("student_name"));
                DataRequest dataRequest = new DataRequest();
                dataRequest.setData(map);
                Result data = HttpRequestUtils.courseField("/course/addStudent", dataRequest);
                String data2 = data.getMsg();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(data2);
                alert.showAndWait();
                try {
                    // 加载新的FXML文件
                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(MainApplication.class.getResource("course-member.fxml"));
                    Parent root = fxmlLoader.load();
                    // 创建新的Stage
                    Stage newStage = new Stage();
                    newStage.initStyle(StageStyle.DECORATED);
                    newStage.setTitle("成员");
                    newStage.setScene(new Scene(root));
                    newStage.setResizable(false);
                    newStage.show();
                    Node node = add.getScene().getRoot();
                    Window window = node.getScene().getWindow();
                    window.hide(); // 关闭窗口
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择要添加的学生");
            alert.showAndWait();
        }
    }

}
