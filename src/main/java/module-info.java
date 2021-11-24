module defaultgroup.file_transfer_app {
    requires javafx.controls;
    requires javafx.fxml;


    opens default_group.file_transfer_app to javafx.fxml;
    exports default_group.file_transfer_app;
}