package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.ReservationRepository;
import com.example.parkupjavafx.repositories.VehicleRepository;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class VehicleReviewController {

    @FXML private Label headerLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label returnDateLabel;
    @FXML private Label costLabel;
    @FXML private ComboBox<String> ratingCombo;
    @FXML private TextArea feedbackArea;

    private Reservation reservation;
    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @FXML
    public void initialize() {
        ratingCombo.getItems().addAll("5 - Excellent", "4 - Good", "3 - Average", "2 - Poor", "1 - Very Poor");
        ratingCombo.getSelectionModel().selectFirst();
    }

    public void setReservation(Reservation r) {
        this.reservation = r;
        headerLabel.setText("Rental Summary #R" + r.getId());
        returnDateLabel.setText(LocalDate.now().format(fmt)); // Return date is today
        costLabel.setText("P" + String.format("%.2f", r.getTotalCost()));

        Vehicle v = vehicleRepository.getVehicleById(r.getVehicleId());
        if (v != null) {
            vehicleLabel.setText(v.getModel());
        }
    }

    @FXML
    protected void onSubmitReviewClick() {
        int rating = 5 - ratingCombo.getSelectionModel().getSelectedIndex(); // Convert index 0->5, 1->4...
        String feedback = feedbackArea.getText();

        if (reservationRepository.submitReview(reservation.getId(), rating, feedback)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Review Submitted! Waiting for Admin approval.");
            alert.showAndWait();

            Stage stage = (Stage) headerLabel.getScene().getWindow();
            stage.close();
        }
    }
}