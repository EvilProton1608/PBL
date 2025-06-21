package com.evoting.service;

import com.evoting.db.DatabaseConnection;
import com.evoting.model.Voter;

import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class DatabaseService {
    private Connection conn;

    public DatabaseService() throws SQLException {
        this.conn = DatabaseConnection.getConnection();
    }

    public Voter getVoter(String stdId) {
        String sql = "SELECT std_id, Name, mobile, age, dob, city, state, pincode, image_path FROM id WHERE std_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stdId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
            	return new Voter(
            		    rs.getString("std_id"),
            		    rs.getString("Name"),
            		    rs.getString("mobile"),
            		    rs.getString("age"),
            		    rs.getString("dob"),
            		    rs.getString("city"),
            		    rs.getString("state"),
            		    rs.getString("pincode"),
            		    sql, rs.getBytes("image_path")   
            		);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching voter: " + e.getMessage());
        }
        return null;
    }

    public void markAsVoted(String stdId) {
        String sql = "UPDATE id SET has_voted = TRUE WHERE `std_id` = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, stdId);
            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated == 0) {
                System.err.println("No voter found with ID: " + stdId);
            } else {
                System.out.println("Voter " + stdId + " successfully marked as voted");
            }
        } catch (SQLException e) {
            System.err.println("Error updating voting status: " + e.getMessage());
        }
    }
}
