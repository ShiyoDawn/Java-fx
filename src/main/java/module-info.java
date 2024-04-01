module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;

    requires com.google.gson;
    requires static lombok;


    opens org.example.javafx.request to com.google.gson,javafx.fxml;
    exports org.example.javafx;
    opens org.example.javafx.pojo to com.google.gson,javafx.fxml;
    exports org.example.javafx.pojo;
    exports org.example.javafx.controller;
    opens org.example.javafx.controller to com.google.gson,javafx.fxml;
}