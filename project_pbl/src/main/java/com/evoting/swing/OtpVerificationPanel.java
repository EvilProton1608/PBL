package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.service.DatabaseService;
import com.evoting.service.OtpService;

import javax.swing.*;
import java.awt.*;

public class OtpVerificationPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final OtpService otpService;
    private final DatabaseService databaseService;

    private final JTextField otpField = new JTextField(10);
    private final JButton verifyButton = new JButton("Verify OTP");
    private final JLabel statusLabel = new JLabel();
    private final JLabel voterInfoLabel = new JLabel();

    private String currentVoterId; // store voter ID here after OTP send

    public OtpVerificationPanel() {
        otpService = SpringContextBridge.getBean(OtpService.class);
        databaseService = SpringContextBridge.getBean(DatabaseService.class);

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.add(new JLabel("Enter OTP:"));
        formPanel.add(otpField);
        formPanel.add(verifyButton);
        formPanel.add(voterInfoLabel);

        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        verifyButton.addActionListener(e -> verifyOtp());
    }

    public void setVoterId(String voterId) {
        this.currentVoterId = voterId;
    }

    private void verifyOtp() {
        String otp = otpField.getText().trim();
        if (otp.isEmpty()) {
            statusLabel.setText("Please enter OTP.");
            return;
        }

        boolean valid = otpService.verifyOtp(currentVoterId, otp);
        if (valid) {
            statusLabel.setText("OTP verified!");

            // Fetch voter info and show
            var voter = databaseService.getVoter(currentVoterId);
            if (voter != null) {
                voterInfoLabel.setText("Name: " + voter.getName() + ", Mobile: " + voter.getMobile());
            } else {
                voterInfoLabel.setText("Voter info not found.");
            }

            // Proceed to voting panel after short delay or with a button (or you can auto-switch)
            cardLayout.show(mainPanel, "VotingPanel");

        } else {
            statusLabel.setText("Invalid OTP. Please try again.");
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
