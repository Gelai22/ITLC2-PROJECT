package com.example.carrental.Dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public class ReservationDTO {

    @NotNull
    private LocalDate pickupDate;

    @NotNull
    private LocalDate returnDate;

    @NotNull
    private Long vehicleId;

    @NotNull
    @Size(min = 1)
    private String pickupLocation;

    @NotNull
    @Size(min = 1)
    private String returnLocation;

    private String vehicleType;

    private String notes;

    public LocalDate getPickupDate() { return pickupDate; }
    public void setPickupDate(LocalDate pickupDate) { this.pickupDate = pickupDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getReturnLocation() { return returnLocation; }
    public void setReturnLocation(String returnLocation) { this.returnLocation = returnLocation; }

    public String getVehicleType() { return vehicleType; }
    public void setVehicleType(String vehicleType) { this.vehicleType = vehicleType; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}