package com.evoting.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.evoting.service.VoteService;

@RestController
@RequestMapping("/api/voting")
public class VotingRestController {

    public static class ApiResponse {
        private String status;
        private String message;

        // Add default constructor
        public ApiResponse() {}

        public ApiResponse(String status, String message) {
            this.status = status;
            this.message = message;
        }

        // Getters and setters
        public String getStatus() { return status; }
        public String getMessage() { return message; }
        public void setStatus(String status) { this.status = status; }
        public void setMessage(String message) { this.message = message; }
    }

    @PostMapping("/send-otp")
    public ResponseEntity<ApiResponse> sendOtp(@RequestParam String voterId) {
        // Implementation here
        return ResponseEntity.ok(new ApiResponse("success", "OTP sent"));
    }
    @RestController
    @RequestMapping("/api/votes")
    public class VoteRestController {

        private final VoteService voteService;

        @Autowired
        public VoteRestController(VoteService voteService) {
            this.voteService = voteService;
        }
        
        @GetMapping("/validate-blockchain")
        public ResponseEntity<VotingRestController.ApiResponse> validateBlockchain() {
            boolean isValid = voteService.validateBlockchainFromDatabase();

            if (isValid) {
                return ResponseEntity.ok(new ApiResponse("success", "Blockchain is valid and intact."));
            } else {
                return ResponseEntity.ok(new ApiResponse("error", "Blockchain has been tampered or corrupted."));
            }
        }


        @GetMapping("/results")
        public Map<String, Integer> getResults() {
            return voteService.getVoteCounts(); // This should run the MySQL GROUP BY query
        }
    }

}