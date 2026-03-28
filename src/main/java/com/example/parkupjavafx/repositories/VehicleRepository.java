package com.example.parkupjavafx.repositories;

import com.example.parkupjavafx.config.DatabaseConfig;
import com.example.parkupjavafx.models.Vehicle;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehicleRepository {

    public List<Vehicle> getAllAvailableVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();
        // Added image_path to selection just to be safe, though * selects all
        String query = "SELECT * FROM vehicle WHERE status != 'ARCHIVED'";

        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Vehicle vehicle = new Vehicle(
                        rs.getInt("Vehicle_Id"),
                        rs.getString("Vehicle_Model"),
                        rs.getInt("Vehicle_Year"),
                        rs.getString("Plate_Number"),
                        rs.getDouble("Rental_Price"),
                        rs.getInt("number_of_seats"),
                        rs.getString("vehicle_type"),
                        rs.getString("status"),
                        rs.getString("image_path") // Fetching the image path
                );
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public Vehicle getVehicleById(int vehicleId) {
        String query = "SELECT * FROM vehicle WHERE Vehicle_Id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, vehicleId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Vehicle(
                        rs.getInt("Vehicle_Id"),
                        rs.getString("Vehicle_Model"),
                        rs.getInt("Vehicle_Year"),
                        rs.getString("Plate_Number"),
                        rs.getDouble("Rental_Price"),
                        rs.getInt("number_of_seats"),
                        rs.getString("vehicle_type"),
                        rs.getString("status"),
                        rs.getString("image_path") // Fetching the image path
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}