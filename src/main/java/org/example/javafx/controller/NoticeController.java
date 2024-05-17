package org.example.javafx.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.util.HashMap;
import java.util.Map;

public class NoticeController {

    @FXML
    public AnchorPane anchorPane;

    @FXML
    public TextField colorField;

    @FXML
    public Button confirmButton;

    @FXML
    public TextArea textField;

    public void initialize() {
        confirmButton.setOnAction(e ->{
            if (textField.getText().isEmpty()){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText("公告不能为空");
                alert.showAndWait();
            }
            else {
                Map newNotice = new HashMap<>();
                newNotice.put("text",textField.getText());
                if (colorField.getText().isEmpty())
                newNotice.put("color", "#000");
                else newNotice.put("color", colorField.getText());
                DataRequest dataRequest = new DataRequest();
                dataRequest.setData(newNotice);
                HttpRequestUtils.request("/menu/changeNotice", dataRequest);
            }
        });
    }


}