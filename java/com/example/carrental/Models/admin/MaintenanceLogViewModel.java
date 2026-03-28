package com.example.carrental.Models.admin;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) used to send Maintenance Log data plus Vehicle details to the view.
 */
public class MaintenanceLogViewModel {

    // Fields from MaintenanceModel
    private Long id;
    private String serviceType;
    private LocalDate dateOut;
    private LocalDate dateBackEstimate;
    private BigDecimal estimatedCost;
    private String status;

    // CRITICAL FIX: ADD THE MISSING FIELD
    private LocalDate dateBackActual;

    // Necessary field from VehicleModel
    private String vehiclePlateNumber;

    // Constructor to easily map from Entity
    public MaintenanceLogViewModel(MaintenanceModel log, String plateNumber) {
        this.id = log.getId();
        this.serviceType = log.getServiceType();
        this.dateOut = log.getDateOut();
        this.dateBackEstimate = log.getDateBackEstimate();
        this.estimatedCost = log.getEstimatedCost();
        this.status = log.getStatus();

        // CRITICAL FIX: Ensure the field is mapped in the constructor
        this.dateBackActual = log.getDateBackActual();

        this.vehiclePlateNumber = plateNumber;
    }

    // --- Getters ---
    public Long getId() { return id; }
    public String getServiceType() { return serviceType; }
    public LocalDate getDateOut() { return dateOut; }
    public LocalDate getDateBackEstimate() { return dateBackEstimate; }
    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public String getStatus() { return status; }
    public String getVehiclePlateNumber() { return vehiclePlateNumber; }

    // CRITICAL FIX: Add the getter for the new field
    public LocalDate getDateBackActual() { return dateBackActual; }
}