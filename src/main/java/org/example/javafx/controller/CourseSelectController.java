package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import org.example.javafx.AppStore;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.IOException;
import java.util.*;


public class CourseSelectController {
    @FXML
    Button major;
    @FXML
    Button option;
    @FXML
    Button limit;
    @FXML
    Pagination pagination;
    @FXML
    Button all;
    @FXML
    GridPane gridPane;
    @FXML
    AnchorPane pane;
    @FXML
    CheckBox noconflict;
    @FXML
    CheckBox nofull;
    static double credits = 0.0;
    List<String[]> listCou = new ArrayList<>();
    Boolean bl = true;

    @FXML
    public void initialize() throws IOException, InterruptedException {
        load();
        //根据班级显示课程
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            Map<String, String> map = new HashMap<>();
            map.put("classes", DashboardController.classes);
            map.put("classe", "临班");
            map.put("terms", "2023-2024-2");
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectSpecial"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        DataRequest dataRequest = new DataRequest();
        Map<String,String> map = new HashMap<>();
        map.put("student_id",DashboardController.student_id);
        map.put("terms",DashboardController.terms);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectLessonStudent", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        addLabelGrid(dataList);
    }

    private void handlePageClick(Map map, int pageNum, String url) throws IOException, InterruptedException {
        deleteNode();
        DataRequest dataRequest = new DataRequest();
        map.put("pageNum", pageNum);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField(url, dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        addLabelPane(dataList);
    }

    private void deleteNode() {
        List<javafx.scene.Node> toRemove = new ArrayList<>();
        pane.getChildren().forEach(node -> {
            if (node.getId() == "course" && (node instanceof Label || node instanceof Button)) {
                toRemove.add(node);
            }
        });
        for (Node b : toRemove) {
            pane.getChildren().remove(b);
        }
    }

    public void load() throws IOException, InterruptedException {
        DataRequest dataRequest1 = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("classes", String.valueOf(DashboardController.classes));
        map.put("classe", "临班");
        map.put("terms", "2023-2024-2");
        dataRequest1.setData(map);
        Result data1 = HttpRequestUtils.courseField("/course/selectSpecial", dataRequest1);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data1.getData().toString(), List.class);
        addLabelPane(dataList);
        setPagination(dataList);
    }

