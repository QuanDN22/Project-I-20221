module com.example.project_i {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires annotations;

    opens MyMain to javafx.graphics;
    exports MyMain;
    opens MyController to javafx.controls,javafx.fxml;
    exports MyController;
    exports MyClass;
}