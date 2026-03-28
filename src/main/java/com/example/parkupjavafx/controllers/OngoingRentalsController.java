package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.models.Reservation;
import com.example.parkupjavafx.models.User;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.ReservationRepository;
import com.example.parkupjavafx.repositories.VehicleRepository;
import com.example.parkupjavafx.services.AuthService;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class OngoingRentalsController {

    @FXML private TableView<Reservation> ongoingTable;
    @FXML private TableColumn<Reservation, String> idCol;
    @FXML private TableColumn<Reservation, String> vehicleCol;
    @FXML private TableColumn<Reservation, String> pickupCol;
    @FXML private TableColumn<Reservation, String> returnCol;
    @FXML private TableColumn<Reservation, String> durationCol;
    @FXML private TableColumn<Reservation, Reservation> actionCol;

    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d, yyyy");

    @FXML
    public void initialize() {
        User user = AuthService.getCurrentUser();
        if (user != null) {
            loadData(user);
        }
    }

    private void loadData(User user) {
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty("#R" + cellData.getValue().getId()));

        vehicleCol.setCellValueFactory(cellData -> {
            Vehicle v = vehicleRepository.getVehicleById(cellData.getValue().getVehicleId());
            return new SimpleStringProperty(v != null ? v.getModel() : "Unknown");
        });

        pickupCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getPickupDate().format(dateFormatter)));

        returnCol.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getReturnDate().format(dateFormatter)));

        durationCol.setCellValueFactory(cellData -> {
            long days = ChronoUnit.DAYS.between(cellData.getValue().getPickupDate(), cellData.getValue().getReturnDate());
            return new SimpleStringProperty(days + " days");
        });

        actionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button returnBtn = new Button("Return Car");
            private final Button receiptBtn = new Button("View Receipt");
            private final HBox pane = new HBox(10, returnBtn, receiptBtn);

            {
                returnBtn.getStyleClass().add("btn-danger");
                returnBtn.setStyle("-fx-background-color: white; -fx-text-fill: #fb977d; -fx-border-color: #fb977d; -fx-border-radius: 5; -fx-cursor: hand;");
                returnBtn.setOnAction(e -> handleReturn(getItem()));

                receiptBtn.getStyleClass().add("btn-primary");
                receiptBtn.setStyle("-fx-font-size: 11px; -fx-padding: 5 10;");
                receiptBtn.setOnAction(e -> System.out.println("View Receipt for ID: " + getItem().getId()));
            }

            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        ongoingTable.setItems(FXCollections.observableArrayList(
                reservationRepository.findOngoingRentals(user.getEmail())
        ));
    }

    private void handleReturn(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(com.example.parkupjavafx.ParkUpApplication.class.getResource("vehicle_review.fxml"));
            javafx.scene.Parent root = loader.load();

            VehicleReviewController controller = loader.getController();
            controller.setReservation(reservation);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Return & Review");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh table after review is submitted
            loadData(AuthService.getCurrentUser());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}