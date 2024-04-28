package org.example.javafx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {
    private String id;

    private String person_id;

    private String student_name;

    private String department;

    private String classes;

    private String grade;

    private String major;

}
