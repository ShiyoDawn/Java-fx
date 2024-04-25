package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.image.ImageView;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import javafx.collections.ObservableList;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;

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

    private Integer id=null;
    @FXML
    public void initialize(){
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_idColumn.setCellValueFactory(new MapValueFactory<>("person_id"));
        student_nameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        departmentColumn.setCellValueFactory(new MapValueFactory<>("department"));
        classesColumn.setCellValueFactory(new MapValueFactory<>("classes"));
        gradeColumn.setCellValueFactory(new MapValueFactory<>("grade"));
        majorColumn.setCellValueFactory(new MapValueFactory<>("major"));
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
    private void onSelectAction(ActionEvent event) {
        String searchText = select.getText().trim();
        if (!searchText.isEmpty()) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("searchText", searchText);
            Result res = HttpRequestUtils.request("/student/searchStudent", dataRequest);

            // 清空表格
            tableView.getItems().clear();

            if (res != null && res.getData() != null) {
                tableView.getItems().addAll((ObservableList) res.getData());
            }
        }
    }
    @FXML
    private void onFuzzySearchStartAction(ActionEvent event) {
        String searchText = select.getText().trim();
        boolean isFuzzy = fuzzySearch.isSelected();
        if (!searchText.isEmpty()) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("searchText", searchText);
            dataRequest.add("isFuzzy", isFuzzy);
            Result res = HttpRequestUtils.request("/student/fuzzySearchStudent", dataRequest);

            // 清空表格
            tableView.getItems().clear();

            if (res != null && res.getData() != null) {
                tableView.getItems().addAll((ObservableList) res.getData());
            }
        }
    }
}

