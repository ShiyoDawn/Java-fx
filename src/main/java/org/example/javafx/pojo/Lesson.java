package org.example.javafx.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Lesson {
    private String course_id;
    private Integer week;
    private Integer week_time;
    private Integer time_sort;
    private String room;
}
