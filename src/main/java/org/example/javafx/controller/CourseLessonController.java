package org.example.javafx.controller;

import com.google.gson.Gson;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;
import org.example.javafx.MainApplication;
import org.example.javafx.pojo.Lesson;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import javafx.scene.Node;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseLessonController {
    static String num;
    static String id;
    static String name;
    static int columnIndex;
    static int rowIndex;
    static List<String[]> list = new ArrayList<>();
    static List<double[]> list2 = new ArrayList<>();

    @FXML
    GridPane gridPane;
    @FXML
    Button save;
    @FXML
    ComboBox comboBoxWeek;
    @FXML
    public void initialize() throws IOException, InterruptedException {
        comboBoxWeek.setValue("编辑全部");
        comboBoxWeek.getEditor().textProperty().addListener((observable, oldValue, newValue) -> {
            deleteNode();
            addLabelByList(newValue,list);
        });
        DataRequest dataRequest = new DataRequest();
        Map<String,String> map = new HashMap<>();
        map.put("num",num);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/course/selectByNum2", dataRequest);
        List<Map<String, String>> dataList = new Gson().fromJson(data.getData().toString(), List.class);
        id = String.valueOf(dataList.get(0).get("id"));
        name = String.valueOf(dataList.get(0).get("course_name"));
        addGrid();
    }

    private void deleteNode() {
        gridPane.getChildren().forEach(node -> {
            if (node.getId() == "true" && (node instanceof Label)) {
                node.setVisible(false);
            }
        });
    }
    private void addGrid(){
        for (int i = 1; i <= 7 ; i++) {
            for(int j = 1; j <= 5; j++) {
                gridPane.add(addLabel(),i,j);
            }
        }
    }
    private Label addLabel(){
        Label label = new Label();
        label.setId("false");
        label.setTextAlignment(TextAlignment.CENTER);
        label.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        label.setWrapText(true);
        label.setMaxSize(100,100);
        GridPane.setHgrow(label,Priority.ALWAYS);
        GridPane.setVgrow(label, Priority.ALWAYS);
        gridPane.setHalignment(label, HPos.CENTER); // 设置水平对齐方式为居中
        gridPane.setValignment(label, VPos.CENTER); // 设置垂直对齐方式为居中
        label.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1 && label.getId().equals("false")) { // 检查是否是单击事件
                columnIndex = GridPane.getColumnIndex(label);
                rowIndex = GridPane.getRowIndex(label);
                label.setId("true");
                label.setText(" " + name + "\n");
                label.setStyle("-fx-background-color: #00ff33; -fx-padding: 10;");
                addList();
            } else if(event.getClickCount() == 1 && label.getId().equals("true")){
                columnIndex = GridPane.getColumnIndex(label);
                rowIndex = GridPane.getRowIndex(label);
                label.setId("false");
                label.setText("");
                label.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
                deleteList();
            }
        });
        return label;
    }
    private void setLabelTrue(int column,int row){
        for (Node node : gridPane.getChildren()) {
            Integer rowIndex = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            if (rowIndex != null && colIndex != null && rowIndex == row && colIndex == column && node instanceof Label) {
                node.setVisible(true);
                node.setId("true");
                break;
            }
        }
    }
    private void addLabelByList(String newValue,List<String[]> list) {
        if((String)comboBoxWeek.getValue() == null || !(String.valueOf(comboBoxWeek.getValue()).equals("编辑全部") ||  String.valueOf(comboBoxWeek.getValue()).equals("第1周") || String.valueOf(comboBoxWeek.getValue()).equals("第2周") || String.valueOf(comboBoxWeek.getValue()).equals("第3周") || String.valueOf(comboBoxWeek.getValue()).equals("第4周") || String.valueOf(comboBoxWeek.getValue()).equals("第5周") || String.valueOf(comboBoxWeek.getValue()).equals("第6周") || String.valueOf(comboBoxWeek.getValue()).equals("第7周") || String.valueOf(comboBoxWeek.getValue()).equals("第8周") || String.valueOf(comboBoxWeek.getValue()).equals("第9周") || String.valueOf(comboBoxWeek.getValue()).equals("第10周") || String.valueOf(comboBoxWeek.getValue()).equals("第11周") || String.valueOf(comboBoxWeek.getValue()).equals("第12周") || String.valueOf(comboBoxWeek.getValue()).equals("第13周") || String.valueOf(comboBoxWeek.getValue()).equals("第14周") || String.valueOf(comboBoxWeek.getValue()).equals("第15周") || String.valueOf(comboBoxWeek.getValue()).equals("第16周") || String.valueOf(comboBoxWeek.getValue()).equals("第17周") || String.valueOf(comboBoxWeek.getValue()).equals("第18周") || String.valueOf(comboBoxWeek.getValue()).equals("第19周") )){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择正确的上课周数");
            alert.showAndWait();
        } else {
            if(!list.isEmpty()){
                if (newValue.equals("编辑全部")) {
                    for (String[] strings : list) {
                        setLabelTrue(Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
                    }
                } else {
                    for (String[] strings : list) {
                        if (("第" + strings[0] + "周").equals(newValue)) {
                            setLabelTrue(Integer.parseInt(strings[1]),Integer.parseInt(strings[2]));
                        }
                    }
                }
            }
        }

    }
    private void addList(){
        if((String)comboBoxWeek.getValue() == null || !(String.valueOf(comboBoxWeek.getValue()).equals("编辑全部") ||  String.valueOf(comboBoxWeek.getValue()).equals("第1周") || String.valueOf(comboBoxWeek.getValue()).equals("第2周") || String.valueOf(comboBoxWeek.getValue()).equals("第3周") || String.valueOf(comboBoxWeek.getValue()).equals("第4周") || String.valueOf(comboBoxWeek.getValue()).equals("第5周") || String.valueOf(comboBoxWeek.getValue()).equals("第6周") || String.valueOf(comboBoxWeek.getValue()).equals("第7周") || String.valueOf(comboBoxWeek.getValue()).equals("第8周") || String.valueOf(comboBoxWeek.getValue()).equals("第9周") || String.valueOf(comboBoxWeek.getValue()).equals("第10周") || String.valueOf(comboBoxWeek.getValue()).equals("第11周") || String.valueOf(comboBoxWeek.getValue()).equals("第12周") || String.valueOf(comboBoxWeek.getValue()).equals("第13周") || String.valueOf(comboBoxWeek.getValue()).equals("第14周") || String.valueOf(comboBoxWeek.getValue()).equals("第15周") || String.valueOf(comboBoxWeek.getValue()).equals("第16周") || String.valueOf(comboBoxWeek.getValue()).equals("第17周") || String.valueOf(comboBoxWeek.getValue()).equals("第18周") || String.valueOf(comboBoxWeek.getValue()).equals("第19周") )){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择正确的上课周数");
            alert.showAndWait();
        } else {
            if(String.valueOf(comboBoxWeek.getValue()).equals("编辑全部")){
                for (int k = 1; k <= 19; k++) {
                    String[] a = new String[3];
                    a[0] = String.valueOf(k);
                    a[1] = String.valueOf(columnIndex);
                    a[2] = String.valueOf(rowIndex);
                    list.add(a);
                    double[] b = new double[3];
                    b[0] = k;
                    b[1] = columnIndex;
                    b[2] = rowIndex;
                    list2.add(b);
                }
            } else if(String.valueOf(comboBoxWeek.getValue()).length() == 3){
                String[] a = new String[3];
                a[0] = String.valueOf(comboBoxWeek.getValue()).substring(1,2);
                a[1] = String.valueOf(columnIndex);
                a[2] = String.valueOf(rowIndex);
                list.add(a);
            } else {
                String[] a = new String[3];
                a[0] = String.valueOf(comboBoxWeek.getValue()).substring(1,3);
                a[1] = String.valueOf(columnIndex);
                a[2] = String.valueOf(rowIndex);
                list.add(a);
            }
        }
    }
    private void deleteList(){
        if((String)comboBoxWeek.getValue() == null || !(String.valueOf(comboBoxWeek.getValue()).equals("编辑全部") ||  String.valueOf(comboBoxWeek.getValue()).equals("第1周") || String.valueOf(comboBoxWeek.getValue()).equals("第2周") || String.valueOf(comboBoxWeek.getValue()).equals("第3周") || String.valueOf(comboBoxWeek.getValue()).equals("第4周") || String.valueOf(comboBoxWeek.getValue()).equals("第5周") || String.valueOf(comboBoxWeek.getValue()).equals("第6周") || String.valueOf(comboBoxWeek.getValue()).equals("第7周") || String.valueOf(comboBoxWeek.getValue()).equals("第8周") || String.valueOf(comboBoxWeek.getValue()).equals("第9周") || String.valueOf(comboBoxWeek.getValue()).equals("第10周") || String.valueOf(comboBoxWeek.getValue()).equals("第11周") || String.valueOf(comboBoxWeek.getValue()).equals("第12周") || String.valueOf(comboBoxWeek.getValue()).equals("第13周") || String.valueOf(comboBoxWeek.getValue()).equals("第14周") || String.valueOf(comboBoxWeek.getValue()).equals("第15周") || String.valueOf(comboBoxWeek.getValue()).equals("第16周") || String.valueOf(comboBoxWeek.getValue()).equals("第17周") || String.valueOf(comboBoxWeek.getValue()).equals("第18周") || String.valueOf(comboBoxWeek.getValue()).equals("第19周") )){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("请选择正确的上课周数");
            alert.showAndWait();
        } else {
            if(String.valueOf(comboBoxWeek.getValue()).equals("编辑全部")){
                for (int k = 1; k <= 19; k++) {
                    for (String[] a : list) {
                        if(Integer.parseInt(a[0]) == k && a[1].equals(String.valueOf(columnIndex)) && a[2].equals(String.valueOf(rowIndex))){
                            list.remove(a);
                            break;
                        }
                    }
                }
            } else if(String.valueOf(comboBoxWeek.getValue()).length() == 3){
                for (String[] a : list) {
                    if(a[0].equals(String.valueOf(comboBoxWeek.getValue()).substring(1,2))&& a[1].equals(String.valueOf(columnIndex)) && a[2].equals(String.valueOf(rowIndex))){
                        list.remove(a);
                        break;
                    }
                }
            } else {
                for (String[] a : list) {
                    if(a[0].equals(String.valueOf(comboBoxWeek.getValue()).substring(1,3))&& a[1].equals(String.valueOf(columnIndex)) && a[2].equals(String.valueOf(rowIndex))){
                        list.remove(a);
                        break;
                    }
                }
            }
        }
    }
    public void saveC() throws IOException, InterruptedException {
        DataRequest dataRequest = new DataRequest();
        List<String[]> list1 = new ArrayList<>();
        String[] b = new String[1];
        b[0] = id ;
        list1.add(b);
        Map<String,List<String[]>> map = new HashMap<>();
        map.put("lesson",list);
        map.put("course_id",list1);
        dataRequest.setData(map);
        Result data = HttpRequestUtils.courseField("/lesson/addLesson", dataRequest);
        String s = data.getMsg();
        if(s.equals("添加成功")){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText("保存成功");
            alert.showAndWait();
            Node node = save.getScene().getRoot();
            Window window = node.getScene().getWindow();
            window.hide(); // 关闭窗口
        }
    }
}
