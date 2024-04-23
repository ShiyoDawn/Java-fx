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

public class ScoreTableController  {
    //显示元素

    @FXML
    private TableView<Map> dataTableView ;

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
    private Button editComfirmButton;

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
        Stage editStage = new Stage();
        //取消放大（全屏）按钮
        editTabPane.setVisible(true);
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        editComfirmButton.setText("增加分数");
        onQueryButtonClick();
        //onResetButtonClick();
    }

    @FXML
    private void onDeleteButtonClick(ActionEvent event) {
        onCancelClick();
        Map selected=dataTableView.getSelectionModel().getSelectedItem();
        if(selected==null){
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
        Integer student_id=CommonMethod.getInteger(selected,"student_id");
        Integer course_id=CommonMethod.getInteger(selected,"course_id");
        System.out.println(student_id+" "+course_id);
        DataRequest dataRequest=new DataRequest();
        dataRequest.add("student_id",student_id);
        dataRequest.add("course_id",course_id);
        String msg=CommonMethod.alertButton("/score/deleteAllById",dataRequest,"删除");
        onQueryButtonClick();
        onQueryButtonClick();
    }

    @FXML
    private void onEditButtonClick(ActionEvent event) {
        onCancelClick();
        Map selected=dataTableView.getSelectionModel().getSelectedItem();
        editComfirmButton.setText("修改分数");
        editTabPane.setVisible(true);
        editTextArea.setVisible(true);
        editTextArea.setEditable(false);
        editTextArea.setDisable(true);
        if(selected!=null){
            DataRequest dataRequest=new DataRequest();
            Integer student_id=CommonMethod.getInteger(selected,"student_id");
            Integer course_id=CommonMethod.getInteger(selected,"course_id");
            dataRequest.add("student_id",student_id);
            dataRequest.add("course_id",course_id);
            Result result=new Result();
            result=HttpRequestUtils.request("/score/selectByStudentAndCourse",dataRequest);
            Map map=(Map) result.getData();
            studentEditComboBox.setValue(map.get("student_name").toString());
            courseEditComboBox.setValue(map.get("course_name").toString());
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

        if(student_name == "请选择学生"){
            studentComboBox.setValue("请选择学生");
            student_name = null;
        }
        if(course_name == "请选择课程"){
            courseComboBox.setValue("请选择课程");
            course_name = null;
        }

        System.out.println(student_name+" "+course_name);
        if (student_name != null && course_name != null)
        {
            DataRequest stuDataRequest = new DataRequest();
            stuDataRequest.add("student_name", student_name);
            result = HttpRequestUtils.request("/student/selectStudentByName", stuDataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
            Map map = (Map) result.getData();
            Integer student_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("course_name", course_name);
            result = HttpRequestUtils.request("/course/selectCourseByName", courDataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
            map = (Map) result.getData();
            Integer course_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

            DataRequest dataRequest = new DataRequest();

            dataRequest.add("student_id", student_id);
            dataRequest.add("course_id", course_id);
            result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
            result = HttpRequestUtils.request("/score/selectByStudentAndCourse", dataRequest);
            map=(Map) result.getData();
            System.out.println(map.get("student_id")+" "+map.get("course_id")+" "+map.get("mark"));
            if(result==null){
                observableList.clear();
                return;
            }

        } else if (student_name != null && course_name == null)
        {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("student_name",student_name);
            result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
            result = HttpRequestUtils.request("/score/selectByStudentName",dataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
        }
        else if(student_name == null && course_name != null)
        {
            DataRequest dataRequest = new DataRequest();
            dataRequest.add("course_name",course_name);
            result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
            result = HttpRequestUtils.request("/score/selectByCourseName",dataRequest);
            if(result==null){
                observableList.clear();
                return;
            }
        }
        else if(student_name == null && course_name == null)
        {
            result = HttpRequestUtils.request("/score/getScoreList", new DataRequest());
            result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        }
        if(result==null){
            return;
        }
        setTableViewData(result);
    }

    @FXML
    public void onResetButtonClick(){
        Result result=null;
        studentComboBox.setValue("请选择学生");
        courseComboBox.setValue("请选择课程");
        result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        result=HttpRequestUtils.request("/score/getScoreList",new DataRequest());
        setTableViewData(result);
    }

    //显示成绩总表
    public void setTableViewData(Result result) {
        observableList.clear();
        int index=1;
        if (result.getData() instanceof Map) {
            Map scoreMap = (Map) result.getData();
            System.out.println(scoreMap);
            //scoreMap.put("student_id",(Integer))
            Button editButton;
            editButton = new Button("编辑");
            editButton.setId("edit"+index);
            editButton.setOnAction(e -> {
                editItem(((Button) e.getSource()).getId());
            });
            index++;
            scoreMap.put("operateColumn", editButton);
            observableList.add(scoreMap);
        } else if (result.getData() instanceof ArrayList) {
            Button editButton;
            scoreList = (ArrayList) result.getData();
            for (Map scoreMap : (ArrayList<Map>) scoreList) {
                System.out.println(scoreMap);
                editButton = new Button("编辑");
                editButton.setId("edit"+index);
                editButton.setOnAction(e -> {
                    editItem(((Button) e.getSource()).getId());
                });
                scoreMap.put("operateColumn", editButton);
                observableList.add(scoreMap);
                index++;
            }
        }
        dataTableView.setItems(observableList);
    }

    //在每个“分数”后面加上编辑按钮
    public void editItem(String name) {
        if (name == null)
            return;
        int index = Integer.parseInt(name.substring(4, name.length()));
        Map data = scoreList.get(index);
        //showEditStage();
        scoreEditController.showDialog(data);
        MainApplication.setCanClose(false);
        stage.showAndWait();
    }

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
        if (markUpdateTextField.getText() == null) {
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
            Integer student_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

            DataRequest courDataRequest = new DataRequest();
            courDataRequest.add("course_name", course_name);
            result = HttpRequestUtils.request("/course/selectCourseByName", courDataRequest);
            map = (Map) result.getData();
            Integer course_id = Integer.parseInt(map.get("id").toString().substring(0,map.get("id").toString().length()-2));

            dataRequest.add("student_id", student_id);
            dataRequest.add("course_id", course_id);
            String markstr=markUpdateTextField.getText();
            Integer cnt=0;
            if(!Character.isDigit(markstr.charAt(0))){
                Alert alert=new Alert(Alert.AlertType.INFORMATION);
                alert.setResizable(false);
                alert.setContentText("请输入正确的分数");
                alert.showAndWait();
                return;
            }
            for(int i=0;i<markstr.length();i++){
                if(Character.isDigit(markstr.charAt(i))||markstr.charAt(i)=='.'&&cnt<=2){
                    if(markstr.charAt(i)=='.'){
                        cnt++;
                    }
                }else{
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            Double mark=Double.parseDouble(markstr);
            if(mark<0||markstr=="0.0"||mark>100||markstr.length()>2){
                boolean ok=true;
                if(mark.toString().split(".")[1].length()>=2){
                    ok=false;
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
                if(ok){
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setResizable(false);
                    alert.setContentText("请输入正确的分数");
                    alert.showAndWait();
                    return;
                }
            }
            dataRequest.add("mark", markUpdateTextField.getText());
            if(editComfirmButton.getText()=="修改分数"){
                String msg=CommonMethod.alertButton("/score/updateScore",dataRequest,"修改");
            }else if(editComfirmButton.getText()=="增加分数"){
                String msg=CommonMethod.alertButton("/score/insertScore",dataRequest,"增加");
                System.out.println(msg);
                if(msg!=null){
                    if(msg.equals("Score has existed.")){
                        Alert alert=new Alert(Alert.AlertType.INFORMATION);
                        alert.setResizable(false);
                        alert.setContentText("成绩已存在,请重新输入");
                        alert.showAndWait();
                        return;
                    }
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
        studentNumColumn.setCellValueFactory(new MapValueFactory("student_id"));  //设置列值工程属性
        studentNameColumn.setCellValueFactory(new MapValueFactory<>("student_name"));
        courseNumColumn.setCellValueFactory(new MapValueFactory<>("course_id"));
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

        Map cancelStudent=new HashMap();
        cancelStudent.put("cancelStudent","请选择学生");
        studentList.add(cancelStudent.get("cancelStudent"));
        Map cancelCourse=new HashMap();
        cancelCourse.put("cancelCourse","请选择课程");
        courseList.add(cancelCourse.get("cancelCourse"));

        List<Map> studentMap = (List<Map>) studentResult.getData();
        List<Map> courseMap = (List<Map>) courseResult.getData();
        for (Map student : studentMap) {
            studentList.add(student.get("student_name"));
        }
        for (Map course : courseMap) {
            courseList.add(course.get("course_name"));
        }
        studentComboBox.getItems().addAll(studentList);
        courseComboBox.getItems().addAll(courseList);
        studentEditComboBox.getItems().addAll(studentList);
        courseEditComboBox.getItems().addAll(courseList);
        dataTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        onResetButtonClick();
    }
}
