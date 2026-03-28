package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.VehicleRepository;
import javafx.fxml.FXML;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReceiptController {

    @FXML private VBox printableArea; // The box we want to print
    @FXML private Label receiptTitle;
    @FXML private Label datePaidLabel;
    @FXML private Label vehicleLabel;
    @FXML private Label periodLabel;
    @FXML private Label amountLabel;

    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy");

    public void setReservation(Reservation r) {
        receiptTitle.setText("Official Receipt #R" + r.getId());
        datePaidLabel.setText("Date Paid: " + LocalDate.now().format(fmt));

        Vehicle v = vehicleRepository.getVehicleById(r.getVehicleId());
        if (v != null) {
            vehicleLabel.setText(v.getModel());
        }

        periodLabel.setText(r.getPickupDate().format(fmt) + " to " + r.getReturnDate().format(fmt));
        amountLabel.setText("P" + String.format("%.2f", r.getTotalCost()));
    }

    @FXML
    protected void onPrintClick() {
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null) {
            boolean proceed = job.showPrintDialog(printableArea.getScene().getWindow());
            if (proceed) {
                // Print the specific VBox (printableArea)
                boolean success = job.printPage(printableArea);
                if (success) {
                    job.endJob();
                }
            }
        }
    }

    @FXML
    protected void onCloseClick() {
        Stage stage = (Stage) receiptTitle.getScene().getWindow();
        stage.close();
    }
}