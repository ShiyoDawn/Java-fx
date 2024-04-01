package org.example.javafx.pojo;

import lombok.Data;

@Data
public class User {

    private Integer id;

    private String person_num;

    private String password;

    private Integer user_type_id;

    private String last_login_time;

    private Integer login_count;

    private String create_time;


}
