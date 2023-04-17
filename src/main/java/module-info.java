module com.targus {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
/*
    opens com.targus to javafx.fxml;
    exports com.targus;
    exports com.targus.ui;
    opens com.targus.ui to javafx.fxml;*/

    opens com.targus.ui to javafx.fxml;
    exports com.targus.ui;
    exports com.targus.utils;
    exports com.targus.ui.controllers;
    opens com.targus.ui.controllers to javafx.fxml;
    exports com.targus.ui.widgets;
    opens com.targus.ui.widgets to javafx.fxml;
    exports com.targus.base;
    opens com.targus.base to javafx.fxml;
    exports com.targus.problem.wsn;
    opens com.targus.problem.wsn to javafx.fxml;

}