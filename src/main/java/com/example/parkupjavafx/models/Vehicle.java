package com.example.parkupjavafx.models;

public class Vehicle {
    private int id;
    private String model;
    private int year;
    private String plateNumber;
    private double rentalPrice;
    private int seats;
    private String type;
    private String status;
    private String imagePath; // Added this field

    // Updated constructor to accept imagePath
    public Vehicle(int id, String model, int year, String plateNumber, double rentalPrice, int seats, String type, String status, String imagePath) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.plateNumber = plateNumber;
        this.rentalPrice = rentalPrice;
        this.seats = seats;
        this.type = type;
        this.status = status;
        this.imagePath = imagePath;
    }

    public int getId() { return id; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public String getPlateNumber() { return plateNumber; }
    public double getRentalPrice() { return rentalPrice; }
    public int getSeats() { return seats; }
    public String getType() { return type; }
    public String getStatus() { return status; }
    public String getImagePath() { return imagePath; } // Added getter
}