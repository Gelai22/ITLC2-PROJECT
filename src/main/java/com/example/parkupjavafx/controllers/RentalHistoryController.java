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
import java.time.temporal.ChronoUnit;

public class RentalHistoryController {

    @FXML private TableView<Reservation> historyTable;
    @FXML private TableColumn<Reservation, String> idCol;
    @FXML private TableColumn<Reservation, String> vehicleCol;
    @FXML private TableColumn<Reservation, String> periodCol;
    @FXML private TableColumn<Reservation, String> returnCol;
    @FXML private TableColumn<Reservation, String> costCol;
    @FXML private TableColumn<Reservation, String> statusCol;

    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @FXML
    public void initialize() {
        User user = AuthService.getCurrentUser();
        if (user != null) {
            idCol.setCellValueFactory(cellData -> new SimpleStringProperty("#R" + cellData.getValue().getId()));

            vehicleCol.setCellValueFactory(cellData -> {
                Vehicle v = vehicleRepository.getVehicleById(cellData.getValue().getVehicleId());
                return new SimpleStringProperty(v != null ? v.getModel() : "Unknown");
            });

            periodCol.setCellValueFactory(cellData -> {
                long days = ChronoUnit.DAYS.between(cellData.getValue().getPickupDate(), cellData.getValue().getReturnDate());
                return new SimpleStringProperty(days + " days");
            });

            returnCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty(cellData.getValue().getReturnDate().format(dateFormatter)));

            costCol.setCellValueFactory(cellData ->
                    new SimpleStringProperty("P" + String.format("%.2f", cellData.getValue().getTotalCost())));

            statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

            historyTable.setItems(FXCollections.observableArrayList(
                    reservationRepository.findRentalHistory(user.getEmail())
            ));
        }
    }
}