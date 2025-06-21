package com.evoting.controller;

import com.evoting.service.DatabaseService;
import com.evoting.service.OtpService;
import com.evoting.service.VoteService;
import com.evoting.model.Voter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class VotingController {

    private final OtpService otpService;
    private final DatabaseService databaseService;
    private final VoteService voteService;

    @Autowired
    public VotingController(OtpService otpService, DatabaseService databaseService, VoteService voteService) {
        this.otpService = otpService;
        this.databaseService = databaseService;
        this.voteService = voteService;
    }

    @GetMapping("/")
    public String showHomePage() {
        return "index"; // Show voter ID input page
    }

    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String voterId, Model model) {
        // Check if voter exists
        Voter voter = databaseService.getVoter(voterId);
        if (voter == null) {
            model.addAttribute("error", "Voter ID not found.");
            return "index";
        }

        // Generate and store OTP
        String otp = otpService.generateOtp(voterId);

        // Ideally, send OTP to voter's mobile here (not implemented)
        System.out.println("OTP for voter " + voterId + ": " + otp);

        model.addAttribute("voterId", voterId);
        model.addAttribute("otpSent", true);
        return "otp"; // Show OTP entry page
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String voterId, @RequestParam String otp, Model model) {
        boolean valid = otpService.verifyOtp(voterId, otp);

        if (!valid) {
            model.addAttribute("voterId", voterId);
            model.addAttribute("error", "Invalid or expired OTP. Please try again.");
            return "otp";
        }

        model.addAttribute("voterId", voterId);
        return "vote"; // Show voting page
    }

    @PostMapping("/vote")
    public String processVote(
            @RequestParam String voterId,
            @RequestParam String candidate,
            Model model
    ) {
        // Record vote
        boolean success = voteService.recordVote(voterId, candidate);

        if (!success) {
            model.addAttribute("error", "Could not record vote. Please try again.");
            model.addAttribute("voterId", voterId);
            return "vote";
        }

        model.addAttribute("message", "Vote recorded successfully for candidate: " + candidate);
        return "result"; // Show result page
    }
}
