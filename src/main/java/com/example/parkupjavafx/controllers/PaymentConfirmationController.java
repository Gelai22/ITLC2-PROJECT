package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.ParkUpApplication;
import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.ReservationRepository;
import com.example.parkupjavafx.repositories.VehicleRepository;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PaymentConfirmationController {

    @FXML private Label headerLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label pickupLabel;
    @FXML private Label returnLabel;
    @FXML private Label totalLabel;

    private Reservation reservation;
    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public void setReservation(Reservation r) {
        this.reservation = r;

        headerLabel.setText("Booking Summary #R" + r.getId());
        pickupLabel.setText(r.getPickupDate().format(fmt));
        returnLabel.setText(r.getReturnDate().format(fmt));
        totalLabel.setText("P" + String.format("%.2f", r.getTotalCost()));

        Vehicle v = vehicleRepository.getVehicleById(r.getVehicleId());
        if (v != null) {
            vehicleLabel.setText(v.getModel() + " (" + v.getYear() + ")");
        }
    }

    @FXML
    protected void onConfirmPayClick() {
        if (reservationRepository.confirmPayment(reservation.getId())) {
            // Close Payment Window
            Stage currentStage = (Stage) headerLabel.getScene().getWindow();
            currentStage.close();

            // Open Receipt Window
            openReceipt();
        }
    }

    @FXML
    protected void onCancelClick() {
        Stage stage = (Stage) headerLabel.getScene().getWindow();
        stage.close();
    }

    private void openReceipt() {
        try {
            FXMLLoader loader = new FXMLLoader(ParkUpApplication.class.getResource("receipt.fxml"));
            Parent root = loader.load();

            ReceiptController controller = loader.getController();
            controller.setReservation(reservation); // Pass data to receipt

            Stage stage = new Stage();
            stage.setTitle("Official Receipt");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}