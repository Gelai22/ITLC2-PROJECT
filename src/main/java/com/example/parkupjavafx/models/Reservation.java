package com.example.parkupjavafx.models;

import java.time.LocalDate;

public class Reservation {
    private int id;
    private int vehicleId;
    private String clientName; // Or create a Client object link
    private LocalDate pickupDate;
    private LocalDate returnDate;
    private double totalCost;
    private String status;
    private String notes;

    public Reservation(int id, int vehicleId, String clientName, LocalDate pickupDate, LocalDate returnDate, double totalCost, String status, String notes) {
        this.id = id;
        this.vehicleId = vehicleId;
        this.clientName = clientName;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.totalCost = totalCost;
        this.status = status;
        this.notes = notes;
    }

    public int getId() { return id; }
    public int getVehicleId() { return vehicleId; }
    public String getClientName() { return clientName; }
    public LocalDate getPickupDate() { return pickupDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public double getTotalCost() { return totalCost; }
    public String getStatus() { return status; }
    public String getNotes() { return notes; }
}