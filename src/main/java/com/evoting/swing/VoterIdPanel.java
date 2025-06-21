package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.service.OtpService;

import javax.swing.*;
import java.awt.*;

public class VoterIdPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private OtpVerificationPanel otpVerificationPanel; // 🔧 Add this reference

    private final OtpService otpService;
    private final JTextField voterIdField = new JTextField(15);
    private final JButton sendOtpButton = new JButton("Send OTP");
    private final JLabel statusLabel = new JLabel();
    
    private JButton createBackButton(JPanel mainPanel, CardLayout cardLayout) {
        JButton backButton = new JButton("⬅ Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.CYAN);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DashboardPanel"));
        return backButton;
    }


    public VoterIdPanel() {
        otpService = SpringContextBridge.getBean(OtpService.class);

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(34, 34, 34));

        JLabel titleLabel = new JLabel("Enter Student ID to Vote");
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(34, 34, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel voterIdLabel = new JLabel("Student ID:");
        voterIdLabel.setForeground(Color.LIGHT_GRAY);
        voterIdLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        voterIdField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        voterIdField.setBackground(new Color(25, 25, 25));
        voterIdField.setForeground(Color.WHITE);
        voterIdField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        sendOtpButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        sendOtpButton.setBackground(new Color(45, 45, 48));
        sendOtpButton.setForeground(Color.WHITE);
        sendOtpButton.setFocusPainted(false);
        sendOtpButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(34, 34, 34));
        backPanel.add(createBackButton(mainPanel, cardLayout));
        add(backPanel, BorderLayout.SOUTH);  // or NORTH, depending on layout


        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(voterIdLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.7;
        formPanel.add(voterIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        formPanel.add(sendOtpButton, gbc);

        add(formPanel, BorderLayout.CENTER);

        statusLabel.setForeground(Color.ORANGE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.SOUTH);

        sendOtpButton.addActionListener(e -> sendOtp());
    }

    private void sendOtp() {
        String voterId = voterIdField.getText().trim();
        if (voterId.isEmpty()) {
            statusLabel.setText("Please enter your Student ID.");
            return;
        }

        String otp = otpService.generateOtp(voterId);
        if (otp != null) {
            statusLabel.setText("OTP sent: " + otp);
            statusLabel.setText("OTP sent successfully. You can resend if needed.");
            sendOtpButton.setEnabled(false);
            otpVerificationPanel.reset();
            Timer timer = new Timer(30000, e -> sendOtpButton.setEnabled(true));
            timer.setRepeats(false); // Run only once
            timer.start();
            

            // ✅ SAFELY PASS OTP PANEL
            if (otpVerificationPanel != null) {
                otpVerificationPanel.setVoterId(voterId);
                cardLayout.show(mainPanel, "OtpVerificationPanel");
            } else {
                statusLabel.setText("OtpVerificationPanel not set.");
            }
        } else {
            statusLabel.setText("Failed to send OTP.");
        }
    }
    
    public void reset() {
        voterIdField.setText("");
        statusLabel.setText("");
       
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

    // 🔧 Setter to link OtpVerificationPanel
    public void setOtpVerificationPanel(OtpVerificationPanel otpVerificationPanel) {
        this.otpVerificationPanel = otpVerificationPanel;
    }
}
