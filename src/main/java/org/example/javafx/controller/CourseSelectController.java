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
    static double credits = 0.0;

    @FXML
    public void initialize() throws IOException, InterruptedException {
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
            label.setText("      " + a.get("course_name") + "   " + a.get("classes") + " " + a.get("teacher_name") + "    "+ String.valueOf(a.get("students"))+"/"+a.get("capacity"));
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
                } else if (dataListP.size() == 0) {
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
                    dataRequest1.setData(map1);
                    Result data1 = null;
                    try {
                        data1 = HttpRequestUtils.courseField("/course/addStudent", dataRequest1);
                        if (data1.getCode() == 200) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setContentText("选课成功");
                            alert.showAndWait();
                            DataRequest dataRequestG = new DataRequest();
                            Map<String, String> mapG = new HashMap<>();
                            mapG.put("course_id", String.valueOf(a.get("id")));
                            dataRequestG.setData(mapG);
                            Result dataG = HttpRequestUtils.courseField("/lesson/selectByCourseId", dataRequestG);
                            List<Map<String, ? extends Object>> dataListG = new Gson().fromJson(dataG.getData().toString(), List.class);
                            addLabelGrid(dataListG);
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
            double week_time = Double.valueOf(String.valueOf(a.get("week_time")));
            String room = (String) a.get("room");
            double time_sort = Double.valueOf(String.valueOf( a.get("time_sort")));
            Label label = new Label();
            label.setId("1");
            label.setTextAlignment(TextAlignment.CENTER);
            label.setWrapText(true);
            gridPane.setHgrow(label, Priority.ALWAYS);
            gridPane.setVgrow(label, Priority.ALWAYS);
            label.setStyle("-fx-background-color: #00ff33; -fx-padding: 10;");
            label.setText(course_name + "\n" + room);
            gridPane.setHalignment(label, HPos.CENTER); // 设置水平对齐方式为居中
            gridPane.setValignment(label, VPos.CENTER); // 设置垂直对齐方式为居中
            gridPane.add(label, (int) week_time, (int) (time_sort));
        }
    }
}
