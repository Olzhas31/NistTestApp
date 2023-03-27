module com.example.nisttestapp {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires commons.math3;

    opens com.example.nisttestapp to javafx.fxml;
    exports com.example.nisttestapp;
}