package com.evoting.service;

import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class SmsService {

    public void sendSms(String mobileNumber, String message) {
        try {
            String apiKey = "YOUR_API_KEY";  // Replace with actual API key
            String senderId = "YOUR_SENDER_ID";
            String urlStr = "https://api.example.com/send?apikey=" + apiKey;

            String payload = "sender=" + senderId +
                             "&number=" + mobileNumber +
                             "&message=" + message;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes(StandardCharsets.UTF_8));
            }

            int responseCode = conn.getResponseCode();
            System.out.println("SMS sent. Response Code: " + responseCode);

        } catch (Exception e) {
            System.err.println("Error sending SMS: " + e.getMessage());
        }
    }
}
