package com.evoting.service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OtpService {
    private static final Map<String, String> otpStorage = new HashMap<>();
    private static final Random random = new Random();

    public String generateOtp(String voterId) {
        String otp = String.format("%04d", random.nextInt(10000));
        otpStorage.put(voterId, otp);
        System.out.println("[MOCK] OTP for " + voterId + ": " + otp);
        return otp;
    }

    public boolean verifyOtp(String voterId, String otp) {
        return otp.equals(otpStorage.get(voterId));
    }
}