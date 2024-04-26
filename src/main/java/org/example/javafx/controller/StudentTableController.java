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
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import javafx.collections.ObservableList;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;

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
    private TableColumn person_idColumn;

    @FXML
    private TextField person_idText;

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

    private Integer id=null;
    // 原始学生数据列表
    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_idColumn.setCellValueFactory(new MapValueFactory<>("person_id"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
        selectChoiceComboBox.getItems().addAll("学号","姓名","部门","班级","年级","专业");
        DataRequest req = new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req);
        List<Map> studentMap = (List<Map>) studentResult.getData();
        tableView.setItems(FXCollections.observableList(studentMap));
        save.setVisible(false);
    }
    public void clearPanel(){
        id=null;
        person_idText.setText("");
        student_nameText.setText("");
        departmentText.setText("");
        classText.setText("");
        gradeText.setText("");
        majorText.setText("");
    }


    public void addStudentButtonClick() {

        if (validateInput()) {
            DataRequest req = new DataRequest();
            req.add("person_id", person_idText.getText().trim());
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
                System.err.println("Failed to add student. Error: " + addResult.getMsg());
            }
        } else {
            System.err.println("Invalid input. Please check the input fields.");
        }
    }

    private boolean validateInput() {
        return !person_idText.getText().trim().isEmpty() &&
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
            String person_id=selectedItem.get("person_id");
            String student_name = selectedItem.get("student_name");
            DataRequest req = new DataRequest();
            req.add("person_id", person_id); // 将 person_id 转换为字符串
            req.add("student_name", student_name);
            Result deleteResult = HttpRequestUtils.request("/student/deleteStudent", req);
            if (deleteResult.getCode() == 200) {
                refreshTable();
                // You can add a success message prompt here if needed
            } else {
                // Handle the error, e.g., show a message to the user
                System.err.println("Failed to delete student. Error: " + deleteResult.getMsg());
            }
        } else {
            System.err.println("Please select a student to delete.");
        }
    }
    @FXML
    public void saveAction() {
        if (validateInput()) {
            if (id != null) {
                DataRequest req = new DataRequest();
                req.add("id", id.toString());
                req.add("person_id", person_idText.getText().trim());
                req.add("student_name", student_nameText.getText().trim());
                req.add("department", departmentText.getText().trim());
                req.add("classes", classText.getText().trim());
                req.add("grade", gradeText.getText().trim());
                req.add("major", majorText.getText().trim());

                Result updateResult = HttpRequestUtils.request("/student/updateStudent", req);

                if (updateResult.getCode() == 200) {
                    clearPanel();
                    refreshTable();
                } else {
                    System.err.println("Failed to update student. Error: " + updateResult.getMsg());
                }
            } else {
                System.err.println("Please select a student to update.");
            }
        } else {
            System.err.println("Invalid input. Please check the input fields.");
        }
    }

    @FXML
    public void updateStudentAction() {
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            id = Integer.parseInt(selectedItem.get("id"));
            person_idText.setText(selectedItem.get("person_id"));
            student_nameText.setText(selectedItem.get("student_name"));
            departmentText.setText(selectedItem.get("department"));
            classText.setText(selectedItem.get("classes"));
            gradeText.setText(selectedItem.get("grade"));
            majorText.setText(selectedItem.get("major"));

            save.setVisible(true);
        } else {
            System.err.println("Please select a student to update.");
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

            // 根据选择的搜索类型和输入的内容进行过滤
            List<Map> filteredStudents = filterStudents(studentMap, selectedOption, searchText);
            // 更新表格显示
            updateTableView(filteredStudents);
        } else {
            System.err.println("Failed to fetch student data. Error: " + studentResult.getMsg());
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

    public void onFuzzySearchStartAction(ActionEvent actionEvent) {
    }
}