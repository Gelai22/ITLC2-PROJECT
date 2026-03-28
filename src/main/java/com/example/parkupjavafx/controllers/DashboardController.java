package com.example.parkupjavafx.controllers;

import com.example.parkupjavafx.ParkUpApplication;
import com.example.parkupjavafx.models.User;
import com.example.parkupjavafx.models.Vehicle;
import com.example.parkupjavafx.repositories.VehicleRepository;
import com.example.parkupjavafx.services.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

public class DashboardController {

    @FXML private BorderPane mainBorderPane;
    @FXML private Label userNameLabel;
    @FXML private TextField searchField;
    @FXML private ComboBox<String> typeFilter;
    @FXML private ComboBox<String> priceFilter;
    @FXML private FlowPane vehicleContainer;

    private final VehicleRepository vehicleRepository = new VehicleRepository();
    private Node browseView;

    @FXML
    public void initialize() {
        User currentUser = AuthService.getCurrentUser();
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getName());
        }

        browseView = mainBorderPane.getCenter();

        typeFilter.getItems().addAll("All Types", "SEDAN", "SUV", "VAN", "COUPE", "TRUCK");
        typeFilter.getSelectionModel().selectFirst();

        priceFilter.getItems().addAll("All Prices", "Under 500", "500 - 1000", "Over 1000");
        priceFilter.getSelectionModel().selectFirst();

        loadVehicles(vehicleRepository.getAllAvailableVehicles());
    }

    private void loadVehicles(List<Vehicle> vehicles) {
        vehicleContainer.getChildren().clear();
        if (vehicles.isEmpty()) {
            Label noData = new Label("No vehicles found matching your criteria.");
            noData.setFont(Font.font("Arial", 16));
            noData.setTextFill(Color.GRAY);
            vehicleContainer.getChildren().add(noData);
        } else {
            for (Vehicle v : vehicles) {
                vehicleContainer.getChildren().add(createVehicleCard(v));
            }
        }
    }

    @FXML
    protected void onSearchClick() {
        applyAllFilters();
    }

    @FXML
    protected void onApplyFiltersClick() {
        applyAllFilters();
    }

    private void applyAllFilters() {
        String searchText = searchField.getText().toLowerCase().trim();
        String selectedType = typeFilter.getValue();
        String selectedPrice = priceFilter.getValue();

        List<Vehicle> allVehicles = vehicleRepository.getAllAvailableVehicles();

        List<Vehicle> filteredList = allVehicles.stream()
                .filter(v -> matchesSearch(v, searchText))
                .filter(v -> matchesType(v, selectedType))
                .filter(v -> matchesPrice(v, selectedPrice))
                .collect(Collectors.toList());

        loadVehicles(filteredList);
    }

    private boolean matchesSearch(Vehicle v, String searchText) {
        if (searchText.isEmpty()) return true;
        return v.getModel().toLowerCase().contains(searchText);
    }

    private boolean matchesType(Vehicle v, String selectedType) {
        if (selectedType == null || selectedType.equals("All Types")) return true;
        return v.getType().equalsIgnoreCase(selectedType);
    }

    private boolean matchesPrice(Vehicle v, String selectedPrice) {
        if (selectedPrice == null || selectedPrice.equals("All Prices")) return true;

        double price = v.getRentalPrice();

        switch (selectedPrice) {
            case "Under 500":
                return price < 500;
            case "500 - 1000":
                return price >= 500 && price <= 1000;
            case "Over 1000":
                return price > 1000;
            default:
                return true;
        }
    }

    private VBox createVehicleCard(Vehicle vehicle) {
        VBox card = new VBox(10);
        card.setPrefWidth(300);
        card.getStyleClass().add("card");
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-padding: 15;");

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(0, 0, 0, 0.1));
        shadow.setRadius(5);
        shadow.setOffsetY(2);
        card.setEffect(shadow);

        Label badge = new Label(vehicle.getStatus());
        badge.getStyleClass().add("badge");

        switch (vehicle.getStatus().toUpperCase()) {
            case "AVAILABLE":
                badge.getStyleClass().add("badge-success");
                break;
            case "RENTED":
            case "ARCHIVED":
                badge.getStyleClass().add("badge-danger");
                break;
            case "MAINTENANCE":
                badge.getStyleClass().add("badge-warning");
                break;
            default:
                badge.getStyleClass().add("badge-secondary");
                break;
        }

        ImageView imageView = new ImageView();
        Image image = loadImage(vehicle.getImagePath());
        imageView.setImage(image);

        imageView.setFitHeight(150);
        imageView.setFitWidth(270);
        imageView.setPreserveRatio(true);

        Label modelLabel = new Label(vehicle.getModel() + " (" + vehicle.getYear() + ")");
        modelLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));

        Label priceLabel = new Label("P" + String.format("%.2f", vehicle.getRentalPrice()) + " / day");
        priceLabel.setTextFill(Color.web("#0085db"));
        priceLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        Label detailsLabel = new Label(vehicle.getSeats() + " Seats | " + vehicle.getType());
        detailsLabel.setTextFill(Color.web("#707a82"));

        Button reserveBtn = new Button("Reserve Now");
        reserveBtn.setMaxWidth(Double.MAX_VALUE);
        reserveBtn.getStyleClass().add("btn-primary");

        if (!"AVAILABLE".equalsIgnoreCase(vehicle.getStatus())) {
            reserveBtn.setDisable(true);
            reserveBtn.setText("Unavailable");
        } else {
            reserveBtn.setOnAction(e -> {
                try {
                    FXMLLoader loader = new FXMLLoader(ParkUpApplication.class.getResource("reservation-dialog.fxml"));
                    Parent root = loader.load();

                    ReservationController controller = loader.getController();
                    controller.setVehicle(vehicle);

                    Stage stage = new Stage();
                    stage.setTitle("Book " + vehicle.getModel());
                    stage.setScene(new Scene(root));
                    stage.initModality(Modality.APPLICATION_MODAL);
                    stage.showAndWait();

                    onMyReservationsClick();

                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
        }

        card.getChildren().addAll(badge, imageView, modelLabel, priceLabel, detailsLabel, reserveBtn);
        return card;
    }

    private Image loadImage(String dbPath) {
        String placeholderPath = "/images/vehicles/placeholder.jpg";
        String packagePrefix = "/com/example/parkupjavafx";

        if (dbPath != null && !dbPath.trim().isEmpty()) {
            String cleanPath = dbPath.startsWith("/") ? dbPath : "/" + dbPath;

            try (InputStream is = getClass().getResourceAsStream(cleanPath)) {
                if (is != null) return new Image(is);
            } catch (Exception ignored) {}

            try (InputStream is = getClass().getResourceAsStream(packagePrefix + cleanPath)) {
                if (is != null) return new Image(is);
            } catch (Exception ignored) {}

            System.err.println("Image missing: " + cleanPath);
        }

        try (InputStream is = getClass().getResourceAsStream(placeholderPath)) {
            if (is != null) return new Image(is);
        } catch (Exception ignored) {}

        try (InputStream is = getClass().getResourceAsStream(packagePrefix + placeholderPath)) {
            if (is != null) return new Image(is);
        } catch (Exception ignored) {}

        return null;
    }

    private void loadView(String fxmlFileName) {
        try {
            FXMLLoader loader = new FXMLLoader(ParkUpApplication.class.getResource(fxmlFileName));
            Parent view = loader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onBrowseClick() {
        if (browseView != null) {
            mainBorderPane.setCenter(browseView);
            applyAllFilters();
        }
    }

    @FXML
    protected void onPendingReservationsClick() {
        loadView("pending_reservation.fxml");
    }

    @FXML
    protected void onPendingPaymentsClick() {
        loadView("pending_payments.fxml");
    }

    @FXML
    protected void onOngoingRentalsClick() {
        loadView("ongoing_rentals.fxml");
    }

    @FXML
    protected void onRentalHistoryClick() {
        loadView("rental_history.fxml");
    }

    @FXML
    protected void onMyReservationsClick() {
        onPendingReservationsClick();
    }

    @FXML
    protected void onLogoutClick() throws IOException {
        AuthService.logout();
        Stage stage = (Stage) userNameLabel.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(ParkUpApplication.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 850);
        stage.setScene(scene);
    }
}