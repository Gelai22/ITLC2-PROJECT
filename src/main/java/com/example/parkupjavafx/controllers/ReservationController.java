package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.User;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.ReservationRepository;
import com.example.parkupjavafx.services.AuthService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservationController {

    @FXML private ImageView vehicleImage;
    @FXML private Label vehicleModelLabel;
    @FXML private Label vehiclePlateLabel;
    @FXML private Label vehicleRateLabel;
    @FXML private Label totalCostLabel;

    @FXML private DatePicker pickupDatePicker;
    @FXML private DatePicker returnDatePicker;
    @FXML private ComboBox<String> pickupLocationCombo;
    @FXML private ComboBox<String> returnLocationCombo;
    @FXML private TextArea notesArea;

    private Vehicle selectedVehicle;
    private final ReservationRepository reservationRepository = new ReservationRepository();

    @FXML
    public void initialize() {
        // Setup Locations
        pickupLocationCombo.getItems().addAll("MAIN_OFFICE", "AIRPORT_A", "CITY_CENTER");
        returnLocationCombo.getItems().addAll("MAIN_OFFICE", "AIRPORT_A", "CITY_CENTER");

        // Default Dates (Tomorrow -> +4 days)
        pickupDatePicker.setValue(LocalDate.now().plusDays(1));
        returnDatePicker.setValue(LocalDate.now().plusDays(4));

        // Add Listeners to recalculate cost when dates change
        pickupDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
        returnDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> calculateTotal());
    }

    // Method to receive data from Dashboard
    public void setVehicle(Vehicle vehicle) {
        this.selectedVehicle = vehicle;

        vehicleModelLabel.setText(vehicle.getModel() + " (" + vehicle.getYear() + ")");
        vehiclePlateLabel.setText(vehicle.getPlateNumber());
        vehicleRateLabel.setText("P" + String.format("%.2f", vehicle.getRentalPrice()) + " / day");

        // Try loading image if available, else keep placeholder
        // if (vehicle.getImagePath() != null) ...

        calculateTotal();
    }

    private void calculateTotal() {
        if (selectedVehicle == null || pickupDatePicker.getValue() == null || returnDatePicker.getValue() == null) {
            return;
        }

        LocalDate start = pickupDatePicker.getValue();
        LocalDate end = returnDatePicker.getValue();

        if (end.isBefore(start)) {
            totalCostLabel.setText("Invalid Dates");
            return;
        }

        long days = ChronoUnit.DAYS.between(start, end);
        if (days < 1) days = 1; // Minimum 1 day rental

        double total = days * selectedVehicle.getRentalPrice();
        totalCostLabel.setText("P" + String.format("%.2f", total));
    }

    @FXML
    protected void onConfirmClick() {
        User user = AuthService.getCurrentUser();
        if (user == null) return; // Should not happen if logged in

        LocalDate start = pickupDatePicker.getValue();
        LocalDate end = returnDatePicker.getValue();
        double total = Double.parseDouble(totalCostLabel.getText().replace("P", "").replace(",", ""));

        Reservation reservation = new Reservation(
                0, // ID auto-generated
                selectedVehicle.getId(),
                user.getName(),
                start,
                end,
                total,
                "PENDING",
                notesArea.getText()
        );

        if (reservationRepository.createReservation(reservation, user.getId())) {
            System.out.println("Reservation Created!");
            closeDialog();
            // Optional: Show success alert
        } else {
            System.err.println("Failed to create reservation.");
            // Optional: Show error alert
        }
    }

    @FXML
    protected void onCancelClick() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) vehicleModelLabel.getScene().getWindow();
        stage.close();
    }
}