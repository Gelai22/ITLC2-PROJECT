package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.ParkUpApplication;
import com.example.parkupjavafx.repositories.UserRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserRepository userRepository = new UserRepository();

    @FXML
    protected void onRegisterClick() {
        String name = nameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            errorLabel.setText("All fields are required.");
            errorLabel.setVisible(true);
            return;
        }

        if (userRepository.emailExists(email)) {
            errorLabel.setText("Email is already taken.");
            errorLabel.setVisible(true);
            return;
        }

        if (userRepository.register(name, email, password)) {
            System.out.println("Registration Successful");
            // Automatically redirect to Login
            try {
                onLoginLinkClick();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Registration failed. Try again.");
            errorLabel.setVisible(true);
        }
    }

    @FXML
    protected void onLoginLinkClick() throws IOException {
        Stage stage = (Stage) nameField.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ParkUpApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 850);
        stage.setScene(scene);
    }

    @FXML
    protected void onBackClick() throws IOException {
        onLoginLinkClick(); // For now, back goes to login
    }
}