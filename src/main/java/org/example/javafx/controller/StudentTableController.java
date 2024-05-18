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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import javafx.collections.ObservableList;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
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
    private Button confirm;


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
    private ImageView imageView;

    @FXML
    private Label infoLabel;

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

    private Integer id = null;

    private static Boolean bool = false;

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
        if (selectChoiceComboBox.getItems().isEmpty()) {
            selectChoiceComboBox.getItems().addAll("学号", "姓名", "部门", "班级", "年级", "专业");
        }
        person_numText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                person_numText.setText(oldValue);
            } else if (newValue.length() < 12) {
                infoLabel.setText("请在框内输入12位学号");
                infoLabel.setTextFill(Color.RED);
            } else if (newValue.length() == 12) {
                infoLabel.setText("学号已满12位");
                infoLabel.setTextFill(Color.GREEN);
            } else {
                person_numText.setText(oldValue);
            }
        });
        gradeText.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) {
                if (!newValue.matches("大一|大二|大三|大四")) {
                    infoLabel.setText("年级输入格式错误，请输入大一or大二or大三or大四");
                    infoLabel.setTextFill(Color.RED);
                } else {
                    infoLabel.setText("年级输入格式正确");
                    infoLabel.setTextFill(Color.GREEN);
                }
            }
        });


        try {
            FileInputStream fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\css\\nobodyPhoto.png");
            Image image = new Image(fileInputStream);
            imageView.setImage(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        tableView.setOnMouseClicked(e -> {
            Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
            if(selectedItem!=null){
                id = Integer.parseInt(selectedItem.get("id"));
                person_numText.setText(selectedItem.get("person_num"));
                student_nameText.setText(selectedItem.get("student_name"));
                departmentText.setText(selectedItem.get("department"));
                classText.setText(selectedItem.get("classes"));
                gradeText.setText(selectedItem.get("grade"));
                majorText.setText(selectedItem.get("major"));
                person_numText.setDisable(true);
                person_numText.setOpacity(0.5);
                student_nameText.setDisable(true);
                student_nameText.setOpacity(0.5);
                departmentText.setDisable(true);
                departmentText.setOpacity(0.5);
                classText.setDisable(true);
                classText.setOpacity(0.5);
                gradeText.setDisable(true);
                gradeText.setOpacity(0.5);
                majorText.setDisable(true);
                majorText.setOpacity(0.5);
                User user = AppStore.getUser();
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("person_num", person_numText.getText());
                Result result = HttpRequestUtils.request("/user/getPhoto", dataRequest);
                if(result.getCode()==-1){
                    FileInputStream fileInputStream = null;
                    try {
                        fileInputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\css\\nobodyPhoto.png");
                    } catch (FileNotFoundException error) {
                        throw new RuntimeException(error);
                    }
                    Image image = new Image(fileInputStream);
                    imageView.setImage(image);
                }
                if (result != null) {
                    if (result.getCode() != -1) {
                        String str = result.getData().toString();
                        byte[] data = Base64.getDecoder().decode(str);
                        if (data != null) {
                            Image image1 = new Image(new ByteArrayInputStream(data));
                            imageView.setImage(image1);
                        }
                    }
                }
                imageView.setOpacity(0.9);
                imageView.setDisable(true);
            }
        });


        Result studentResult = HttpRequestUtils.request("/student/getStudentList", new DataRequest());
        List<Map> studentList = (List<Map>) studentResult.getData();
        if (studentList == null || studentList.isEmpty()) {
            return;
        }
        tableView.setItems(FXCollections.observableList(studentList));
        add.setDisable(false);
        add.setOpacity(1.0);
        update.setDisable(false);
        update.setOpacity(1.0);
        save.setVisible(false);
        save.setDisable(true);
//        save.setOpacity(0.5);
        confirm.setVisible(false);
        confirm.setDisable(true);
    }

    public void clearPanel() {
        id = null;
        person_numText.setText("");
        student_nameText.setText("");
        departmentText.setText("");
        classText.setText("");
        gradeText.setText("");
        majorText.setText("");
        imageView.setImage(null);
    }


    public void addStudentButtonClick() {
//
        save.setVisible(false);
        save.setDisable(true);
        clearPanel();
        add.setDisable(true);
        add.setOpacity(0.5);
        confirm.setVisible(true);
        confirm.setDisable(false);
        person_numText.setDisable(false);
        person_numText.setOpacity(1.0);
        student_nameText.setDisable(false);
        student_nameText.setOpacity(1.0);
        departmentText.setDisable(false);
        departmentText.setOpacity(1.0);
        classText.setDisable(false);
        classText.setOpacity(1.0);
        gradeText.setDisable(false);
        gradeText.setOpacity(1.0);
        majorText.setDisable(false);
        majorText.setOpacity(1.0);

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
        initialize();
    }

    public void deleteStudentButtonClick() {
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            String id = selectedItem.get("id");
            String person_num = selectedItem.get("person_num");
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
        if(!gradeText.getText().matches("大一|大二|大三|大四")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("年级输入格式错误，请输入大一or大二or大三or大四");
            alert.showAndWait();
            return;
        }
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
                    save.setVisible(false);
                    update.setDisable(false);
                    update.setOpacity(1.0);
                    person_numText.setDisable(true);
                    person_numText.setOpacity(0.5);
                    student_nameText.setDisable(true);
                    student_nameText.setOpacity(0.5);
                    departmentText.setDisable(true);
                    departmentText.setOpacity(0.5);
                    classText.setDisable(true);
                    classText.setOpacity(0.5);
                    gradeText.setDisable(true);
                    gradeText.setOpacity(0.5);
                    majorText.setDisable(true);
                    majorText.setOpacity(0.5);
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
        update.setDisable(true);
        update.setOpacity(0.5);
        confirm.setVisible(false);
        confirm.setDisable(true);
        Map<String, String> selectedItem = tableView.getSelectionModel().getSelectedItem();
        if(selectedItem == null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("警告");
            alert.setHeaderText(null);
            alert.setContentText("请选择要修改的信息");
            alert.showAndWait();
            return;
        }
        save.setVisible(true);
        save.setDisable(false);
        save.setOpacity(1.0);
        student_nameText.setDisable(false);
        student_nameText.setOpacity(1.0);
        departmentText.setDisable(false);
        departmentText.setOpacity(1.0);
        classText.setDisable(false);
        classText.setOpacity(1.0);
        gradeText.setDisable(false);
        gradeText.setOpacity(1.0);
        majorText.setDisable(false);
        majorText.setOpacity(1.0);


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
            if (bool) {
                // 根据选择的搜索类型和输入的内容进行过滤
                List<Map> filteredStudents = filterStudents(studentMap, selectedOption, searchText);
                // 更新表格显示
                updateTableView(filteredStudents);
            } else {
                // 根据选择的搜索类型和输入的内容进行筛查
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
        clearPanel();
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
                // Handle default case
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

    @FXML
    public void onFuzzySearchStartAction() {
        if (fuzzySearch.isSelected()) {
            bool = true;
        } else {
            bool = false;
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
        initialize();
    }

    public void confirmAction() {
        if (!gradeText.getText().equals("大一") && !gradeText.getText().equals("大二") && !gradeText.getText().equals("大三") && !gradeText.getText().equals("大四")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("年级输入格式错误，请输入大一or大二or大三or大四");
            alert.showAndWait();
            return;
        }
        if (validateInput()) {
            DataRequest req = new DataRequest();
            req.add("person_num", person_numText.getText().trim());
            req.add("student_name", student_nameText.getText().trim());
            req.add("department", departmentText.getText().trim());
            req.add("classes", classText.getText().trim());
            req.add("grade", gradeText.getText().trim());
            req.add("major", majorText.getText().trim());

            Result addResult = HttpRequestUtils.request("/student/insertStudent", req);
            String msg = addResult.getMsg();
            if (msg.equals("学生已存在")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("添加学生失败。错误：" + addResult.getMsg());
                alert.showAndWait();
            }
            else if (msg.equals("人员不存在")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("添加学生失败。错误：" + "人员不存在，请先到人员管理添加人员信息。");
                alert.showAndWait();
            }
            else if (msg.equals("添加成功")){
                clearPanel();
                refreshTable();
                person_numText.setDisable(true);
                person_numText.setOpacity(0.5);
                student_nameText.setDisable(true);
                student_nameText.setOpacity(0.5);
                departmentText.setDisable(true);
                departmentText.setOpacity(0.5);
                classText.setDisable(true);
                classText.setOpacity(0.5);
                gradeText.setDisable(true);
                gradeText.setOpacity(0.5);
                majorText.setDisable(true);
                majorText.setOpacity(0.5);
                add.setDisable(false);
                add.setOpacity(1.0);
                confirm.setVisible(false);
                confirm.setDisable(true);
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

    public void onStatisticButtonClickAction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/student-classification.fxml"));
            Parent parent = loader.load();
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("学生统计");
            stage.setScene(new Scene(parent));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}