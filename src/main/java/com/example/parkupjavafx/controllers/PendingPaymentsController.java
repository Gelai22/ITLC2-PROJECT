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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.time.format.DateTimeFormatter;

public class PendingPaymentsController {

    @FXML private TableView<Reservation> paymentsTable;
    @FXML private TableColumn<Reservation, String> idCol;
    @FXML private TableColumn<Reservation, String> vehicleCol;
    @FXML private TableColumn<Reservation, String> datesCol;
    @FXML private TableColumn<Reservation, String> costCol;
    @FXML private TableColumn<Reservation, String> statusCol;
    @FXML private TableColumn<Reservation, Reservation> actionCol;

    private final ReservationRepository reservationRepository = new ReservationRepository();
    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMM d");

    @FXML
    public void initialize() {
        User user = AuthService.getCurrentUser();
        if (user != null) {
            setupColumns();
            loadData(user);
        }
    }

    private void setupColumns() {
        idCol.setCellValueFactory(cellData -> new SimpleStringProperty("#R" + cellData.getValue().getId()));

        vehicleCol.setCellValueFactory(cellData -> {
            Vehicle v = vehicleRepository.getVehicleById(cellData.getValue().getVehicleId());
            return new SimpleStringProperty(v != null ? v.getModel() : "Unknown");
        });

        datesCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getPickupDate().format(dateFormatter) + " - " +
                        cellData.getValue().getReturnDate().format(dateFormatter)
        ));

        costCol.setCellValueFactory(cellData ->
                new SimpleStringProperty("P" + String.format("%.2f", cellData.getValue().getTotalCost())));

        statusCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        actionCol.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue()));
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button payBtn = new Button("Proceed to Payment");

            {
                payBtn.getStyleClass().add("btn-success");
                payBtn.setStyle("-fx-font-size: 12px; -fx-padding: 5 10;");
                payBtn.setOnAction(e -> handlePayment(getItem()));
            }

            @Override
            protected void updateItem(Reservation item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(payBtn);
                }
            }
        });
    }

    private void loadData(User user) {
        paymentsTable.setItems(FXCollections.observableArrayList(
                reservationRepository.findPendingPayments(user.getEmail())
        ));
    }

    private void handlePayment(Reservation reservation) {
        try {
            FXMLLoader loader = new FXMLLoader(com.example.parkupjavafx.ParkUpApplication.class.getResource("payment_confirmation.fxml"));
            javafx.scene.Parent root = loader.load();

            PaymentConfirmationController controller = loader.getController();
            controller.setReservation(reservation);

            javafx.stage.Stage stage = new javafx.stage.Stage();
            stage.setTitle("Payment Confirmation");
            stage.setScene(new javafx.scene.Scene(root));
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.showAndWait();

            // Refresh the table after window closes
            loadData(AuthService.getCurrentUser());

        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}