    public void majorC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_type_id", "1");
        map.put("course_typ_id", "2");
        map.put("course_ty_id", "7");
        map.put("classes", DashboardController.classes);
        map.put("classe", "临班");
        map.put("terms", "2023-2024-2");
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectCourseByType", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        deleteNode();
        addLabelPane(dataList);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectCourseByType"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        setPagination(dataList);
    }

    public void optionC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_type_id", "3");
        map.put("course_typ_id", "4");
        map.put("course_ty_id", "5");
        map.put("classes", DashboardController.classes);
        map.put("classe", "临班");
        map.put("terms", "2023-2024-2");
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectCourseByType", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        deleteNode();
        addLabelPane(dataList);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectCourseByType"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        setPagination(dataList);
    }

    public void limitC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        Map<String, String> map = new HashMap<>();
        map.put("course_type_id", "6");
        map.put("course_typ_id", "0");
        map.put("course_ty_id", "0");
        map.put("classes", DashboardController.classes);
        map.put("classe", "临班");
        map.put("terms", "2023-2024-2");
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectCourseByType", dataRequest);
        List<Map<String, ? extends Object>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        deleteNode();
        addLabelPane(dataList);
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectCourseByType"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        setPagination(dataList);
    }

    public void allC() throws IOException, InterruptedException {
        deleteNode();
        load();
        pagination.currentPageIndexProperty().addListener((observable, oldValue, newValue) -> {
            int clickedPageIndex = newValue.intValue();
            Map<String, String> map = new HashMap<>();
            map.put("classes", DashboardController.classes);
            map.put("classe", "临班");
            map.put("terms", "2023-2024-2");
            try {
                handlePageClick(map, clickedPageIndex, "/course/selectSpecial"); // 处理点击事件
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void setPagination(List<Map<String, ? extends Object>> list) {
        if (list.size() == 0) {
            pagination.setPageCount(1);
        } else {
            pagination.setPageCount((list.size() % 6 == 0 ? (list.size() / 6) : ((list.size() / 6) + 1)));
        }
    }

    private void addLabelPane(List<Map<String, ? extends Object>> list) {
        int count = 0;
        for (Map<String, ? extends Object> a : list) {
            count++;
            Label label = new Label();
            label.setMaxSize(350, 30);
            label.setId("course");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(new Font(12));
            label.setText("      " + a.get("course_name")  + "  " + a.get("teacher_name") + "    "+ idC(String.valueOf(a.get("students")))+"/"+idC(String.valueOf(a.get("capacity"))));
            label.setStyle("-fx-text-overrun: ellipsis; -fx-ellipsis-string: '...'");
            if (count <= 3) {
                label.setLayoutX(36);
                label.setLayoutY(447 + 64 * (count - 1));
            } else {
                label.setLayoutX(508);
                label.setLayoutY(447 + 64 * (count - 4));
            }
            pane.getChildren().add(label);
            Button button = new Button("选中");
            button.setId("course");
            button.setPrefHeight(7);
            button.setPrefWidth(55);
            button.setOnAction(event -> {
                DataRequest dataRequest1 = new DataRequest();
                Map<String, String> map1 = new HashMap<>();
                map1.put("student_id", String.valueOf(DashboardController.student_id));
                map1.put("student_name", String.valueOf(DashboardController.student_name));
                map1.put("course_id", String.valueOf(a.get("id")));
                map1.put("pre_course_id", String.valueOf(a.get("pre_course_id")));
                DataRequest dataRequestM = new DataRequest();
                dataRequestM.setData(map1);
                Result dataM = null;
                DataRequest dataRequestP = new DataRequest();
                dataRequestP.setData(map1);
                Result dataP = null;
                try {
                    dataM = HttpRequestUtils.courseField("/course/selectStudentAndCourse", dataRequestM);
                    dataP = HttpRequestUtils.courseField("/course/selectPre", dataRequestP);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                List<Map<String, String>> dataListM = new Gson().fromJson(dataM.getData().toString(), List.class);
                List<Map<String, String>> dataListP = new Gson().fromJson(dataP.getData().toString(), List.class);
                if (dataListM.size() != 0) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("已经选择该课程");
                    alert.showAndWait();
                } else if (dataListP.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    DataRequest dataRequest = new DataRequest();
                    Map<String,String> map = new HashMap<>();
                    map.put("id",String.valueOf(a.get("pre_course_id")));
                    dataRequest.setData(map);
                    try {
                        Result data = HttpRequestUtils.courseField("/course/selectInfo",dataRequest);
                        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
                        alert.setHeaderText("请先选择前序课程：" + dataList.get(0).get("course_name"));
                        alert.showAndWait();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    bl = true;
                    DataRequest dataRequestC = new DataRequest();
                    Map<String,String> mapC = new HashMap<>();
                    mapC.put("course_id",String.valueOf(a.get("id")));
                    dataRequestC.setData(mapC);
                    try {
                        Result dataC = HttpRequestUtils.courseField("/lesson/selectByCourseId", dataRequestC);
                        List<Map<String, ? extends Object>> dataListC = new Gson().fromJson(dataC.getData().toString(), List.class);
                        for (Map<String, ? extends Object> c : dataListC) {
                            double week = Double.valueOf(String.valueOf(c.get("week")));
                            double week_time = Double.valueOf(String.valueOf(c.get("week_time")));
                            double time_sort = Double.valueOf(String.valueOf( c.get("time_sort")));
                            for (String[] strings:listCou) {
                                if(String.valueOf(week).equals(strings[0]) && String.valueOf(week_time).equals(strings[1]) && String.valueOf(time_sort).equals(strings[2])){
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setHeaderText("与 "+strings[3]+" 课程"+"时间冲突不能选课");
                                    alert.showAndWait();
                                    bl = false;
                                    break;
                                }
                            }
                            if(!bl){
                                break;
                            }
                        }
                        if(bl){
                            dataRequest1.setData(map1);
                            Result data1 = null;
                            try {
                                data1 = HttpRequestUtils.courseField("/course/addStudent", dataRequest1);
                                if (data1.getCode() == 200) {
                                    DataRequest dataRequestG = new DataRequest();
                                    Map<String, String> mapG = new HashMap<>();
                                    mapG.put("course_id", String.valueOf(a.get("id")));
                                    dataRequestG.setData(mapG);
                                    Result dataG = HttpRequestUtils.courseField("/lesson/selectByCourseId", dataRequestG);
                                    List<Map<String, ? extends Object>> dataListG = new Gson().fromJson(dataG.getData().toString(), List.class);
                                    addLabelGrid(dataListG);
                                    DataRequest dataRequest = new DataRequest();
                                    Map<String,String> map = new HashMap<>();
                                    map.put("course_id", String.valueOf(a.get("id")));
                                    dataRequest.setData(map);
                                    Result data = HttpRequestUtils.courseField("/course/addCourseStudent", dataRequest);
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText("选课成功");
                                    alert.showAndWait();

                                } else {
                                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                    alert.setContentText("选课失败");
                                    alert.showAndWait();
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }


                }
            });
            if (count <= 3) {
                button.setLayoutX(406);
                button.setLayoutY(447 + 64 * (count - 1));
            } else {
                button.setLayoutX(878);
                button.setLayoutY(447 + 64 * (count - 4));
            }
            pane.getChildren().add(button);
            if (count % 6 == 0) {
                count = 0;
                break;
            }
        }

    }

    private void addLabelGrid(List<Map<String, ? extends Object>> list) {
        for (Map<String, ? extends Object> a : list) {
            String course_name = (String) a.get("course_name");
            double week = Double.valueOf(String.valueOf(a.get("week")));
            double week_time = Double.valueOf(String.valueOf(a.get("week_time")));
            String room = (String) a.get("room");
            double time_sort = Double.valueOf(String.valueOf( a.get("time_sort")));
            String[] d = new String[4];
            d[0] = String.valueOf(week);
            d[1] = String.valueOf(week_time);
            d[2] = String.valueOf(time_sort);
            d[3] = String.valueOf(course_name);
            listCou.add(d);
            Label label = new Label();
            label.setId("1");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            label.setFont(Font.font(10.0));
            gridPane.setHgrow(label, Priority.ALWAYS);
            gridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle("-fx-background-color: #00ff33; -fx-padding: 10;");
            label.setText(course_name + "\n" + room);
            gridPane.setHalignment(label, HPos.CENTER); // 设置水平对齐方式为居中
            gridPane.setValignment(label, VPos.CENTER); // 设置垂直对齐方式为居中
            gridPane.add(label, (int) week_time, (int) (time_sort));
        }
    }
    private String idC(String str){
        int count = str.length();
        for (int i = 0; i < str.length(); i++) {
            if(str.charAt(i) == '.'){
                count = i;
            }
        }
        return str.substring(0,count);
    }
}
