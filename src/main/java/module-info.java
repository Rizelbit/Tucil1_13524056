module tucil1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;

    opens tucil1 to javafx.fxml;
    exports tucil1;
}