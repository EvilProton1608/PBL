package com.evoting.service;

import com.evoting.db.DatabaseConnection;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.Random;

@Service
public class OtpService {
    private static final Random random = new Random();

    public String generateOtp(String voterId) {
        String otp = String.format("%04d", random.nextInt(10000));

        String sql = "UPDATE id SET otp = ?, otp_expiry = DATE_ADD(NOW(), INTERVAL 5 MINUTE) WHERE std_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, otp);
            pstmt.setString(2, voterId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error storing OTP: " + e.getMessage());
        }

        return otp;
    }

    public boolean verifyOtp(String voterId, String otp) {
        String sql = "SELECT 1 FROM id WHERE std_id = ? AND otp = ? AND otp_expiry > NOW()";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voterId);
            pstmt.setString(2, otp);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            System.err.println("Error verifying OTP: " + e.getMessage());
            return false;
        }
    }
}
