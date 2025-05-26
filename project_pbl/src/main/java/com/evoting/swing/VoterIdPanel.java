package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.service.OtpService;

import javax.swing.*;
import java.awt.*;

public class VoterIdPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final OtpService otpService;
    private final JTextField voterIdField = new JTextField(15);
    private final JButton sendOtpButton = new JButton("Send OTP");
    private final JLabel statusLabel = new JLabel();

    public VoterIdPanel() {
        otpService = SpringContextBridge.getBean(OtpService.class);

        setLayout(new BorderLayout(10, 10));
        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));

        formPanel.add(new JLabel("Enter Voter ID:"));
        formPanel.add(voterIdField);
        formPanel.add(sendOtpButton);

        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        sendOtpButton.addActionListener(e -> sendOtp());
    }

    private void sendOtp() {
        String voterId = voterIdField.getText().trim();
        if (voterId.isEmpty()) {
            statusLabel.setText("Please enter your voter ID.");
            return;
        }

        String otp = otpService.generateOtp(voterId);
        if (otp != null) {
            statusLabel.setText("OTP sent: " + otp);
            // Switch to OTP verification panel
            cardLayout.show(mainPanel, "OtpVerificationPanel");

            // You can also pass the voterId to next panel if needed (e.g. via a shared model or method)
        } else {
            statusLabel.setText("Failed to send OTP.");
        }
    }

    public void setParentFrame(JFrame frame) {
        this.parentFrame = frame;
    }

    public void setMainPanel(JPanel mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setCardLayout(CardLayout cardLayout) {
        this.cardLayout = cardLayout;
    }
}
