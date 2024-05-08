package org.example.javafx.controller;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.example.javafx.pojo.Result;
import org.example.javafx.request.DataRequest;
import org.example.javafx.request.HttpRequestUtils;

import java.io.*;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;

import javax.imageio.ImageIO;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

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
            GPALabel.setText(getGPA((String) studentInfo.get("person_num")));
            person_idLabel.setText((String) studentInfo.get("person_num"));//1
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
            List<Map<String, String>> glories = (List<Map<String, String>>) studentInfo.get("glories");
            String s = "";
            s = s + "小学：" + (String) studentInfo.get("primary") + "\n" + "初中：" + (String) studentInfo.get("junior") + "\n" + "高中：" + (String) studentInfo.get("senior") + "\n" + "大学：山东大学" + "\n";
            for (Map<String, String> m : glories) {
                s = s + "荣誉：" + "\n" + m.get("glory_name") + "," + m.get("glory_type") + "," + m.get("glory_level") + "." + "\n";
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
            FileInputStream inputStream = new FileInputStream("src\\main\\resources\\org\\example\\javafx\\css\\studentPhoto.jpg");
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

    public String getGPA(String student_num) {
        DataRequest dataRequest = new DataRequest();
        dataRequest.add("student_num", student_num);
        Result result = new Result();
        result = HttpRequestUtils.request("/score/selectByStudentNum", dataRequest);
        List<Map> scoreList = (List<Map>) result.getData();
        Double totalCredit = 0.0;
        Double totalGradePoint = 0.0;
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
        return String.valueOf(GPA);
    }


    @FXML
    public void onExportToPDFButtonClick(ActionEvent event) {
        Document document = new Document(PageSize.A4);

        try {
            // 创建一个文件选择器
            FileChooser fileChooser = new FileChooser();
            // 设置文件选择器的标题
            fileChooser.setTitle("保存PDF");
            // 设置默认的文件名
            fileChooser.setInitialFileName(person_nameLabel.getText() + "的学生简历.pdf");
            // 设置文件类型过滤器
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF Files", "*.pdf"));
            // 显示保存文件的对话框
            File file = fileChooser.showSaveDialog(null);
            if (file != null) {
                // 使用用户选择的文件路径创建 FileOutputStream
                PdfWriter.getInstance(document, new FileOutputStream(file));

                //PdfWriter.getInstance(document, new FileOutputStream(person_nameLabel.getText()+"的学生简历.pdf"));
            }
            document.open();

            // 设置中文字体
            BaseFont bfChinese = BaseFont.createFont("C:\\Windows\\Fonts\\simsun.ttc,1", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            com.itextpdf.text.Font fontChinese = new com.itextpdf.text.Font(bfChinese, 12, com.itextpdf.text.Font.NORMAL);
            com.itextpdf.text.Font titleFont = new com.itextpdf.text.Font(bfChinese, 20, com.itextpdf.text.Font.BOLD); // 大字体用于标题

            // 创建标题
            Paragraph title = new Paragraph(person_nameLabel.getText() + "的学生简历", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            // 添加一个空的段落作为间隔
            document.add(new Paragraph(" "));
            // 创建一个表格
            PdfPTable table = new PdfPTable(2); // 2 columns.

            // 添加图片到 PDF
            WritableImage snapshot = personImage.snapshot(null, null);
            File outputFile = new File("person_image.png");
            ImageIO.write(SwingFXUtils.fromFXImage(snapshot, null), "png", outputFile);
            com.itextpdf.text.Image image = com.itextpdf.text.Image.getInstance(outputFile.getAbsolutePath());
            image.scaleAbsolute(100, 100); // 设置图片大小

            // 创建一个 PdfPCell 对象并添加图片
            PdfPCell imageCell = new PdfPCell(image, false);
            imageCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

            // 在表格中添加一行
            table.addCell(new PdfPCell(new Phrase("个人照片:", fontChinese)));
            table.addCell(imageCell);
            table.addCell(new PdfPCell(new Phrase("姓名:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(person_nameLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("性别:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(genderLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("身份证号:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(identity_numberLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("电话号码:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(phone_numberLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("出生日期:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(birthdayLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("邮箱:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(emailLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("地址:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(addressLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("父亲姓名:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(father_nameLabel.getText(), fontChinese)));
            Phrase phrase = new Paragraph("父亲工作:" + father_workLabel.getText(), fontChinese);
            table.addCell(new PdfPCell(new Phrase("父亲工作:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(father_workLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("父亲电话号码:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(father_phone_numberLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("母亲姓名:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(mother_nameLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("母亲工作:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(mother_workLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("母亲电话号码:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(mother_phone_numberLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("学号:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(person_idLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("学院:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(collegeLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("专业:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(majorLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("班级:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(classLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("GPA:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(GPALabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("入学时间:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(admission_dateLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("政治面貌:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(identityLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("学位:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(degreeLabel.getText(), fontChinese)));
            table.addCell(new PdfPCell(new Phrase("个人简介:", fontChinese)));
            table.addCell(new PdfPCell(new Phrase(personal_profileTextArea.getText(), fontChinese)));

            document.add(table);

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
