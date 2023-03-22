module com.logindemo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.logindemo to javafx.fxml;
    exports com.logindemo;
}