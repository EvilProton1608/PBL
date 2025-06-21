package com.evoting.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OtpControllerService {

    @Autowired
    private OtpService otpService;

    @Autowired
    private SmsService smsService;

    public void sendOtpToVoter(String voterId, String mobileNumber) {
        otpService.generateOtp(voterId); // No need to send manually, Twilio handles OTP message
        smsService.sendOtp(mobileNumber); // ✅ FIXED
    }
}
