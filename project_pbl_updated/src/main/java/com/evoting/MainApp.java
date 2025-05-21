package com.evoting;

import com.evoting.service.DatabaseService;
import com.evoting.service.OtpService;
import com.evoting.service.VoteService;

import java.sql.SQLException;
import java.util.Scanner;

public class MainApp {
    public static void main(String[] args) throws SQLException {
        DatabaseService db = new DatabaseService();
        OtpService otp = new OtpService();
        VoteService vote = new VoteService();
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("=== E-Voting System ===");
        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine();
        
        // Verify voter exists
        if (db.getVoter(voterId) == null) {
            System.out.println("❌ Voter not found!");
            return;
        }
        
        // Generate and send OTP
        String generatedOtp = otp.generateOtp(voterId);
        System.out.println("OTP sent to registered mobile number: " + generatedOtp);
        
        System.out.print("Enter OTP: ");
        String userOtp = scanner.nextLine();
        
        if (otp.verifyOtp(voterId, userOtp)) {
            System.out.println("\nCandidates:");
            System.out.println("1. Candidate A");
            System.out.println("2. Candidate B");
            System.out.print("Select candidate (1/2): ");
            String choice = scanner.nextLine();
            
            vote.recordVote(voterId, "Candidate " + choice);
            db.markAsVoted(voterId);
            System.out.println("✅ Vote recorded successfully!");
        } else {
            System.out.println("❌ Invalid OTP!");
        }
        
        scanner.close();
    }
}