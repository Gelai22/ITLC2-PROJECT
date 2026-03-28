package com.example.carrental.Models.admin;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.math.BigDecimal;

@Entity(name = "maintenance_log")
public class MaintenanceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Maintenance_Id")
    private Long id;

    // Link to the Vehicle being serviced
    @Column(name = "Vehicle_Id")
    private Long vehicleId;

    private String serviceType;
    private String notes;
    private LocalDate dateOut;
    private LocalDate dateBackEstimate;
    private LocalDate dateBackActual; // For when service is completed

    @Column(precision = 10, scale = 2)
    private BigDecimal estimatedCost;

    private String status = "IN_PROGRESS"; // IN_PROGRESS, COMPLETED

    // --- Constructors and Getters/Setters ---
    public MaintenanceModel() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    public LocalDate getDateOut() { return dateOut; }
    public void setDateOut(LocalDate dateOut) { this.dateOut = dateOut; }

    public LocalDate getDateBackEstimate() { return dateBackEstimate; }
    public void setDateBackEstimate(LocalDate dateBackEstimate) { this.dateBackEstimate = dateBackEstimate; }

    public LocalDate getDateBackActual() { return dateBackActual; }
    public void setDateBackActual(LocalDate dateBackActual) { this.dateBackActual = dateBackActual; }

    public BigDecimal getEstimatedCost() { return estimatedCost; }
    public void setEstimatedCost(BigDecimal estimatedCost) { this.estimatedCost = estimatedCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}