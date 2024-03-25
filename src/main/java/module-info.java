module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;


    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
    exports org.example.javafx.controller;
    opens org.example.javafx.controller to javafx.fxml;
}