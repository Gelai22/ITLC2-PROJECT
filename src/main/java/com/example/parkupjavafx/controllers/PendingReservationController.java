package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.User;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.ReservationRepository;
import com.example.parkupjavafx.repositories.VehicleRepository;
import com.example.parkupjavafx.services.AuthService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

public class PendingReservationController {

    @FXML private TableView<Reservation> reservationsTable;
    @FXML private TableColumn<Reservation, String> idCol;
    @FXML private TableColumn<Reservation, String> vehicleCol;
    @FXML private TableColumn<Reservation, String> pickupCol;
    @FXML private TableColumn<Reservation, String> returnCol;
    @FXML private TableColumn<Reservation, String> costCol;
    @FXML private TableColumn<Reservation, String> statusCol;

    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();

    // Formatter for dates (e.g. "Nov 24, 2025")
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @FXML
    public void initialize() {
        User user = AuthService.getCurrentUser();
        if (user != null) {

            // FIX: Use Lambdas to ensure data is retrieved correctly
            idCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty("#R" + cellData.getValue().getId()));

            vehicleCol.setCellValueFactory(cellData -> {
                Vehicle v = vehicleRepository.getVehicleById(cellData.getValue().getVehicleId());
                return new SimpleStringProperty(v != null ? v.getModel() : "Unknown");
            });

            pickupCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getPickupDate().format(dateFormatter)));

            returnCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getReturnDate().format(dateFormatter)));

            costCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty("P" + String.format("%.2f", cellData.getValue().getTotalCost())));

            statusCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getStatus()));

            // Load Data
            reservationsTable.setItems(FXCollections.observableArrayList(
                    reservationRepository.findByCustomerEmail(user.getEmail())
            ));
        }
    }
}