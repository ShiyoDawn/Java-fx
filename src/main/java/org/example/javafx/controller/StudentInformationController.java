package org.example.javafx.controller;

import com.itextpdf.text.pdf.BaseFont;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
public class StudentInformationController {

    @FXML
    private Label GPALabel;

    @FXML
    private Label addressLabel;

    @FXML
    private Label admission_dateLabel;

    @FXML
    private Label birthdayLabel;

    @FXML
    private Label classLabel;

    @FXML
    private Label collegeLabel;

    @FXML
    private Label degreeLabel;

    @FXML
    private Label emailLabel;

    @FXML
    private Button exitButton;

    @FXML
    private Button export_to_PDFButton;

    @FXML
    private Label father_nameLabel;

    @FXML
    private Label father_phone_numberLabel;

    @FXML
    private Label father_workLabel;

    @FXML
    private Label genderLabel;

    @FXML
    private Label identityLabel;

    @FXML
    private Label identity_numberLabel;

    @FXML
    private Label majorLabel;

    @FXML
    private Label mother_nameLabel;

    @FXML
    private Label mother_phone_numberLabel;

    @FXML
    private Label mother_workLabel;

    @FXML
    private ImageView personImage;

    @FXML
    private Label person_idLabel;

    @FXML
    private Label person_nameLabel;

    @FXML
    private TextArea personal_profileTextArea;

    @FXML
    private Label phone_numberLabel;

