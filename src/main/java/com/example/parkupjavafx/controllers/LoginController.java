package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.ParkUpApplication;
import com.example.parkupjavafx.services.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    // FIX: Instantiate the Service (Don't use UserRepository directly here)
    private final AuthService authService = new AuthService();

    @FXML
    protected void onLoginClick() {
        String email = emailField.getText();
        String password = passwordField.getText();

        // FIX: Let the Service handle the logic.
        // If login returns true, the user is saved in AuthService automatically.
        if (authService.login(email, password)) {

            try {
                FXMLLoader fxmlLoader = new FXMLLoader(ParkUpApplication.class.getResource("dashboard.fxml"));
                Scene dashboardScene = new Scene(fxmlLoader.load(), 1300, 850);

                Stage stage = (Stage) emailField.getScene().getWindow();

                stage.setTitle("ParkUp - Dashboard");
                stage.setScene(dashboardScene);
                stage.centerOnScreen();

            } catch (IOException e) {
                e.printStackTrace();
                messageLabel.setText("Error loading dashboard.");
                messageLabel.setVisible(true);
            }

        } else {
            messageLabel.setText("Invalid email or password.");
            messageLabel.setStyle("-fx-text-fill: #fb977d;");
            messageLabel.setVisible(true);
        }
    }

    @FXML
    protected void onRegisterLinkClick() throws IOException {
        Stage stage = (Stage) emailField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ParkUpApplication.class.getResource("register.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 850);
        stage.setScene(scene);
    }
}