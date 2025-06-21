package com.evoting.service;
import com.twilio.Twilio;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;

import org.springframework.stereotype.Service;

@Service
public class SmsService {

    private final String ACCOUNT_SID = "AC90bb15d1fc0428aa1036bdffef2e41aa";
    private final String AUTH_TOKEN = "dc35eb4f9ea63243be83c026ff811cbd";
    private final String VERIFY_SERVICE_SID = "VA16cad214c0054614af544ea0a0c0a89a";

    public SmsService() {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }

    public void sendOtp(String mobileNumber) {
        Verification verification = Verification.creator(
                VERIFY_SERVICE_SID,
                mobileNumber,
                "sms"
        ).create();

        System.out.println("OTP Status: " + verification.getStatus());
    }
    public boolean verifyOtp(String mobileNumber, String code) {
        VerificationCheck verificationCheck = VerificationCheck.creator(VERIFY_SERVICE_SID, code)
                .setTo(mobileNumber)
                .create();

        return "approved".equalsIgnoreCase(verificationCheck.getStatus());
    }
}

