package com.evoting;
import com.evoting.service.*;
import java.util.Scanner;  

public class MainApp {
    public static void main(String[] args) {
        DatabaseService db = new DatabaseService();
        OtpService otp = new OtpService();
        VoteService vote = new VoteService();
        Scanner scanner = new Scanner(System.in);  

        System.out.println("=== E-Voting System ===");
        
        
        System.out.print("Enter Voter ID: ");
        String voterId = scanner.nextLine();

      
        if (db.getVoter(voterId) == null) {
            System.out.println("❌ Invalid voter ID!");
            return;
        }

        
        String generatedOtp = otp.generateOtp(voterId);
        System.out.println("📲 OTP sent to registered mobile: " + generatedOtp);

        
        System.out.print("Enter OTP: ");
        String userEnteredOtp = scanner.nextLine();

     
        if (otp.verifyOtp(voterId, userEnteredOtp)) {
         
            System.out.println("\nCandidates:");
            System.out.println("1. Candidate A");
            System.out.println("2. Candidate B");
            System.out.print("Select candidate (1/2): ");
            String candidate = scanner.nextLine();
            
            vote.recordVote(voterId, "Candidate " + candidate);
            System.out.println("✅ Vote recorded successfully!");
        } else {
            System.out.println("❌ Invalid OTP! Voting failed.");
        }
        
        scanner.close();  
    }
}