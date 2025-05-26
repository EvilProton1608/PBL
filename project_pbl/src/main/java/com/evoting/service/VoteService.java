package com.evoting.service;

import com.evoting.db.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.stereotype.Service;

@Service
public class VoteService {

    private final DatabaseService databaseService;

    // Constructor injection
    public VoteService(DatabaseService databaseService) {
        this.databaseService = databaseService;
    }

    public boolean recordVote(String voterId, String candidate) {
        String sql = "INSERT INTO votes (voter_id, candidate) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, voterId);
            pstmt.setString(2, candidate);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows > 0) {
                // Mark voter as voted
                databaseService.markAsVoted(voterId);
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            System.err.println("Error recording vote: " + e.getMessage());
            return false;
        }
    }
}
