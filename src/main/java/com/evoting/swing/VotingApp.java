package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.formdev.flatlaf.FlatDarkLaf;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import javax.swing.*;
import java.awt.*;

@SpringBootApplication(scanBasePackages = "com.evoting")
public class VotingApp {

    private static JFrame frame;
    private static JPanel mainPanel;
    private static CardLayout cardLayout;

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(VotingApp.class, args);
        SpringContextBridge.setApplicationContext(context);

        try {
            // Set FlatLaf Dark Look and Feel
            UIManager.setLookAndFeel(new FlatDarkLaf());

            UIManager.put("Button.arc", 12);
            UIManager.put("Component.arc", 12);
            UIManager.put("TextComponent.arc", 12);

            UIManager.put("TextComponent.padding", new Insets(8, 12, 8, 12));

            UIManager.put("Button.font", new Font("Segoe UI", Font.PLAIN, 16));
            UIManager.put("Label.font", new Font("Segoe UI", Font.PLAIN, 14));
            UIManager.put("TextField.font", new Font("Segoe UI", Font.PLAIN, 14));

            UIManager.put("Panel.background", new Color(34, 34, 34));

            UIManager.put("Button.background", new Color(45, 45, 48));
            UIManager.put("Button.foreground", Color.WHITE);

            UIManager.put("TextField.background", new Color(25, 25, 25));
            UIManager.put("TextField.foreground", Color.WHITE);
            UIManager.put("TextField.border", BorderFactory.createEmptyBorder(6, 10, 6, 10));

            UIManager.put("Label.foreground", Color.LIGHT_GRAY);

        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("E-Voting System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);
            cardLayout.show(mainPanel, "LocationPanel");

            // Create panels
            
            DashboardPanel dashboardPanel = new DashboardPanel();
            VoterIdPanel voterIdPanel = new VoterIdPanel();
            OtpVerificationPanel otpVerificationPanel = new OtpVerificationPanel();
            VotingPanel votingPanel = new VotingPanel();
            LocationSelectionPanel locationPanel = new LocationSelectionPanel(mainPanel, cardLayout, dashboardPanel);
            
            voterIdPanel.setOtpVerificationPanel(otpVerificationPanel);
            otpVerificationPanel.setVotingPanel(votingPanel);
            votingPanel.setDashboardPanel(dashboardPanel);

            // Pass references for navigation
            dashboardPanel.setParentFrame(frame);
            dashboardPanel.setMainPanel(mainPanel);
            dashboardPanel.setCardLayout(cardLayout);
            
            
            voterIdPanel.setParentFrame(frame);
            voterIdPanel.setMainPanel(mainPanel);
            voterIdPanel.setCardLayout(cardLayout);

            otpVerificationPanel.setParentFrame(frame);
            otpVerificationPanel.setMainPanel(mainPanel);
            otpVerificationPanel.setCardLayout(cardLayout);

            votingPanel.setParentFrame(frame);
            votingPanel.setMainPanel(mainPanel);
            votingPanel.setCardLayout(cardLayout);
            

            // Add panels to CardLayout container with unique names
            mainPanel.add(locationPanel, "LocationSelectionPanel");
            mainPanel.add(dashboardPanel, "DashboardPanel");
            mainPanel.add(voterIdPanel, "VoterIdPanel");
            mainPanel.add(otpVerificationPanel, "OtpVerificationPanel");
            mainPanel.add(votingPanel, "VotingPanel");

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
