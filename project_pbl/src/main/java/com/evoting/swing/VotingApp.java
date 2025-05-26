package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
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
        System.out.println("Is headless? " + java.awt.GraphicsEnvironment.isHeadless());


        SwingUtilities.invokeLater(() -> {
            frame = new JFrame("E-Voting System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 400);
            frame.setLocationRelativeTo(null);

            cardLayout = new CardLayout();
            mainPanel = new JPanel(cardLayout);

            // Create panels
            VoterIdPanel voterIdPanel = new VoterIdPanel();
            OtpVerificationPanel otpVerificationPanel = new OtpVerificationPanel();
            VotingPanel votingPanel = new VotingPanel();

            // Pass a reference to VotingApp or mainPanel so panels can request switching
            voterIdPanel.setParentFrame(frame);
            voterIdPanel.setMainPanel(mainPanel);
            voterIdPanel.setCardLayout(cardLayout);

            otpVerificationPanel.setParentFrame(frame);
            otpVerificationPanel.setMainPanel(mainPanel);
            otpVerificationPanel.setCardLayout(cardLayout);

            votingPanel.setParentFrame(frame);
            votingPanel.setMainPanel(mainPanel);
            votingPanel.setCardLayout(cardLayout);

            // Add panels to mainPanel with names for CardLayout
            mainPanel.add(voterIdPanel, "VoterIdPanel");
            mainPanel.add(otpVerificationPanel, "OtpVerificationPanel");
            mainPanel.add(votingPanel, "VotingPanel");

            frame.setContentPane(mainPanel);
            frame.setVisible(true);
        });
    }
}
