package com.evoting.swing;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {

    private JLabel locationDisplayLabel;
    private JLabel nameLabel;
    private JLabel ageLabel;
    private JLabel dobLabel;
    private JLabel cityLabel;
    private JLabel stateLabel;
    private JLabel statusLabel;
    private JLabel hashLabel;

    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final JButton logoutButton = new JButton("Logout");
    private final JButton loginButton = new JButton("Login");
    private final JButton checkBlockchainButton = new JButton("Check Blockchain");
    private final JButton showResultsButton = new JButton("Show Results");
    

    public DashboardPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(30, 30, 30));

        // === TOP PANEL ===
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(30, 30, 30));

        locationDisplayLabel = new JLabel("Location: Not set");
        locationDisplayLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        locationDisplayLabel.setForeground(Color.LIGHT_GRAY);
        locationDisplayLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 5));

        JLabel title = new JLabel("E-Voting Dashboard", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));

        topPanel.add(locationDisplayLabel, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        // === VOTER INFO PANEL ===
        JPanel infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(new Color(30, 30, 30));
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.DARK_GRAY),
                "Voter Information", 0, 0,
                new Font("Segoe UI", Font.BOLD, 14), Color.CYAN
        ));

        nameLabel = createInfoLabel();
        ageLabel = createInfoLabel();
        dobLabel = createInfoLabel();
        cityLabel = createInfoLabel();
        stateLabel = createInfoLabel();
        statusLabel = createInfoLabel();
        hashLabel = createInfoLabel();

        infoPanel.add(nameLabel);
        infoPanel.add(ageLabel);
        infoPanel.add(dobLabel);
        infoPanel.add(cityLabel);
        infoPanel.add(stateLabel);
        infoPanel.add(statusLabel);
        infoPanel.add(hashLabel);

        add(infoPanel, BorderLayout.CENTER);

        // === BUTTON PANEL ===
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(new Color(30, 30, 30));
        buttonPanel.add(loginButton);
        buttonPanel.add(checkBlockchainButton);
        buttonPanel.add(showResultsButton);
        buttonPanel.add(logoutButton);

        add(buttonPanel, BorderLayout.SOUTH);

        // === Button Styling ===
        Font buttonFont = new Font("Segoe UI", Font.BOLD, 14);
        Color bgColor = new Color(25, 25, 25);
        Color fgColor = Color.CYAN;

        for (JButton button : new JButton[]{logoutButton, loginButton, checkBlockchainButton, showResultsButton}) {
            button.setFont(buttonFont);
            button.setBackground(bgColor);
            button.setForeground(fgColor);
            button.setFocusPainted(false);
            button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        }

        // === Button Actions ===

        loginButton.addActionListener(e -> {
            if (mainPanel != null && cardLayout != null) {
                cardLayout.show(mainPanel, "VoterIdPanel");
                
            }
        });

        logoutButton.addActionListener(e -> {
            clearDashboardInfo();

            JOptionPane.showMessageDialog(this, "You have been logged out.", "Logout", JOptionPane.INFORMATION_MESSAGE);

            if (mainPanel != null && cardLayout != null) {
                cardLayout.show(mainPanel, "LocationSelectionPanel");
            }
        });

        checkBlockchainButton.addActionListener(e -> {
            boolean isValid = com.evoting.config.SpringContextBridge
                    .getBean(com.evoting.service.VoteService.class)
                    .isBlockchainValid();

            String msg = isValid ? "Blockchain is valid." : "Blockchain has been tampered!";
            JOptionPane.showMessageDialog(this, msg, "Blockchain Integrity",
                    isValid ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.ERROR_MESSAGE);
        });

        showResultsButton.addActionListener(e -> {
            ResultPanel resultPanel = new ResultPanel();
            JOptionPane.showMessageDialog(this, resultPanel, "Voting Results", JOptionPane.PLAIN_MESSAGE);
        });
    }

    private JLabel createInfoLabel() {
        JLabel label = new JLabel();
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.LIGHT_GRAY);
        label.setBorder(BorderFactory.createEmptyBorder(3, 10, 3, 10));
        return label;
    }

    // ✅ For displaying location (top-left)
    public void setLocationDisplay(String state, String city, String pincode) {
        locationDisplayLabel.setText("Location: " + state + ", " + city + ", " + pincode);
    }

    // ✅ For displaying full voter info (after voting)
    public void setVoterInfo(String name, String age, String dob, String city, String state, String status, String hash) {
        nameLabel.setText("Name: " + name);
        ageLabel.setText("Age: " + age);
        dobLabel.setText("DOB: " + dob);
        cityLabel.setText("City: " + city);
        stateLabel.setText("State: " + state);
        statusLabel.setText("Voting Status: " + status);
        hashLabel.setText("Vote Hash: " + hash);
    }

    public void clearDashboardInfo() {
        nameLabel.setText("");
        ageLabel.setText("");
        dobLabel.setText("");
        cityLabel.setText("");
        stateLabel.setText("");
        statusLabel.setText("User logged out.");
        hashLabel.setText("");
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
