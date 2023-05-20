module library.management.system.ui {

    requires javafx.fxml;
    requires javafx.controls;
    requires library.management.system.common;

    opens library.management.system.ui to javafx.fxml;
    exports library.management.system.ui;
}