module org.example.javafx {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.javafx to javafx.fxml;
    exports org.example.javafx;
}