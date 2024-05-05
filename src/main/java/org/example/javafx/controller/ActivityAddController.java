package org.example.javafx.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

public class ActivityAddController {

    @FXML
    private DatePicker activityDatePicker;

    @FXML
    private TextField activityNameTextField;

    @FXML
    private ComboBox activityTypeComboBox;

    @FXML
    private TextField addTextField;

    @FXML
    private Button cancelButton;

    @FXML
    private Button saveButton;

    @FXML
    private Spinner scoreSpinner;

    @FXML
    private TextField personNumTextField;

    @FXML
    public void initialize() {
        // 初始化activityTypeComboBox的内容
        activityTypeComboBox.getItems().addAll("社会实践", "学科竞赛", "科技成果", "培训讲座", "创新项目","校外实习","学生荣誉","体育活动","外出旅游","文艺演出","聚会","其他");
        // 设置scoreSpinner的范围
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);
        scoreSpinner.setValueFactory(valueFactory);
    }
    @FXML
    void onCancelButtonClickAction(ActionEvent event) {
        // 获取当前按钮的场景
        Scene scene = cancelButton.getScene();
        // 获取场景的窗口
        Window window = scene.getWindow();
        // 如果窗口是Stage类型，关闭它
        if (window instanceof Stage) {
            ((Stage) window).close();
        }
    }

    @FXML
    void onSaveButtonClickAction(ActionEvent event) {
        // 创建一个新的DataRequest对象
        DataRequest dataRequest = new DataRequest();

        // 获取activityDatePicker，activityNameTextField，activityTypeComboBox，addTextField，scoreSpinner中的内容
        String activityDate = activityDatePicker.getValue().toString();
        String activityName = activityNameTextField.getText();
        String activityType = activityTypeComboBox.getValue().toString();
        String addText = addTextField.getText();
        String score = scoreSpinner.getValue().toString();
        String personNum = personNumTextField.getText();

        // 将获取的内容存入DataRequest对象中
        dataRequest.add("activityDate", activityDate);
        dataRequest.add("activityName", activityName);
        dataRequest.add("activityType", activityType);
        dataRequest.add("addText", addText);
        dataRequest.add("score", score);
        dataRequest.add("personNum", personNum);

        // 发送DataRequest对象到后端的/evaluate/addEvaluate接口
        try {
            Result result = HttpRequestUtils.request("/evaluate/addEvaluate", dataRequest);
            // 检查结果，如果请求失败，打印错误信息
            if (result.getCode() != 200) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("错误");
                alert.setHeaderText(null);
                alert.setContentText("添加学生失败。错误：" + result.getMsg());
                alert.showAndWait();
            }
            Scene scene = saveButton.getScene();
            // 获取场景的窗口
            Window window = scene.getWindow();
            // 如果窗口是Stage类型，关闭它
            if (window instanceof Stage) {
                ((Stage) window).close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

