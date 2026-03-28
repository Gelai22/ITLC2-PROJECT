package com.example.parkupjavafx.repositories;

import com.example.parkupjavafx.config.DatabaseConfig;
import com.example.parkupjavafx.models.Reservation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationRepository {

    public boolean createReservation(Reservation reservation, int customerId) {
        String query = "INSERT INTO reservation (Reservation_Date, Pickup_Location, Pickup_Date, " +
                "Return_Date, Total_Cost, Vehicle_Id, Customer_Id, return_location, status) " +
                "VALUES (NOW(), ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, "MAIN_OFFICE");
            pstmt.setDate(2, Date.valueOf(reservation.getPickupDate()));
            pstmt.setDate(3, Date.valueOf(reservation.getReturnDate()));
            pstmt.setDouble(4, reservation.getTotalCost());
            pstmt.setInt(5, reservation.getVehicleId());
            pstmt.setInt(6, customerId);
            pstmt.setString(7, "MAIN_OFFICE");
            pstmt.setString(8, "PENDING");

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean confirmPayment(int reservationId) {
        String query = "UPDATE reservation SET status='CONFIRMED', payment_status='PAID', Payment_Date=NOW() WHERE Reservation_Id=?";
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, reservationId);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean processReturn(int reservationId, int vehicleId) {
        String updateReservation = "UPDATE reservation SET status='COMPLETED' WHERE Reservation_Id=?";
        String updateVehicle = "UPDATE vehicle SET status='AVAILABLE' WHERE Vehicle_Id=?";

        try (Connection conn = DatabaseConfig.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement psRes = conn.prepareStatement(updateReservation);
                 PreparedStatement psVeh = conn.prepareStatement(updateVehicle)) {

                psRes.setInt(1, reservationId);
                psRes.executeUpdate();

                psVeh.setInt(1, vehicleId);
                psVeh.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean submitReview(int reservationId, int rating, String feedback) {
        String query = "UPDATE reservation SET status='AWAITING_ADMIN_FINALIZATION', Rating=?, client_feedback=? WHERE Reservation_Id=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, rating);
            pstmt.setString(2, feedback);
            pstmt.setInt(3, reservationId);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reservation> findByCustomerEmail(String email) {
        return fetchReservations(email, "WHERE c.Email_Address=?");
    }

    public List<Reservation> findPendingPayments(String email) {
        return fetchReservations(email, "WHERE c.Email_Address=? AND r.status='PENDING_PAYMENT'");
    }

    public List<Reservation> findOngoingRentals(String email) {
        return fetchReservations(email, "WHERE c.Email_Address=? AND r.status IN ('CONFIRMED', 'RENTED', 'ONGOING')");
    }

    public List<Reservation> findRentalHistory(String email) {
        return fetchReservations(email, "WHERE c.Email_Address=? AND r.status IN ('COMPLETED', 'CANCELLED', 'RETURNED', 'AWAITING_ADMIN_FINALIZATION')");
    }

    private List<Reservation> fetchReservations(String email, String whereClause) {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT r.*, c.customer_name FROM reservation r " +
                "JOIN customer c ON r.Customer_Id = c.Customer_Id " +
                whereClause;

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Reservation reservation = new Reservation(
                        rs.getInt("Reservation_Id"),
                        rs.getInt("Vehicle_Id"),
                        rs.getString("customer_name"),
                        rs.getDate("Pickup_Date").toLocalDate(),
                        rs.getDate("Return_Date").toLocalDate(),
                        rs.getDouble("Total_Cost"),
                        rs.getString("status"),
                        ""
                );
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }
}