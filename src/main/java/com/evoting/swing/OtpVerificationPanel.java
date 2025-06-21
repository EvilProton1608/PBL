package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.model.Voter;
import com.evoting.service.DatabaseService;
import com.evoting.service.OtpService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

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

    private VotingPanel votingPanel;
    private String currentVoterId;

    private Timer cooldownTimer;
    private int cooldownSecondsLeft = 30;

    public OtpVerificationPanel() {
        otpService = SpringContextBridge.getBean(OtpService.class);
        databaseService = SpringContextBridge.getBean(DatabaseService.class);

        setLayout(new BorderLayout(15, 15));
        setBackground(new Color(34, 34, 34));

        JLabel titleLabel = new JLabel("Verify OTP");
        titleLabel.setForeground(Color.LIGHT_GRAY);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(titleLabel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(34, 34, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel otpLabel = new JLabel("Enter OTP:");
        otpLabel.setForeground(Color.LIGHT_GRAY);
        otpLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        otpField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        otpField.setBackground(new Color(25, 25, 25));
        otpField.setForeground(Color.WHITE);
        otpField.setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));

        verifyButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        verifyButton.setBackground(new Color(45, 45, 48));
        verifyButton.setForeground(Color.WHITE);
        verifyButton.setFocusPainted(false);
        verifyButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(34, 34, 34));
        backPanel.add(createBackButton());
        add(backPanel, BorderLayout.SOUTH);

        voterInfoLabel.setForeground(Color.LIGHT_GRAY);
        voterInfoLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        voterInfoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.3;
        formPanel.add(otpLabel, gbc);

        gbc.gridx = 1;
        formPanel.add(otpField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        formPanel.add(verifyButton, gbc);

        gbc.gridy = 2;
        formPanel.add(voterInfoLabel, gbc);

        add(formPanel, BorderLayout.CENTER);

        statusLabel.setForeground(Color.ORANGE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        add(statusLabel, BorderLayout.SOUTH);

        verifyButton.addActionListener(e -> verifyOtp());
    }

    private JButton createBackButton() {
        JButton backButton = new JButton("⬅ Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.CYAN);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            stopCooldownIfRunning();
            cardLayout.show(mainPanel, "DashboardPanel");
        });
        return backButton;
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
            stopCooldownIfRunning();
            statusLabel.setText("OTP verified!");
            otpField.setEnabled(false);
            verifyButton.setEnabled(false);

            Voter voter = databaseService.getVoter(currentVoterId);
            if (voter != null) {
                voterInfoLabel.setText("Name: " + voter.getName() + ", Mobile: " + voter.getMobile());

                Timer timer = new Timer(1500, e -> {
                    votingPanel.setVoter(voter);
                    cardLayout.show(mainPanel, "VotingPanel");
                });
                timer.setRepeats(false);
                timer.start();
            } else {
                voterInfoLabel.setText("Voter info not found.");
            }
        } else {
            // ❌ Incorrect OTP — trigger 30s cooldown
            statusLabel.setText("Invalid OTP. Please wait before retrying.");
            verifyButton.setEnabled(false);
            cooldownSecondsLeft = 30;
            verifyButton.setText("Retry in 30s");

            stopCooldownIfRunning(); // Reset existing timer
            cooldownTimer = new Timer(1000, new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cooldownSecondsLeft--;
                    verifyButton.setText("Retry in " + cooldownSecondsLeft + "s");

                    if (cooldownSecondsLeft <= 0) {
                        cooldownTimer.stop();
                        verifyButton.setText("Verify OTP");
                        verifyButton.setEnabled(true);
                    }
                }
            });
            cooldownTimer.start();
        }
    }

    private void stopCooldownIfRunning() {
        if (cooldownTimer != null && cooldownTimer.isRunning()) {
            cooldownTimer.stop();
        }
    }

    public void reset() {
        stopCooldownIfRunning();
        otpField.setText("");
        otpField.setEnabled(true);
        verifyButton.setText("Verify OTP");
        verifyButton.setEnabled(true);
        statusLabel.setText("");
        voterInfoLabel.setText("");
    }

    public void setVotingPanel(VotingPanel votingPanel) {
        this.votingPanel = votingPanel;
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
