module com.app.questit {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires java.sql;
    requires spring.context;
    requires org.apache.logging.log4j;

    opens com.app.questit to javafx.fxml;
    exports com.app.questit;
    opens com.app.questit.domain to javafx.base;
    opens com.app.questit.configs to spring.core;
    exports com.app.questit.configs;

    opens com.app.questit.controllers to javafx.fxml;
    exports com.app.questit.controllers;
}