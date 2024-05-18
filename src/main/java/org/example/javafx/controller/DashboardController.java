package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.example.javafx.AppStore;
import org.example.javafx.MainApplication;
import org.example.javafx.PieChartUtils;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import org.example.javafx.util.CommonMethod;
import org.example.javafx.util.ElementsTool;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardController {
    @FXML
    BorderPane borderPane;

    @FXML
    AnchorPane anchorpane;

    @FXML TabPane chart;
    @FXML
    MenuBar menuBar;
//    @FXML
//    Button selectCourseSheet;
    @FXML
    ComboBox comboBoxTerm;
    @FXML
    ComboBox comboBoxWeek;

    @FXML
    VBox eventBox;
    @FXML
    GridPane gridPane;

    @FXML Label noticeLabel;

    @FXML Button newNoticeButton;

    @FXML Label adminNum;
    @FXML Label stuNum;
    @FXML Label teaNum;
    Boolean bl = false;
    static String classes;
    static String student_id;
    static String student_name;
    static String terms;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        borderPane.setOnMouseEntered(e -> setNotice());
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(MainApplication.class.getResource("dashboard-view.fxml"));
        if(AppStore.getUser().getUser_type_id() == 3){
            DataRequest dataRequestS = new DataRequest();
            Map<String, String> mapS = new HashMap<>();
            mapS.put("id", String.valueOf(AppStore.getUser().getPerson_id()));
            dataRequestS.setData(mapS);
            Result da = null;
            try {
                da = HttpRequestUtils.courseField("/course/selectClasses", dataRequestS);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            List<Map<String, ? extends Object>> dataListS = new Gson().fromJson(da.getData().toString(), List.class);
            classes = String.valueOf(dataListS.get(0).get("classes"));
            student_id = String.valueOf(dataListS.get(0).get("id"));
            student_name = String.valueOf(dataListS.get(0).get("student_name"));
            terms = "2023-2024-2";
            bl = true;
            addlabel(Integer.parseInt(getCurrentTime().get("week")), student_id, getCurrentTime().get("terms"));
            comboBoxTerm.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    select();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            comboBoxWeek.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    select();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            setEvent(eventBox);
            chart.setDisable(true);
            chart.setOpacity(0);
        } else if(AppStore.getUser().getUser_type_id() == 1 || AppStore.getUser().getUser_type_id() == 2){
            gridPane.setVisible(false);
            comboBoxTerm.setVisible(false);
            comboBoxWeek.setVisible(false);
            setEvent(eventBox);

            Result personList = HttpRequestUtils.request("/person/getAll",new DataRequest());
            List<Map> people = (List<Map>) personList.getData();
            Map total = new HashMap<>();
            stuNum.setText("学生数：" + CommonMethod.filter(people,"user_type","1").size());
            adminNum.setText("管理员数：" + CommonMethod.filter(people,"user_type","1").size());
            teaNum.setText("教师数：" + CommonMethod.filter(people,"user_type","1").size());
            total.put("admin", CommonMethod.filter(people,"user_type","1").size() * 1.0 /people.size());
            total.put("tea", CommonMethod.filter(people,"user_type","2").size() * 1.0 /people.size());
            total.put("stu", CommonMethod.filter(people,"user_type","3").size() * 1.0 /people.size());
            PieChart pieChart = (PieChart)anchorpane.lookup("#pieChart");
            PieChartUtils pieChartUtils = new PieChartUtils(pieChart);
            pieChartUtils.operatePieChart(total);
        }
    }

    //添加课程表上的课程
    private void addlabel(int week, String student_id, String terms) throws IOException, InterruptedException {
        Map<String, String> student = new HashMap();
        student.put("student_id", student_id);
        student.put("week", String.valueOf(week));
        student.put("terms", terms);
        DataRequest dataRequest = new DataRequest();
        dataRequest.setData(student);
        Result data = HttpRequestUtils.courseField("/course/selectLessonByStudent", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        for (Map<String, ? extends Object> a : dataList) {
            String course_name = (String) a.get("course_name");
            double week_time = (double) a.get("week_time");
            String room = (String) a.get("room");
            double time_sort = (double) a.get("time_sort");
            Label label = new Label();
            label.setId("1");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            gridPane.setHgrow(label, Priority.ALWAYS);
            gridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle("-fx-background-color: transparent; -fx-padding: 10;-fx-border-color: #86f3c0; -fx-border-radius: 1px; -fx-border-width: 3px");
            label.setText(course_name + "\n" + "\n" + room);
            label.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1) { // 检查是否是单击事件
                    detailCourse();
                }
            });
            gridPane.setHalignment(label, HPos.CENTER); // 设置水平对齐方式为居中
            gridPane.setValignment(label, VPos.CENTER); // 设置垂直对齐方式为居中
            gridPane.add(label, (int) week_time, (int) (time_sort));
        }


    }

    private Map<String, String> selectData() {
        Map<String, String> map = new HashMap<>();
        String a = (String) comboBoxTerm.getValue();
        if (a == null || (!(a.equals("2023-2024-1")) && !(a.equals("2023-2024-2")))) {
            return null;
        }
        String b = (String) comboBoxWeek.getValue();
        if (b == null || (!(b.equals("第1周"))&&!(b.equals("第2周"))&&!(b.equals("第3周"))&&!(b.equals("第4周"))&&!(b.equals("第5周"))&&!(b.equals("第6周"))&&!(b.equals("第7周"))&&!(b.equals("第8周"))&&!(b.equals("第9周"))&&!(b.equals("第10周"))&&!(b.equals("第11周"))&&!(b.equals("第12周"))&&!(b.equals("第13周"))&&!(b.equals("第14周"))&&!(b.equals("第15周"))&&!(b.equals("第16周"))&&!(b.equals("第17周"))&&!(b.equals("第18周"))&&!(b.equals("第19周")) )) {
            return null;
        }
        map.put("terms", a);
        switch (b.length()) {
            case 4:
                map.put("week", b.substring(1, 3));
                break;
            case 3:
                map.put("week", b.substring(1, 2));
                break;
        }
        return map;
    }

    public void select() throws IOException, InterruptedException {
        if (selectData() == null) {
            return;
        } else {
            //防止异常
            List<Node> toRemove = new ArrayList<>();
            gridPane.getChildren().forEach(node -> {
                if (node.getId() == "1" && node instanceof Label) {
                    toRemove.add(node);
                }
            });
            for (Node a : toRemove) {
                gridPane.getChildren().remove(a);
            }


            int week = Integer.parseInt(selectData().get("week"));
            String terms = selectData().get("terms");
            if(bl){
                addlabel(week, String.valueOf(student_id), terms);
            }else {
                addlabel(week, "1", terms);
            }

        }
    }

    private void detailCourse() {
    }


    private Map<String, String> getCurrentTime() {
        Map<String, String> map = new HashMap<>();
        // 获取当前时间
        LocalDateTime currentTime = LocalDateTime.now();
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedTime = currentTime.format(formatter);
        String time = formattedTime.substring(0, 10);
        if (time.compareTo("2023-09-05") >= 0 && time.compareTo("2024-02-26") < 0) {
            map.put("terms", "2023-2024-1");
            map.put("week","19");
            comboBoxTerm.setValue("2023-2024-1");
        } else if (time.compareTo("2024-02-26") >= 0 && time.compareTo("2024-09-05") < 0) {
            map.put("terms", "2023-2024-2");
            comboBoxTerm.setValue("2023-2024-2");
            if (time.compareTo("2024-03-04") < 0) {
                map.put("week", "1");
                comboBoxWeek.setValue("第一周");
            } else if (time.compareTo("2024-03-11") < 0) {
                map.put("week", "2");
                comboBoxWeek.setValue("第2周");
            } else if (time.compareTo("2024-03-18") < 0) {
                map.put("week", "3");
                comboBoxWeek.setValue("第3周");
            } else if (time.compareTo("2024-03-25") < 0) {
                map.put("week", "4");
                comboBoxWeek.setValue("第4周");
            } else if (time.compareTo("2024-04-01") < 0) {
                map.put("week", "5");
                comboBoxWeek.setValue("第5周");
            } else if (time.compareTo("2024-04-08") < 0) {
                map.put("week", "6");
                comboBoxWeek.setValue("第6周");
            } else if (time.compareTo("2024-04-15") < 0) {
                map.put("week", "7");
                comboBoxWeek.setValue("第7周");
            } else if (time.compareTo("2024-04-22") < 0) {
                map.put("week", "8");
                comboBoxWeek.setValue("第8周");
            } else if (time.compareTo("2024-04-29") < 0) {
                map.put("week", "9");
                comboBoxWeek.setValue("第9周");
            } else if (time.compareTo("2024-05-06") < 0) {
                map.put("week", "10");
                comboBoxWeek.setValue("第10周");
            } else if (time.compareTo("2024-05-13") < 0) {
                map.put("week", "11");
                comboBoxWeek.setValue("第11周");
            } else if (time.compareTo("2024-05-20") < 0) {
                map.put("week", "12");
                comboBoxWeek.setValue("第12周");
            } else if (time.compareTo("2024-05-27") < 0) {
                map.put("week", "13");
                comboBoxWeek.setValue("第13周");
            } else if (time.compareTo("2024-06-03") < 0) {
                map.put("week", "14");
                comboBoxWeek.setValue("第14周");
            } else if (time.compareTo("2024-06-10") < 0) {
                map.put("week", "15");
                comboBoxWeek.setValue("第15周");
            } else if (time.compareTo("2024-06-17") < 0) {
                map.put("week", "16");
                comboBoxWeek.setValue("第16周");
            } else if (time.compareTo("2024-06-24") < 0) {
                map.put("week", "17");
                comboBoxWeek.setValue("第17周");
            } else if (time.compareTo("2024-07-01") < 0) {
                map.put("week", "18");
                comboBoxWeek.setValue("第18周");
            } else if (time.compareTo("2024-07-08") < 0) {
                map.put("week", "19");
                comboBoxWeek.setValue("第19周");
            } else {
                map.put("week", "20");
                comboBoxWeek.setValue("第20周");
            }
        }
        return map;
    }

    private void setEvent(VBox vBox) throws IOException {
        ElementsTool tool = new ElementsTool();
        Result result = new Result();
        result = HttpRequestUtils.request("/leave/getLeaveList", new DataRequest());
        if (AppStore.getUser().getUser_type_id() != 3){
            List<Map> leaveList = CommonMethod.filter((List<Map>) result.getData(),"status","[未处理]*");
            if (leaveList.size() != 0){
                Button leaveButton = new Button("有待审核请假");
                tool.setEventButton1(leaveButton);
                leaveButton.setOnAction(e -> {});
                vBox.getChildren().add(leaveButton);
            }
        }else {
            List<Map> leaveList0 = (List<Map>) result.getData();
            List<Map> leaveList = CommonMethod.filter(leaveList0,"student_name",student_name);
            for (int i = 0; i < leaveList.size() ;i++) {
                String str = null;
                if (leaveList.get(i).get("status").equals("[未处理]*")){
                    str = "待审核请假 " + leaveList.get(i).get("leave_reason") + leaveList.get(i).get("start_time").toString().substring(5);
                    Button leaveButton = new Button(str);
                    tool.setEventButton1(leaveButton);
                    eventBox.getChildren().add(0,leaveButton);
                }
                else if (leaveList.get(i).get("status").equals("不通过")){
                    str = "未通过审核 " + leaveList.get(i).get("leave_reason") + leaveList.get(i).get("start_time").toString().substring(5);
                    Button leaveButton = new Button(str);
                    tool.setEventButton3(leaveButton);
                    eventBox.getChildren().add(0,leaveButton);
                }
                else {
                    str = "已通过审核请假 " + leaveList.get(i).get("leave_reason");
                    Button leaveButton = new Button(str);
                    tool.setEventButton2(leaveButton);
                    eventBox.getChildren().add(leaveButton);
                }

            }
        }
        if (vBox.getChildren().isEmpty()){
            vBox.getChildren().add(new Label("空"));
        }
    }

    private void setNotice(){
        Result result =  HttpRequestUtils.request("/menu/getNotice", new DataRequest());
        System.out.println(result.getData());
        List<Map> notice = (List<Map>)result.getData();
        noticeLabel.setText(notice.get(0).get("text").toString());
        noticeLabel.setTextFill(Color.web(notice.get(0).get("color").toString()));
        if (AppStore.getUser().getUser_type_id() != 1){
            newNoticeButton.setDisable(true);
            newNoticeButton.setOpacity(0);
        }
        newNoticeButton.setOnAction(e -> {
            try {
                FXMLLoader messageFxml = new FXMLLoader();
                messageFxml.setLocation(MainApplication.class.getResource("newNotice.fxml"));
                Parent root = messageFxml.load();
                Stage newStage = new Stage();
                newStage.initStyle(StageStyle.DECORATED);
                newStage.setTitle("发布公告");
                newStage.setScene(new Scene(root));
                newStage.initModality(Modality.APPLICATION_MODAL);
                newStage.show();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }




}
