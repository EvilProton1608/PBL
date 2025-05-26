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
        String otp = otpService.generateOtp(voterId);
        String message = "Your OTP for voting is: " + otp;
        smsService.sendSms(mobileNumber, message);
    }
}
