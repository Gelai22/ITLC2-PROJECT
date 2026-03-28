package com.example.parkupjavafx.repositories;

import com.example.parkupjavafx.config.DatabaseConfig;
import com.example.parkupjavafx.models.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class UserRepository {

    public User authenticate(String email, String password) {
        String query = "SELECT * FROM customer WHERE Email_Address=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("Customer_Password");

                if (BCrypt.checkpw(password, storedHash)) {
                    return new User(
                            rs.getInt("Customer_Id"),
                            rs.getString("customer_name"),
                            rs.getString("Email_Address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean register(String name, String email, String password) {
        String query = "INSERT INTO customer (customer_name, Email_Address, Customer_Password) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setString(3, hashedPassword);

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean emailExists(String email) {
        String query = "SELECT COUNT(*) FROM customer WHERE Email_Address=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public int getCustomerId(String email) {
        String query = "SELECT Customer_Id FROM customer WHERE Email_Address=?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Customer_Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
}