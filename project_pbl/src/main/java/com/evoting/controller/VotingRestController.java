package com.evoting.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}