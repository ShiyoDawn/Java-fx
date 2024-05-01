package org.example.javafx.controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import javafx.collections.ObservableList;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StudentTableController {

    @FXML
    private Button add;

    @FXML
    private TextField classText;

    @FXML
    private TableColumn classesColumn;

    @FXML
    private Button delete;

    @FXML
    private TableColumn departmentColumn;

    @FXML
    private TextField departmentText;

    @FXML
    private CheckBox fuzzySearch;

    @FXML
    private TableColumn gradeColumn;

    @FXML
    private TextField gradeText;

    @FXML
    private TableColumn idColumn;

    @FXML
    private ImageView image;

    @FXML
    private TableColumn majorColumn;

    @FXML
    private TextField majorText;

    @FXML
    private TableColumn person_numColumn;

    @FXML
    private TextField person_numText;

    @FXML
    private Button save;

    @FXML
    private TextField select;

    @FXML
    private TableColumn student_nameColumn;

    @FXML
    private TextField student_nameText;

    @FXML
    private TableView<Map> tableView;

    @FXML
    private Button update;

    @FXML
    private ComboBox selectChoiceComboBox;

    @FXML
    private Button moreButton;

    private Integer id=null;

    private static Boolean bool=false;
    // 原始学生数据列表
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
        save.setDisable(true);
        save.setOpacity(0.5);
    }
    public void clearPanel(){
        id=null;
        person_numText.setText("");
        student_nameText.setText("");
        departmentText.setText("");
        classText.setText("");
        gradeText.setText("");
        majorText.setText("");
    }


    public void addStudentButtonClick() {
        if (validateInput()) {
            DataRequest req = new DataRequest();
            req.add("person_num", person_numText.getText().trim());
            req.add("student_name", student_nameText.getText().trim());
            req.add("department", departmentText.getText().trim());
            req.add("classes", classText.getText().trim());
            req.add("grade", gradeText.getText().trim());
            req.add("major", majorText.getText().trim());

            Result addResult = HttpRequestUtils.request("/student/insertStudent", req);

            if (addResult.getCode() == 200) {
                clearPanel();
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("添加学生失败。错误：" + addResult.getMsg());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("输入无效，请检查输入字段。");
            alert.showAndWait();
        }
    }

    private boolean validateInput() {
        return !person_numText.getText().trim().isEmpty() &&
                !student_nameText.getText().trim().isEmpty() &&
                !departmentText.getText().trim().isEmpty() &&
                !classText.getText().trim().isEmpty() &&
                !gradeText.getText().trim().isEmpty() &&
                !majorText.getText().trim().isEmpty();
    }

    private void refreshTable() {
        DataRequest req = new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req);
        List<Map> studentMap = (List<Map>) studentResult.getData();
        tableView.setItems(FXCollections.observableList(studentMap));
    }
    public void deleteStudentButtonClick() {
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String id=selectedItem.get("id");
            String person_num=selectedItem.get("person_num");
            String student_name = selectedItem.get("student_name");
            DataRequest req = new DataRequest();
            req.add("id", id);
            req.add("person_num", person_num); // 将 person_id 转换为字符串
            req.add("student_name", student_name);
            Result deleteResult = HttpRequestUtils.request("/student/deleteStudent", req);
            if (deleteResult.getCode() == 200) {
                clearPanel();
                refreshTable();
                // You can add a success message prompt here if needed
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("删除失败，错误：" + deleteResult.getMsg());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择要删除的信息");
            alert.showAndWait();
        }
    }
    @FXML
    public void saveAction() {
        if (validateInput()) {
            if (id != null) {
                DataRequest req = new DataRequest();
                req.add("id", id.toString());
                req.add("person_num", person_numText.getText().trim());
                req.add("student_name", student_nameText.getText().trim());
                System.out.println(student_nameText.getText().trim());
                req.add("department", departmentText.getText().trim());
                req.add("classes", classText.getText().trim());
                req.add("grade", gradeText.getText().trim());
                req.add("major", majorText.getText().trim());

                Result updateResult = HttpRequestUtils.request("/student/updateStudent", req);

                if (updateResult.getCode() == 200) {
                    clearPanel();
                    refreshTable();
                    save.setDisable(true);
                    save.setOpacity(0.5);
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("错误");
                    alert.setHeaderText(null);
                    alert.setContentText("修改学生信息失败。错误：" + updateResult.getMsg());
                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("警告");
                alert.setHeaderText(null);
                alert.setContentText("请选择要修改的信息");
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("输入无效，请检查输入字段。");
            alert.showAndWait();
        }
    }

    @FXML
    public void updateStudentAction() {
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = Integer.parseInt(selectedItem.get("id"));
            person_numText.setText(selectedItem.get("person_num"));
            student_nameText.setText(selectedItem.get("student_name"));
            departmentText.setText(selectedItem.get("department"));
            classText.setText(selectedItem.get("classes"));
            gradeText.setText(selectedItem.get("grade"));
            majorText.setText(selectedItem.get("major"));
            save.setDisable(false);
            save.setOpacity(1.0);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择要修改的信息");
            alert.showAndWait();
        }

    }
    @FXML
    public void onSelectAction() {
        // 获取用户选择的搜索类型和输入的内容
        String selectedOption = translate((String) selectChoiceComboBox.getSelectionModel().getSelectedItem());
        String searchText = select.getText().trim();

        // 发起请求，获取所有学生数据
        DataRequest req = new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req);


        if (studentResult.getCode() == 200) {
            List<Map> studentMap = (List<Map>) studentResult.getData();
            if(bool){
                // 根据选择的搜索类型和输入的内容进行过滤
                List<Map> filteredStudents = filterStudents(studentMap, selectedOption, searchText);
                // 更新表格显示
                updateTableView(filteredStudents);
            }
            else {
                // 根据选择的搜索类型和输入的内容进行过滤
                List<Map> caughtStudents = catchStudents(studentMap, selectedOption, searchText);
                // 更新表格显示
                updateTableView(caughtStudents);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("获取学生数据失败。错误：" + studentResult.getMsg());
            alert.showAndWait();
        }
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
    private List<Map> catchStudents(List<Map> studentMap, String selectedOption, String searchText){
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
                // Handle default case
                break;
        }
        return selectedOption;
    }
    public String translate(String selectedOption){
        switch (selectedOption) {
            case "学号":
                selectedOption="person_id";
                break;
            case "姓名":
                selectedOption="student_name";
                break;
            case "部门":
                selectedOption="department";
                break;
            case "班级":
                selectedOption="classes";
                break;
            case "年级":
                selectedOption="grade";
                break;
            case "专业":
                selectedOption="major";
                break;
            default:
                // Handle default case
                break;
        }
        return selectedOption;
    }
    @FXML
    public void onFuzzySearchStartAction() {
        if(fuzzySearch.isSelected()) {
            bool=true;
        }
        else {
            bool=false;
        }
    }
    @FXML
    void onMoreButtonAction() {
        // 获取选中的行
        Map<String, String> selectedStudent = tableView.getSelectionModel().getSelectedItem();

        // 检查是否选中了行
        if (selectedStudent != null) {
            try {
                // 加载新窗口的FXML文件
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/student-information.fxml"));
                Parent parent = loader.load();

                // 获取Controller并传递选中的学生对象
                StudentInformationController controller = loader.getController();
                controller.initData(selectedStudent);

                // 创建一个新的舞台
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("更多信息");
                stage.setScene(new Scene(parent));

                // 显示新窗口
                stage.showAndWait();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // 如果没有选中行，则显示警告信息
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择一条信息");
            alert.showAndWait();
        }
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
        save.setDisable(true);
        save.setOpacity(0.5);
    }
}