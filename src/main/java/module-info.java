module com.parkupjavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;

    requires jbcrypt;

    opens com.example.parkupjavafx.models to javafx.base;

    opens com.example.parkupjavafx.controllers to javafx.fxml;

    exports com.example.parkupjavafx;
    exports com.example.parkupjavafx.controllers;
    exports com.example.parkupjavafx.models;
    exports com.example.parkupjavafx.services;
    exports com.example.parkupjavafx.repositories;
    exports com.example.parkupjavafx.config;
}