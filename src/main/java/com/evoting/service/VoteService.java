package com.evoting.service;

import com.evoting.blockchain.Block;
import com.evoting.blockchain.Blockchain;
import com.evoting.db.DatabaseConnection;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

@Service
public class VoteService {

    private final DatabaseService databaseService;
    private final Blockchain blockchain;
    private final DataSource dataSource; // ✅ Injected from Spring Boot

    public VoteService(DatabaseService databaseService, DataSource dataSource) throws SQLException {
        this.databaseService = databaseService;
        this.blockchain = new Blockchain();
        this.dataSource = dataSource; // ✅ Save injected datasource
    }

    public boolean recordVote(String voterId, String candidate) {
        try {
            blockchain.addBlock(voterId, candidate);
            String voteHash = blockchain.getLatestBlock().hash;

            String sql = "INSERT INTO votes (std_id, candidate, hash, has_voted) VALUES (?, ?, ?, TRUE)";


            try (Connection conn = dataSource.getConnection(); // ✅ Use Spring-managed connection
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, voterId);
                pstmt.setString(2, candidate);
                pstmt.setString(3, voteHash);

                int affectedRows = pstmt.executeUpdate();

                if (affectedRows > 0) {
                    databaseService.markAsVoted(voterId);
                    return true;
                }

            }

        } catch (Exception e) {
            System.err.println("Error recording vote with blockchain: " + e.getMessage());
        }

        return false;
    }
    public boolean hasAlreadyVoted(String voterId) {
        String sql = "SELECT has_voted FROM votes WHERE std_id = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, voterId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean("has_voted"); // true if voter has already voted
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }


    public boolean isBlockchainValid() {
        return blockchain.isChainValid();
    }

    public Map<String, Integer> getVoteCounts() {
        Map<String, Integer> voteCounts = new LinkedHashMap<>();

        String sql = "SELECT candidate, COUNT(*) AS votes FROM votes GROUP BY candidate";

        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String candidate = rs.getString("candidate");
                int count = rs.getInt("votes");
                voteCounts.put(candidate, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return voteCounts;
    }

	public boolean validateBlockchainFromDatabase() {
    List<Block> blocksFromDb = new ArrayList<>();

    String sql = "SELECT std_id, candidate, hash FROM votes ORDER BY id ASC"; // Ensure proper order

    try (Connection conn = dataSource.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {

        String previousHash = "0";

        while (rs.next()) {
            String voterId = rs.getString("std_id");
            String candidate = rs.getString("candidate");
            String storedHash = rs.getString("hash");

            Block block = new Block(voterId, candidate, previousHash);

            if (!block.hash.equals(storedHash)) {
                System.err.println("Hash mismatch for voter " + voterId);
                return false;
            }

            previousHash = storedHash; // move to next block
        }

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }

    return true;
}
	public String getLastBlockHashForVoter(String voterId) {
	    String sql = "SELECT hash FROM votes WHERE std_id = ? ORDER BY id DESC LIMIT 1";

	    try (Connection conn = dataSource.getConnection();
	         PreparedStatement stmt = conn.prepareStatement(sql)) {

	        stmt.setString(1, voterId);

	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getString("hash");
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return null;
	}


}
