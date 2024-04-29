package org.example.javafx.controller;

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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Course;
import org.example.javafx.pojo.Result;
import org.example.javafx.pojo.Student;
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
    private TableColumn<Map, Button> operateColumn;

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
    private TextArea editTextArea;

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
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        editConfirmButton.setText("增加分数");
        onQueryButtonClick();
        //onResetButtonClick();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        onCancelClick();
        List<Map> selected = dataTableView.getSelectionModel().getSelectedItems();
        System.out.println(selected);
        if (selected.size() == 0) {
            Stage editStage = new Stage();
            //取消放大（全屏）按钮
            editStage.setResizable(false);
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                URL url = getClass().getResource("/org/example/javafx/score-edit-delete.fxml");
                fxmlLoader.setLocation(url);
                Parent parent = fxmlLoader.load();
                editStage.setScene(new Scene(parent));
                editStage.setTitle("删除学生分数");
                editStage.showAndWait();
                onQueryButtonClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }
        //System.out.println(selected);
        //onResetButtonClick();
        String msg = CommonMethod.alertButton("删除");
        System.out.println(msg);
        if (msg == "确认") {
            for (Map scoreMap : selected) {
                String student_num = CommonMethod.getString(scoreMap, "student_num");
                String course_num = CommonMethod.getString(scoreMap, "course_num");
                System.out.println(student_num + " " + course_num);
                DataRequest dataRequest = new DataRequest();
                dataRequest.add("student_num", student_num);
                dataRequest.add("course_num", course_num);
                HttpRequestUtils.request("/score/deleteAllById", dataRequest);
            }
        }
        onQueryButtonClick();
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        onCancelClick();
        Map selected = dataTableView.getSelectionModel().getSelectedItem();
        editConfirmButton.setText("修改分数");
        editTabPane.setVisible(true);
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        if (selected != null) {
            DataRequest dataRequest = new DataRequest();
            String student_num = CommonMethod.getString(selected, "student_num");
            String course_num = CommonMethod.getString(selected, "course_num");
            dataRequest.add("student_num", student_num);
            dataRequest.add("course_num", course_num);
            Result result = new Result();
            result = HttpRequestUtils.request("/score/selectByStudentAndCourse", dataRequest);
            Map map = (Map) result.getData();
            System.out.println(map);
            studentEditComboBox.setValue(map.get("student_name"));
            courseEditComboBox.setValue(map.get("course_num") + "-" + map.get("course_name"));
            studentEditComboBox.setEditable(false);
            courseEditComboBox.setEditable(false);
            studentEditComboBox.setDisable(true);
            courseEditComboBox.setDisable(true);
            markUpdateTextField.setText(map.get("mark").toString());
        }
    }

    @FXML
    private void onQueryButtonClick() {
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
            studentComboBox.setValue("请选择学生");
            student_name = null;
        }
        if (course_name == "请选择课程") {
            courseComboBox.setValue("请选择课程");
            course_name = null;
        }

        System.out.println(student_name + " " + course_name);
        if (student_name != null && course_name != null) {
            DataRequest stuDataRequest = new DataRequest();
            stuDataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            if (result == null) {
                observableList.clear();
                return;
            }
            Map map = (Map) result.getData();
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
            map = (Map) result.getData();
            System.out.println(map.get("student_num") + " " + map.get("course_num") + " " + map.get("mark"));
            if (result == null) {
                observableList.clear();
                return;
            }

        } else if (student_name != null && course_name == null) {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            result = HttpRequestUtils.request("/score/selectByStudentName", dataRequest);
            if (result == null) {
                observableList.clear();
                return;
            }
        } else if (student_name == null && course_name != null) {
            DataRequest dataRequest = new DataRequest();
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
            }
        } else if (student_name == null && course_name == null) {
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        }
        if (result == null) {
            return;
        }
        setTableViewData(result);
    }

    @FXML
    public void onResetButtonClick() {
        Result result = null;
        studentComboBox.setValue("请选择学生");
        courseComboBox.setValue("请选择课程");
        result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
        setTableViewData(result);
    }

    //显示成绩总表
    public void setTableViewData(Result result) {
        observableList.clear();
        //int index=1;
        if (result.getData() instanceof Map) {
            Map scoreMap = (Map) result.getData();
            System.out.println(scoreMap);
            //scoreMap.put("student_id",(Integer))
            /*Button editButton;
            editButton = new Button("编辑");
            editButton.setId("edit"+index);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            index++;
            scoreMap.put("operateColumn", editButton);*/
            observableList.add(scoreMap);
        } else if (result.getData() instanceof ArrayList) {
            //Button editButton;
            scoreList = (ArrayList) result.getData();
            for (Map scoreMap : (ArrayList<Map>) scoreList) {
                System.out.println(scoreMap);
                /*editButton = new Button("编辑");
                editButton.setId("edit"+index);
                editButton.setOnAction(e -> {
                    editItem(((Button) e.getSource()).getId());
                });
                scoreMap.put("operateColumn", editButton);*/
                observableList.add(scoreMap);
                //index++;
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
        studentEditComboBox.setValue("请选择学生");
        courseEditComboBox.setValue("请选择课程");
        markUpdateTextField.setText("");
        studentEditComboBox.setDisable(false);
        courseEditComboBox.setDisable(false);
    }

    @FXML
    private void onConfirmClick(ActionEvent event) {
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
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入分数");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
            return;
        }
        if (student_name != null && course_name != null) {
            DataRequest stuDataRequest = new DataRequest();
            stuDataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            Map map = (Map) result.getData();
            DataRequest dataRequest1=new DataRequest();
            dataRequest1.add("id",map.get("person_id"));
            result=HttpRequestUtils.request("/person/selectById",dataRequest1);
            Map map1=(Map) result.getData();
            System.out.println(map1);
            String student_num = map1.get("person_num").toString();

            DataRequest courDataRequest = new DataRequest();
            System.out.println(course_name);
            courDataRequest.add("id", Integer.parseInt(course_name.split("-")[0]));
            result = HttpRequestUtils.request("/course/selectInfo", courDataRequest);
            map = (Map) result.getData();
            System.out.println(map);
            String course_num = map.get("num").toString();

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
                if(msg=="确认"){
                    result=HttpRequestUtils.request("/score/updateScore",dataRequest);
                }
            } else if (editConfirmButton.getText() == "增加分数") {
                String msg = CommonMethod.alertButton("增加");
                result=HttpRequestUtils.request("/score/selectByStudentAndCourse",dataRequest);
                if(result!=null){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("成绩已存在,请重新输入");
                    alert.showAndWait();
                    return;
                }
                if(msg=="确认"){
                    HttpRequestUtils.request("/score/insertScore",dataRequest);
                }
            }
            onQueryButtonClick();
        } else if (student_name == null && course_name != null) {
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
        } else if (student_name != null && course_name == null) {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入课程");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        } else {
            Stage confirmStage = new Stage();
            confirmStage.setWidth(250);
            confirmStage.setHeight(150);
            //取消放大（全屏）按钮
            confirmStage.setResizable(false);
            Text text = new Text("请输入学生和课程");
            HBox hBox = new HBox(text);
            hBox.setAlignment(Pos.CENTER);
            Scene scene = new Scene(hBox);
            confirmStage.setScene(scene);
            confirmStage.show();
        }
    }


    @FXML
    public void initialize() {
        System.out.println("check");
        id.setCellValueFactory(new MapValueFactory<>("id"));
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_num"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("course_num"));
        courseNameColumn.setCellValueFactory(new MapValueFactory<>("course_name"));
        creditColumn.setCellValueFactory(new MapValueFactory<>("credit"));
        markColumn.setCellValueFactory(new MapValueFactory<>("mark"));

        editTabPane.setVisible(false);
        editTextArea.setVisible(false);


        DataRequest req = new DataRequest();
        List studentList = new ArrayList();
        List courseList = new ArrayList();
        Result studentResult = HttpRequestUtils.request("/student/getStudentList", req); //从后台获取所有学生信息列表集合
        Result courseResult = HttpRequestUtils.request("/course/selectAll", req); //从后台获取所有学生信息列表集合

        Map cancelStudent = new HashMap();
        cancelStudent.put("cancelStudent", "请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        Map cancelCourse = new HashMap();
        cancelCourse.put("cancelCourse", "请选择课程");
        courseList.add(cancelCourse.get("cancelCourse"));

        List<Map> studentMap = (List<Map>) studentResult.getData();
        List<Map> courseMap = (List<Map>) courseResult.getData();
        for (Map student : studentMap) {
            studentList.add(student.get("student_name"));
        }
        for (Map course : courseMap) {
            courseList.add(course.get("id").toString().split("\\.")[0] + "-" + course.get("course_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
        studentEditComboBox.getItems().addAll(studentList);
        courseEditComboBox.getItems().addAll(courseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onResetButtonClick();
    }
}
