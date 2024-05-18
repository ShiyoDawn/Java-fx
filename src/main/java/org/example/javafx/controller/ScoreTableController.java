package org.example.javafx.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
import org.example.javafx.pojo.User;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.request.OptionItem;
import org.example.javafx.response.DataResponse;
import org.example.javafx.util.CommonMethod;
import org.w3c.dom.events.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ScoreTableController {
    //显示元素

    @FXML
    private TableView<Map> dataTableView;

    @FXML
    private ComboBox courseComboBox;


    @FXML
    private TableColumn<Map, String> courseNameColumn;

    @FXML
    private TableColumn<Map, String> courseNumColumn;

    @FXML
    private TableColumn<Map, String> creditColumn;

    @FXML
    private TableColumn<Map, String> id;

    @FXML
    private TableColumn<Map, String> markColumn;


    @FXML
    private ComboBox studentComboBox;

    @FXML
    private ComboBox studentEditComboBox;

    @FXML
    private TableColumn studentNameColumn;

    @FXML
    private TableColumn studentNumColumn;

    @FXML
    private ComboBox courseEditComboBox;

    @FXML
    private TextField markUpdateTextField;

    @FXML
    private Button addButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button queryButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button editCancelButton;
    @FXML
    private Button editConfirmButton;

    @FXML
    private AnchorPane scoreAnchorPane;

    @FXML
    private BorderPane scoreBorderPane;
    @FXML
    private AnchorPane editAnchorPane;
    @FXML
    private TabPane editTabPane;

    @FXML
    private Label one;

    @FXML
    private Label two;

    @FXML
    private Text three;

    @FXML
    private Label five;

    @FXML
    private ComboBox classComboBox;

    @FXML
    private TableColumn<Map, String> gradePointColumn;

    @FXML
    private TableColumn<Map, String> classColumn;

    @FXML
    private AnchorPane anchor;

    @FXML
    private Tab viewTab;

    private static Label label = new Label();

    private static Label creditLabel = new Label();

    //------------------------------------------------------------

    private List<Map> scoreList = new ArrayList<>(); // 学生信息列表数据
    private ObservableList<Map> observableList = FXCollections.observableArrayList();  // TableView渲染列表


    private ScoreEditController scoreEditController = null;

    public static ScoreTableController scoreTableController;

    private Stage stage = null;


    //------------------------------------------------------------

    //因为不知道出了啥bug，于是为了方便，以下部分“result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());”可为刷新数据库作用
    @FXML
    private void onAddButtonClick(ActionEvent event) {
        onCancelClick();
        editTabPane.setVisible(true);
        editConfirmButton.setText("增加分数");
        onQueryButtonClick();
        //onResetButtonClick();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        onCancelClick();
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        if (selected.size() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择要删除的数据");
            alert.showAndWait();
            return;
        }
        //System.out.println(selected);
        //onResetButtonClick();
        String msg = CommonMethod.alertButton("删除");
        if (msg == "确认") {
            for (Map scoreMap : selected) {
                String student_num = CommonMethod.getString(scoreMap, "student_num");
                String course_num = CommonMethod.getString(scoreMap, "course_num");
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", student_num);
                dataRequest.add("course_num", course_num);
                HttpRequestUtils.request("/score/deleteAllById", dataRequest);
            }
        }
        onQueryButtonClick();
        onQueryButtonClick();
    }

    @FXML
    private void onEditButtonClick() {
        onCancelClick();
        Map selected = dataTableView.getSelectionModel().getSelectedItem();
        editConfirmButton.setText("修改分数");
        editTabPane.setVisible(true);
        if (selected != null) {
            DataRequest dataRequest = new DataRequest();
            String student_num = CommonMethod.getString(selected, "student_num");
            String course_num = CommonMethod.getString(selected, "course_num");
            String student_name = CommonMethod.getString(selected, "student_name");
            String course_name = CommonMethod.getString(selected, "course_name");
            String mark = CommonMethod.getString(selected, "mark");
            studentEditComboBox.setValue(student_num + "-" + student_name);
            courseEditComboBox.setValue(course_num+"-"+course_name);
            studentEditComboBox.setDisable(true);
            courseEditComboBox.setDisable(true);
            markUpdateTextField.setText(mark);
        }else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请选择要编辑的数据");
            alert.showAndWait();
        }
    }

    @FXML
    private void onQueryButtonClick() {
        if (studentComboBox.getSelectionModel().getSelectedItem() != null && studentComboBox.getSelectionModel().getSelectedItem().toString() != "请选择学生" || courseComboBox.getSelectionModel().getSelectedItem() != null && courseComboBox.getSelectionModel().getSelectedItem().toString() != "请选择课程" || classComboBox.getSelectionModel().getSelectedItem() != null && classComboBox.getSelectionModel().getSelectedItem().toString() != "请选择班级") {
            id.setVisible(false);
        }
        if (classComboBox.getSelectionModel().getSelectedItem() != null && classComboBox.getSelectionModel().getSelectedItem().toString() != "请选择班级") {
            Result result = new Result();
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            List<Map> scoreList = (List<Map>) result.getData();
            for (Map score : scoreList) {
                DataRequest dataRequest = new DataRequest();
                //不直接用是怕有重名的人（也没那么夸张）
                dataRequest.add("person_num", score.get("student_num"));
                result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
                Map map = (Map) result.getData();
                dataRequest.add("person_id", map.get("id"));
                result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
                map = (Map) result.getData();
                score.put("class", map.get("classes"));
            }
            result = new Result();
            List<Map> ans = CommonMethod.filter(scoreList, "class", classComboBox.getValue().toString());
            result.setData(ans);
            setTableViewData(result);
            return;
        }

        String student_name = null;
        String course_name = null;
        Object student = studentComboBox.getSelectionModel().getSelectedItem();
        Object course = courseComboBox.getSelectionModel().getSelectedItem();
        Result result = null;
        if (student != null)
            student_name = student.toString();

        if (course != null)
            course_name = course.toString();

        if (student_name == "请选择学生") {
            studentComboBox.setPromptText("请选择学生");
            student_name = null;
        }
        if (course_name == "请选择课程") {
            courseComboBox.setPromptText("请选择课程");
            course_name = null;
        }

        if (student_name != null && course_name != null) {
            //DataRequest stuDataRequest = new DataRequest();
            //stuDataRequest.add("student_name", student_name);
            //result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            List<Map> studentList = CommonMethod.filter(scoreList, "student_num", student_name.split("-")[0]);
            if (studentList == null || studentList.size() == 0) {
                observableList.clear();
                return;
            } else {
                observableList.clear();
                List<Map> ans = new ArrayList<>();
                ans=CommonMethod.filter(studentList,"course_num",course_name.split("-")[0]);
                for (Map map : ans) {
                    observableList.add(map);
                }
                dataTableView.setItems(observableList);
                return;
            }
            /*Map map = (Map) result.getData();
            DataRequest dataRequest=new DataRequest();
            dataRequest.add("id",map.get("person_id"));
            result= HttpRequestUtils.request("/person/selectById",dataRequest);
            String student_num=((Map)result.getData()).get("person_num").toString();

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("id", course_name.split("-")[0]);
            result = HttpRequestUtils.request("/course/selectInfo", courDataRequest);
            if (result == null) {
                observableList.clear();
                return;
            }
            map = (Map) result.getData();
            String course_num = map.get("num").toString();

            dataRequest = new DataRequest();

            dataRequest.add("student_num", student_num);
            dataRequest.add("course_num", course_num);
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            result = HttpRequestUtils.request("/score/selectByStudentAndCourse", dataRequest);
            System.out.println(map.get("student_num") + " " + map.get("course_num") + " " + map.get("mark"));
            if (result == null) {
                observableList.clear();
                return;
            }
            */
        } else if (student_name != null && course_name == null) {
            //DataRequest dataRequest = new DataRequest();
            //dataRequest.add("student_name", student_name);
            //result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            //result = HttpRequestUtils.request("/score/selectByStudentName", dataRequest);
            List<Map> studentList = CommonMethod.filter(scoreList, "student_num", student_name.split("-")[0]);
            Result result1=new Result();
            result1.setData(studentList);
            setTableViewData(result1);
            return;
        } else if (student_name == null && course_name != null) {
            List<Map> courseList = new ArrayList<>();
            courseList=CommonMethod.filter(scoreList,"course_num",course_name.split("-")[0]);
            if (courseList == null || courseList.size() == 0) {
                observableList.clear();
                return;
            } else {
                Result result1=new Result();
                result1.setData(courseList);
                setTableViewData(result1);
                return;
            }
            /*DataRequest dataRequest = new DataRequest();
            dataRequest.add("id", course_name.split("-")[0]);
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());

            result = HttpRequestUtils.request("/course/selectInfo", dataRequest);
            Map map = (Map) result.getData();
            DataRequest newDataRequest = new DataRequest();
            newDataRequest.add("course_num", map.get("num"));
            result = HttpRequestUtils.request("/score/selectByCourseId", newDataRequest);
            System.out.println(dataTableView.getItems());
            if (result == null) {
                observableList.clear();
                return;
            }*/
        } else if (student_name == null && course_name == null) {
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        }
        /*if (result == null) {
            return;
        }*/
        setTableViewData(result);
    }

    @FXML
    public void onResetButtonClick() {
        initialize();
        User user = AppStore.getUser();
        Result result = new Result();
        id.setVisible(true);
        studentEditComboBox.setDisable(false);
        courseEditComboBox.setDisable(false);
        if (user.getUser_type_id() == 3) {
            id.setVisible(false);
            String person_num = user.getPerson_num();
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", person_num);
            result = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
            Map map = (Map) result.getData();
            dataRequest.add("person_id", Integer.parseInt(map.get("id").toString().split("\\.")[0]));
            result = HttpRequestUtils.request("/student/selectStudentByPid", dataRequest);
            map = (Map) result.getData();
            String student_name = map.get("student_name").toString();
            studentComboBox.setValue(user.getPerson_num() + "-" + student_name);
            courseComboBox.setValue(null);
            studentComboBox.setPromptText("请选择学生");
            courseComboBox.setPromptText("请选择课程");
            result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        } else {
            studentComboBox.setValue(null);
            courseComboBox.setValue(null);
            classComboBox.setValue(null);
            studentComboBox.setDisable(false);
            courseComboBox.setDisable(false);
            studentComboBox.setPromptText("请选择学生");
            courseComboBox.setPromptText("请选择课程");
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        }
        setTableViewData(result);
    }

    //显示成绩总表
    public void setTableViewData(Result result) {
        observableList.clear();
        //int index=1;
        if (result != null) {

            if (result.getData() instanceof Map) {
                Map scoreMap = (Map) result.getData();
                //scoreMap.put("student_id",(Integer))
            /*Button editButton;
            editButton = new Button("编辑");
            editButton.setId("edit"+index);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            index++;
            scoreMap.put("operateColumn", editButton);*/
                if(AppStore.getUser().getUser_type_id()==3){
                    if(scoreMap.get("student_num").equals(AppStore.getUser().getPerson_num())){
                        observableList.add(scoreMap);
                    }
                }else{
                    observableList.add(scoreMap);
                }
            } else if (result.getData() instanceof ArrayList) {
                //Button editButton;
                scoreList = (ArrayList) result.getData();
                if (AppStore.getUser().getUser_type_id() == 3) {
                    for (Map scoreMap : scoreList) {
                        if (scoreMap.get("student_num").equals(AppStore.getUser().getPerson_num())) {
                            observableList.add(scoreMap);
                        }
                    }
                }else{
                    for(Map scoreMap:scoreList){
                        observableList.add(scoreMap);
                    }
                }
            }
            if (AppStore.getUser().getUser_type_id() == 3) {
                Double totalCredit = 0.0;
                Double totalGradePoint = 0.0;
                scoreList=CommonMethod.filter(scoreList,"student_num",AppStore.getUser().getPerson_num());
                for (Map map : scoreList) {
                    double credit = Double.parseDouble(map.get("credit").toString());
                    totalCredit += credit;
                    double mark = Double.parseDouble(map.get("mark").toString());
                    double gradePoint = mark < 60 ? 0 : mark / 10 - 5;
                    gradePoint = (double) Math.round(gradePoint * 100) / 100;
                    totalGradePoint += credit * gradePoint;
                }
                Double GPA = totalGradePoint / totalCredit;
                GPA = (double) Math.round(GPA * 100) / 100;
                three.setText("您的平均绩点为：");
                three.setLayoutX(74.0);
                three.setLayoutY(34.0);


                label.setFont(new Font("System Bold", 14.0));
                label.setLayoutX(180.0);
                label.setLayoutY(20.0);
                label.setText(String.valueOf(GPA));
                if (!anchor.getChildren().contains(label)) {
                    anchor.getChildren().add(label);
                }

                Text text = new Text("您目前的总学分为：");
                text.setLayoutX(250.0);
                text.setLayoutY(34.0);
                text.setFill(javafx.scene.paint.Color.valueOf("#e21e1e"));
                text.setFont(new Font("System", 13.0));
                anchor.getChildren().add(text);

                creditLabel.setFont(new Font("System Bold", 14.0));
                creditLabel.setText(String.valueOf(totalCredit));
                creditLabel.setLayoutX(365.0);
                creditLabel.setLayoutY(20.0);
                if (!anchor.getChildren().contains(creditLabel)) {
                    anchor.getChildren().add(creditLabel);
                }
            }
        }
        dataTableView.setItems(observableList);
    }

    /*//在每个“分数”后面加上编辑按钮
    public void editItem(String name) {
        if (name == null)
            return;
        int index = Integer.parseInt(name.substring(4, name.length()));
        Map data = scoreList.get(index);
        //showEditStage();
        //scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }*/

    @FXML
    private void onCancelClick() {
        studentEditComboBox.setDisable(false);
        courseEditComboBox.setDisable(false);
        studentEditComboBox.setValue("请选择学生");
        courseEditComboBox.setValue("请选择课程");
        markUpdateTextField.setText("");
    }

    @FXML
    private void onConfirmClick() {
        DataRequest dataRequest = new DataRequest();
        String student_name = null;
        String course_name = null;
        Object student = studentEditComboBox.getSelectionModel().getSelectedItem();
        Object course = courseEditComboBox.getSelectionModel().getSelectedItem();
        Result result = null;
        if (student != null)
            student_name = student.toString();

        if (course != null)
            course_name = course.toString();

        if (student_name == "请选择学生") {
            studentEditComboBox.setValue("请选择学生");
            student_name = null;
        }
        if (course_name == "请选择课程") {
            courseEditComboBox.setValue("请选择课程");
            course_name = null;
        }
        if (markUpdateTextField.getText() == "") {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入分数");
            alert.showAndWait();
            return;
        }
        if (student_name != null && course_name != null) {
            DataRequest stuDataRequest = new DataRequest();
            String student_num = student_name.split("-")[0];
            String course_num = course_name.split("-")[0];

            dataRequest.add("student_num", student_num);
            dataRequest.add("course_num", course_num);
            String markstr = markUpdateTextField.getText();
            Integer cnt = 0;
            if (markstr.length() > 1) {
                if (markstr.charAt(0) == '0' && markstr.charAt(1) != '.') {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            if (markstr.length() == 2) {
                if (markstr.charAt(0) == '0' && markstr.charAt(1) == '.') {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            if (!Character.isDigit(markstr.charAt(0))) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setResizable(false);
                alert.setContentText("请输入正确的分数");
                alert.showAndWait();
                return;
            }
            for (int i = 0; i < markstr.length(); i++) {
                if (Character.isDigit(markstr.charAt(i)) || markstr.charAt(i) == '.' && cnt <= 1) {
                    if (markstr.charAt(i) == '.') {
                        cnt++;
                    }
                    if (cnt >= 2) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setResizable(false);
                        alert.setContentText("请输入正确的分数");
                        alert.showAndWait();
                        return;
                    }
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            Double mark = Double.parseDouble(markstr);
            if (mark < 0 || markstr.equals("0.0") || mark > 100) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setResizable(false);
                alert.setContentText("请输入正确的分数");
                alert.showAndWait();
                return;
            }
            if (markstr.length() > 2 && mark != 100) {
                if (markstr.split("\\.")[1].length() >= 2) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            dataRequest.add("mark", markUpdateTextField.getText());
            if (editConfirmButton.getText() == "修改分数") {
                String msg = CommonMethod.alertButton("修改");
                if (msg == "确认") {
                    result = HttpRequestUtils.request("/score/updateScore", dataRequest);
                }
            } else if (editConfirmButton.getText() == "增加分数") {
                result = HttpRequestUtils.request("/score/selectByStudentAndCourse", dataRequest);
                System.out.println(result);
                if(result!=null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("成绩已存在,请重新输入");
                    alert.showAndWait();
                    return;
                }
                String msg = CommonMethod.alertButton("增加");
                if (msg == "确认") {
                    System.out.println(dataRequest.getData());
                    HttpRequestUtils.request("/score/insertScore", dataRequest);
                }
            }
            onQueryButtonClick();
        } else if (student_name == null && course_name != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入学生");
            alert.showAndWait();
        } else if (student_name != null && course_name == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入课程");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("请输入学生和课程");
            alert.showAndWait();
        }
    }


    @FXML
    public void initialize() {
        classComboBox.getItems().clear();
        studentComboBox.getItems().clear();
        courseComboBox.getItems().clear();
        studentEditComboBox.getItems().clear();
        courseEditComboBox.getItems().clear();
        editButton.setVisible(false);
        Result result = HttpRequestUtils.request("/student/getStudentList", new DataRequest());
        List<Map> studentList = (List<Map>) result.getData();
        result = HttpRequestUtils.request("/person/getPersonList", new DataRequest());
        List<Map> personList = (List<Map>) result.getData();
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_num"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        classColumn.setCellValueFactory(
                e -> {
                    for (Map student : studentList) {
                        if (student.get("student_name").equals(e.getValue().get("student_name"))) {
                            for (Map person : personList) {
                                if (person.get("person_num").equals(e.getValue().get("student_num"))) {
                                    return new SimpleStringProperty(student.get("classes").toString());
                                }
                            }
                        }
                    }
                    return new SimpleStringProperty("未知");
                }
        );
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("course_num"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("course_name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));
        gradePointColumn.setCellValueFactory(
                e -> {
                    Double mark = Double.parseDouble(e.getValue().get("mark").toString());
                    double gradePoint = mark < 60 ? 0 : mark / 10 - 5;
                    gradePoint = (double) Math.round(gradePoint * 100) / 100;
                    return new SimpleStringProperty(gradePoint + "");
                }
        );
        editConfirmButton.setText("添加分数");
        User user = AppStore.getUser();
        if (user.getUser_type_id() == 3) {

            viewTab.setText("我的分数");


            one.setVisible(false);
            studentComboBox.setVisible(false);
            id.setVisible(false);
            addButton.setVisible(false);
            deleteButton.setVisible(false);
            editButton.setVisible(false);
            classComboBox.setVisible(false);
            five.setVisible(false);

            String person_num = user.getPerson_num();
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("person_num", person_num);
            Result result1 = HttpRequestUtils.request("/person/selectByPersonNum", dataRequest);
            studentComboBox.setValue(user.getPerson_num() + "-" + ((Map) result1.getData()).get("person_name"));
            studentComboBox.setDisable(true);
        }

        DataRequest req = new DataRequest();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req); //从后台获取所有学生信息列表集合
        Result courseResult = HttpRequestUtils.request("/course/selectAll", req); //从后台获取所有学生信息列表集合


        List cstudentList = new ArrayList();
        List courseList = new ArrayList();
        List classList = new ArrayList();
        Map cancelStudent = new HashMap();
        cancelStudent.put("cancelStudent", "请选择学生");
        cstudentList.add(cancelStudent.get("cancelStudent"));
        Map cancelCourse = new HashMap();
        cancelCourse.put("cancelCourse", "请选择课程");
        courseList.add(cancelCourse.get("cancelCourse"));
        Map classMap = new HashMap();
        classMap.put("cancelClass", "请选择班级");
        classComboBox.getItems().add(classMap.get("cancelClass"));

        List<Map> studentMap = (List<Map>) studentResult.getData();
        List<Map> courseMap = (List<Map>) courseResult.getData();
        for (Integer i = 1; i <= 8; i++) {
            Map map = new HashMap();
            map.put("class", "23."+i);
            classList.add(map.get("class"));
        }
        for (Map student : studentMap) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("id", student.get("person_id"));
            Result result1 = HttpRequestUtils.request("/person/selectById", dataRequest);
            Map map = (Map) result1.getData();
            cstudentList.add(map.get("person_num") + "-" + student.get("student_name"));
        }
        for (Map course : courseMap) {
            courseList.add(course.get("num") + "-" + course.get("course_name"));
        }
        studentComboBox.getItems().addAll(cstudentList);
        courseComboBox.getItems().addAll(courseList);
        studentEditComboBox.getItems().addAll(cstudentList);
        courseEditComboBox.getItems().addAll(courseList);
        classComboBox.getItems().addAll(classList);
        classComboBox.setOnAction(e -> {
            if (classComboBox.getSelectionModel().getSelectedItem() != null) {
                String class_name = classComboBox.getSelectionModel().getSelectedItem().toString();
                if (class_name.equals("请选择班级")) {
                    return;
                }
                courseComboBox.setValue(null);
                courseComboBox.setPromptText("请选择课程");
                courseComboBox.setDisable(true);
                studentComboBox.setValue(null);
                studentComboBox.setPromptText("请选择学生");
                studentComboBox.setDisable(true);
            }
        });
        dataTableView.setOnMouseClicked(e -> {
            if (e.getClickCount() == 1 && e.getButton() == MouseButton.PRIMARY && editTabPane.isVisible()) {
                Map selected = dataTableView.getSelectionModel().getSelectedItem();
                if (selected != null) {
                    editTabPane.setVisible(true);
                    editAnchorPane.setVisible(true);
                    DataRequest dataRequest = new DataRequest();
                    String student_num = CommonMethod.getString(selected, "student_num");
                    String course_num = CommonMethod.getString(selected, "course_num");
                    String student_name = CommonMethod.getString(selected, "student_name");
                    String course_name = CommonMethod.getString(selected, "course_name");
                    String mark = CommonMethod.getString(selected, "mark");
                    studentEditComboBox.setValue(student_num + "-" + student_name);
                    courseEditComboBox.setValue(course_num + "-" + course_name);
                    studentEditComboBox.setDisable(true);
                    courseEditComboBox.setDisable(true);
                    markUpdateTextField.setText(mark);
                    editConfirmButton.setText("修改分数");
                }
            }
        });

        result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        scoreList=(List<Map>)result.getData();

        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onQueryButtonClick();
    }
}
