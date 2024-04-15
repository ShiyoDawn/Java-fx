package org.example.javafx.pojo;

import lombok.Data;

@Data
public class Course {
    private Integer id;
    private String course_name;
    private Double credit;
    private Integer num;
    private Integer course_type_id;
    private Integer pre_course_id;
    private String book;
    private String extracurricular;
    public Course(String course_name){
        this.course_name = course_name;
    }
}
