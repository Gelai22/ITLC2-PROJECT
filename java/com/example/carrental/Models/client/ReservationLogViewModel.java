package com.example.carrental.Models.client;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ReservationLogViewModel {

    private Long reservationId;
    private LocalDateTime pickupDate;
    private LocalDateTime returnDate;
    private BigDecimal totalCost;
    private String status;
    private String rating; // Renamed
    private String clientFeedback; // Added

    private String vehicleModel;
    private Integer vehicleYear;
    private String plateNumber;

    public ReservationLogViewModel(ReservationModel reservation, String vehicleModel,
                                   Integer vehicleYear, String plateNumber) {
        this.reservationId = reservation.getId();
        this.pickupDate = reservation.getPickupDate();
        this.returnDate = reservation.getReturnDate();
        this.totalCost = reservation.getTotalCost();
        this.status = reservation.getStatus();
        this.rating = reservation.getRating(); // Updated getter
        this.clientFeedback = reservation.getClientFeedback(); // Updated getter

        this.vehicleModel = vehicleModel;
        this.vehicleYear = vehicleYear;
        this.plateNumber = plateNumber;
    }

    public Long getReservationId() { return reservationId; }
    public void setReservationId(Long reservationId) { this.reservationId = reservationId; }

    public LocalDateTime getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDateTime pickupDate) { this.pickupDate = pickupDate; }

    public LocalDateTime getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDateTime returnDate) { this.returnDate = returnDate; }

    public BigDecimal getTotalCost() { return totalCost; }
    public void setTotalCost(BigDecimal totalCost) { this.totalCost = totalCost; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRating() { return rating; }
    public void setRating(String rating) { this.rating = rating; }

    public String getClientFeedback() { return clientFeedback; }
    public void setClientFeedback(String clientFeedback) { this.clientFeedback = clientFeedback; }

    public String getVehicleModel() { return vehicleModel; }
    public void setVehicleModel(String vehicleModel) { this.vehicleModel = vehicleModel; }

    public Integer getVehicleYear() { return vehicleYear; }
    public void setVehicleYear(Integer vehicleYear) { this.vehicleYear = vehicleYear; }

    public String getPlateNumber() { return plateNumber; }
    public void setPlateNumber(String plateNumber) { this.plateNumber = plateNumber; }
}