    @FXML
    public void initData(Map<String, String> selectedStudent) {
        String studentId = selectedStudent.get("id");
        if (studentId == null || studentId.isEmpty()) {
            System.out.println("Error: No student ID provided.");
            return;
        }
        DataRequest req = new DataRequest();
        req.add("id", studentId);
        Result studentResult = HttpRequestUtils.request("/student/getStudentInfo", req);
        if (studentResult != null && studentResult.getCode() == 200) {
            Map<String, Object> studentInfo = (Map<String, Object>) studentResult.getData();
            person_idLabel.setText((String)studentInfo.get("person_num"));//1
            person_nameLabel.setText((String) studentInfo.get("student_name"));//1
            genderLabel.setText((String) studentInfo.get("gender"));//1
            birthdayLabel.setText((String) studentInfo.get("birthday"));//1
            identityLabel.setText((String) studentInfo.get("identity"));//1
            identity_numberLabel.setText((String) studentInfo.get("identity_number"));//1
            phone_numberLabel.setText((String) studentInfo.get("phone_number"));//1
            emailLabel.setText((String) studentInfo.get("email"));//1
            //admission_dateLabel.setText((String) studentInfo.get("admission_date"));
            collegeLabel.setText((String) studentInfo.get("department"));//1
            majorLabel.setText((String) studentInfo.get("major"));//1
            classLabel.setText((String) studentInfo.get("classes"));//1
            degreeLabel.setText((String) studentInfo.get("grade"));//1
            //GPALabel.setText("");
            List<Map<String, String>> studentFamilies = (List<Map<String, String>>) studentInfo.get("studentFamilies");
            if (studentFamilies != null && !studentFamilies.isEmpty()) {
                for (Map<String, String> familyMember : studentFamilies) {
                    if ("父亲".equals(familyMember.get("relation"))) {
                        father_nameLabel.setText((String) familyMember.get("name"));
                        father_phone_numberLabel.setText((String) familyMember.get("phone"));
                        father_workLabel.setText((String) familyMember.get("job"));
                        addressLabel.setText((String) familyMember.get("address"));
                    } else if ("母亲".equals(familyMember.get("relation"))) {
                        mother_nameLabel.setText((String) familyMember.get("name"));
                        mother_phone_numberLabel.setText((String) familyMember.get("phone"));
                        mother_workLabel.setText((String) familyMember.get("job"));
                        addressLabel.setText((String) familyMember.get("address"));
                    }
                }
            }
            List<Map<String, String>> glories=(List<Map<String, String>>)studentInfo.get("glories");
            String s="";
            s=s+"小学："+(String) studentInfo.get("primary")+"\n"+"初中："+(String) studentInfo.get("junior")+"\n"+"高中："+(String) studentInfo.get("senior")+"\n"+"大学：山东大学"+"\n";
            for (Map<String,String> m:glories){
                s=s+"荣誉："+"\n"+m.get("glory_name")+","+m.get("glory_type")+","+m.get("glory_level")+"."+"\n";
            }
            personal_profileTextArea.setText(s);

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText((studentResult != null && studentResult.getMsg() != null) ? studentResult.getMsg() : "Failed to fetch student information.");
            alert.showAndWait();
        }
        try {
            // 将"path_to_your_image"改为实际图片文件的路径
            FileInputStream inputStream = new FileInputStream("C:\\Users\\Lenovo\\Desktop\\b_ceeab09a7a242063992bbc5b656ffba3.jpg");
            Image image = new Image(inputStream);
            personImage.setImage(image);
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("错误：找不到图片文件。");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
    @FXML
    void onExitButtonAction() {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onExportPDFButtonAction(ActionEvent event) {
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, new FileOutputStream("student_information.pdf"));
            document.open();

            // 设置中文字体
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            com.itextpdf.text.Font fontChinese = new com.itextpdf.text.Font(bfChinese, 12, com.itextpdf.text.Font.NORMAL);

            // 添加图片到 PDF
            WritableImage snapshot = personImage.snapshot(null, null);
            File outputFile = new File("person_image.png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(outputFile.getAbsolutePath());
            document.add(image);

            // 添加文本信息到 PDF
            document.add(new Paragraph("姓名: " + person_nameLabel.getText(), fontChinese));
            document.add(new Paragraph("性别: " + genderLabel.getText(), fontChinese));
            document.add(new Paragraph("身份证号: " + identity_numberLabel.getText(), fontChinese));
            document.add(new Paragraph("电话号码: " + phone_numberLabel.getText(), fontChinese));
            document.add(new Paragraph("出生日期: " + birthdayLabel.getText(), fontChinese));
            document.add(new Paragraph("邮箱: " + emailLabel.getText(), fontChinese));
            document.add(new Paragraph("地址: " + addressLabel.getText(), fontChinese));
            document.add(new Paragraph("父亲姓名: " + father_nameLabel.getText(), fontChinese));
            document.add(new Paragraph("父亲工作: " + father_workLabel.getText(), fontChinese));
            document.add(new Paragraph("父亲电话号码: " + father_phone_numberLabel.getText(), fontChinese));
            document.add(new Paragraph("母亲姓名: " + mother_nameLabel.getText(), fontChinese));
            document.add(new Paragraph("母亲工作: " + mother_workLabel.getText(), fontChinese));
            document.add(new Paragraph("母亲电话号码: " + mother_phone_numberLabel.getText(), fontChinese));
            document.add(new Paragraph("学号: " + person_idLabel.getText(), fontChinese));
            document.add(new Paragraph("学院: " + collegeLabel.getText(), fontChinese));
            document.add(new Paragraph("专业: " + majorLabel.getText(), fontChinese));
            document.add(new Paragraph("班级: " + classLabel.getText(), fontChinese));
            document.add(new Paragraph("GPA: " + GPALabel.getText(), fontChinese));
            document.add(new Paragraph("入学时间: " + admission_dateLabel.getText(), fontChinese));
            document.add(new Paragraph("政治面貌: " + identityLabel.getText(), fontChinese));
            document.add(new Paragraph("学位: " + degreeLabel.getText(), fontChinese));
            document.add(new Paragraph("个人简介: " + personal_profileTextArea.getText(), fontChinese));

            document.close();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("成功");
            alert.setHeaderText(null);
            alert.setContentText("学生信息已成功导出到PDF文件。");
            alert.showAndWait();
        } catch (DocumentException | IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("错误");
            alert.setHeaderText(null);
            alert.setContentText("导出PDF文件时发生错误。");
            alert.showAndWait();
            e.printStackTrace();
        }
    }
}
