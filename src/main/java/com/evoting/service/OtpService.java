package com.evoting.service;

import com.evoting.model.Voter;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import org.springframework.stereotype.Service;

@Service
public class OtpService {

	private final String ACCOUNT_SID = "AC90bb15d1fc0428aa1036bdffef2e41aa";
    private final String AUTH_TOKEN = "dc35eb4f9ea63243be83c026ff811cbd";
    private final String SERVICE_SID = "VA16cad214c0054614af544ea0a0c0a89a";

    private final DatabaseService databaseService;

    public OtpService(DatabaseService databaseService) {
        this.databaseService = databaseService;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }


    // Sends OTP SMS via Twilio Verify
    public String generateOtp(String stdId) {
        Voter voter = databaseService.getVoter(stdId);

        if (voter == null) {
            throw new IllegalArgumentException("Voter not found for ID: " + stdId);
        }

        String phoneNumber = voter.getMobile();  // must be in format like +91XXXXXXXXXX

        Verification verification = Verification.creator(
            SERVICE_SID,
            phoneNumber,
            "sms"
        ).create();

        return verification.getStatus();  // usually "pending"
    }

    // Verifies OTP using voter's phone number from DB
    public boolean verifyOtp(String stdId, String code) {
        Voter voter = databaseService.getVoter(stdId);

        if (voter == null) {
            throw new IllegalArgumentException("Voter not found for ID: " + stdId);
        }

        String phoneNumber = voter.getMobile();

        VerificationCheck verificationCheck = VerificationCheck.creator(
            SERVICE_SID,
            code
        ).setTo(phoneNumber).create();

        return "approved".equals(verificationCheck.getStatus());
    }
}