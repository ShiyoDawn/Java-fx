package org.example.javafx.controller;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PersonTableController {

    @FXML
    private TableColumn birthdayColumn;

    @FXML
    private Button deleteButton;

    @FXML
    private TableColumn emailColumn;

    @FXML
    private CheckBox fuzzySearchCheckBox;

    @FXML
    private TableColumn genderColumn;

    @FXML
    private TableColumn idColumn;

    @FXML
    private TableColumn identityColumn;

    @FXML
    private TableColumn identity_numberColumn;

    @FXML
    private Button insertButton;

    @FXML
    private TableColumn person_nameColumn;

    @FXML
    private TableColumn person_numColumn;

    @FXML
    private TableColumn phone_numberColumn;

    @FXML
    private Button resetButton;

    @FXML
    private Button searchButton;

    @FXML
    private ComboBox selectChoiceComboBox;

    @FXML
    private TextField selectTextField;

    @FXML
    private Button statisticButton;

    @FXML
    private TableView<Map> tableView;


    @FXML
    private Button upadateButton;

    @FXML
    private TableColumn user_typeColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(new MapValueFactory<>("id"));
        person_nameColumn.setCellValueFactory(new MapValueFactory<>("person_name"));
        genderColumn.setCellValueFactory(new MapValueFactory<>("gender"));
        phone_numberColumn.setCellValueFactory(new MapValueFactory<>("phone_number"));
        identityColumn.setCellValueFactory(new MapValueFactory<>("identity"));
        person_numColumn.setCellValueFactory(new MapValueFactory<>("person_num"));
        birthdayColumn.setCellValueFactory(new MapValueFactory<>("birthday"));
        user_typeColumn.setCellValueFactory(new MapValueFactory<>("user_type"));
        emailColumn.setCellValueFactory(new MapValueFactory<>("email"));
        identity_numberColumn.setCellValueFactory(new MapValueFactory<>("identity_number"));
        if (selectChoiceComboBox.getItems().isEmpty()) {
            selectChoiceComboBox.getItems().addAll("姓名","性别","联系电话","身份","工号","出生日期","用户类型","邮箱","身份证号");
        }
        Result personList = HttpRequestUtils.request("/person/getAll",new DataRequest());
        List<Map> persons = (List<Map>) personList.getData();
        if (persons==null) {
            return;
        }
        tableView.setItems(FXCollections.observableList(persons));
    }
    @FXML
    void onDeleteButtonClickAction() {
        Map<String, Object> selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("id", selectedPerson.get("id"));
            Result result = HttpRequestUtils.request("/person/deletePerson", dataRequest);
            if (result.getCode() == 200) {
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText("删除失败");
                alert.setContentText(result.getMsg());
                alert.showAndWait();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("未选择人员");
            alert.setContentText("请先选择一个人员");
            alert.showAndWait();
        }
    }

    @FXML
    void onInsertButtonClickAction() {
        try {
            // 加载FXML文件
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/person-add.fxml"));
            Parent root = loader.load();

            PersonAddController controller = loader.getController();
            controller.initialize();

            // 创建新的Stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("添加人员");
            stage.setScene(new Scene(root));

            // 显示新窗口
            stage.showAndWait();
            refreshTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void onResetButtonAction() {
        refreshTable();
    }
    private void refreshTable() {
        Result personList = HttpRequestUtils.request("/person/getAll",new DataRequest());
        List<Map> persons = (List<Map>) personList.getData();
        if (persons==null) {
            return;
        }
        tableView.setItems(FXCollections.observableList(persons));
    }

    @FXML
    void onSelectButtonAction() {
        if(selectChoiceComboBox.getValue() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("请选择搜索类型");
            alert.setContentText("请选择搜索类型");
            alert.showAndWait();
            return;
        }
        String selectChoice = translateChoice((String) selectChoiceComboBox.getValue());
        String selectText = selectTextField.getText();
        if(selectText == null || selectText.equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("请输入搜索内容");
            alert.setContentText("请输入搜索内容");
            alert.showAndWait();
            return;
        }
        DataRequest dataRequest = new DataRequest();
        Result result = HttpRequestUtils.request("/person/getAll", dataRequest);
        if (result.getCode() == 200) {
            List<Map> personMap = (List<Map>) result.getData();
            if(fuzzySearchCheckBox.isSelected()) {
                List<Map> filteredPersons = filterPersons(personMap, selectChoice, selectText);
                tableView.setItems(FXCollections.observableList(filteredPersons));
            }else {
                List<Map> caughtPersons = catchPersons(personMap, selectChoice, selectText);
                tableView.setItems(FXCollections.observableList(caughtPersons));
            }
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText("获取人员列表失败。错误："+result.getMsg());
            alert.setContentText(result.getMsg());
            alert.showAndWait();
        }
    }
    private String translateChoice(String selectedChoice) {
        //"姓名","性别","联系电话","身份","工号","出生日期","用户类型","邮箱","身份证号"
        switch (selectedChoice) {
            case "姓名":
                return "person_name";
            case "性别":
                return "gender";
            case "联系电话":
                return "phone_number";
            case "身份":
                return "identity";
            case "工号":
                return "person_num";
            case "出生日期":
                return "birthday";
            case "用户类型":
                return "user_type";
            case "邮箱":
                return "email";
            case "身份证号":
                return "identity_number";
            default:
                return null;
        }
    }
    @FXML
    public String onSelectChoiceComboBoxAction(){
        String selectedChoice = (String) selectChoiceComboBox.getValue();
        switch (selectedChoice) {
            case "姓名":
                selectTextField.setPromptText("请输入姓名");
                break;
            case "性别":
                selectChoiceComboBox.setPromptText("请输入性别");
                break;
            case "联系电话":
                selectChoiceComboBox.setPromptText("请输入联系电话");
                break;
            case "身份":
                selectChoiceComboBox.setPromptText("请输入身份");
                break;
            case "工号":
                selectChoiceComboBox.setPromptText("请输入工号");
                break;
            case "出生日期":
                selectChoiceComboBox.setPromptText("请输入出生日期");
                break;
            case "用户类型":
                selectChoiceComboBox.setPromptText("请输入用户类型");
                break;
            case "邮箱":
                selectChoiceComboBox.setPromptText("请输入邮箱");
                break;
            case "身份证号":
                selectChoiceComboBox.setPromptText("请输入身份证号");
                break;
            default:
                break;
        }
        return selectedChoice;
    }
    private List<Map> filterPersons(List<Map> personMap, String selectedChoice, String searchText) {
        List<Map> filteredPersons = new ArrayList<>();
        // 根据选择的搜索类型和输入的内容对学生数据进行过滤
        for (Map person : personMap) {
            String value = (String) person.get(selectedChoice); // 根据选择的搜索类型获取对应的值
            if (value != null && value.contains(searchText)) { // 如果该值包含输入的内容，则加入过滤后的列表中
                filteredPersons.add(person);
            }
        }
        return filteredPersons;
    }

    private List<Map> catchPersons(List<Map> personMap, String selectedChoice, String searchText) {
        List<Map> caughtPersons = new ArrayList<>();
        for (Map person : personMap) {
            String value = (String) person.get(selectedChoice);
            if (value != null && value.equals(searchText)) {
                caughtPersons.add(person);
            }
        }
        return caughtPersons;
    }
    @FXML
    void onStatisticButtonClickAction() {

    }

    @FXML
    void onUpdateButtonClickAction() {
        Map<String, Object> selectedPerson = tableView.getSelectionModel().getSelectedItem();
        if (selectedPerson != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/javafx/person-update.fxml"));
                Parent root = loader.load();

                PersonUpdateController controller = loader.getController();
                controller.initialize(selectedPerson);

                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setTitle("修改人员信息");
                stage.setScene(new Scene(root));

                stage.showAndWait();
                refreshTable();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("提示");
            alert.setHeaderText("未选择人员");
            alert.setContentText("请先选择一个人员");
            alert.showAndWait();
        }
    }
}
