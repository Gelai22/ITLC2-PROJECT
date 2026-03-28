package com.example.parkupjavafx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ParkUpApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ParkUpApplication.class.getResource("login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 1300, 850);

        stage.setTitle("ParkUp - Client Portal");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}