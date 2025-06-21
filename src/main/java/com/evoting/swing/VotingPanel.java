package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.model.Voter;
import com.evoting.service.VoteService;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;

public class VotingPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final VoteService voteService;

    private final JComboBox<String> candidateBox = new JComboBox<>(new String[]{"BJP", "Congress", "SPA"});
    private final JButton voteButton = new JButton("Submit Vote");
    private final JLabel statusLabel = new JLabel();

    private final JLabel nameLabel = new JLabel();
    private final JLabel ageLabel = new JLabel();
    private final JLabel dobLabel = new JLabel();
    private final JLabel mobileLabel = new JLabel();
    private final JLabel cityLabel = new JLabel();
    private final JLabel stateLabel = new JLabel();
    private final JLabel pincodeLabel = new JLabel();
    private final JLabel imageLabel = new JLabel();

    private Voter currentVoter;
    private DashboardPanel dashboardPanel;
    private String currentVoterId;

    public VotingPanel() {
        voteService = SpringContextBridge.getBean(VoteService.class);

        setLayout(new BorderLayout(20, 20));
        setBackground(new Color(34, 34, 34));

        JLabel title = new JLabel("Cast Your Vote");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(Color.WHITE);
        title.setHorizontalAlignment(SwingConstants.CENTER);
        add(title, BorderLayout.NORTH);

        JPanel infoPanel = new JPanel(new GridLayout(7, 1, 5, 5));
        infoPanel.setBackground(new Color(34, 34, 34));
        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        for (JLabel label : new JLabel[]{nameLabel, ageLabel, dobLabel, mobileLabel, cityLabel, stateLabel, pincodeLabel}) {
            label.setForeground(Color.LIGHT_GRAY);
            label.setFont(font);
            infoPanel.add(label);
        }

        JPanel votePanel = new JPanel(new GridBagLayout());
        votePanel.setBackground(new Color(34, 34, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel candidateLabel = new JLabel("Select Candidate:");
        candidateLabel.setForeground(Color.LIGHT_GRAY);
        candidateLabel.setFont(font);

        candidateBox.setFont(font);
        candidateBox.setBackground(new Color(25, 25, 25));
        candidateBox.setForeground(Color.WHITE);

        voteButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        voteButton.setBackground(new Color(45, 45, 48));
        voteButton.setForeground(Color.WHITE);
        
        JPanel backPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        backPanel.setBackground(new Color(34, 34, 34));
        backPanel.add(createBackButton(mainPanel, cardLayout));
        add(backPanel, BorderLayout.SOUTH);  // or NORTH, depending on layout


        gbc.gridx = 0;
        gbc.gridy = 0;
        votePanel.add(candidateLabel, gbc);

        gbc.gridx = 1;
        votePanel.add(candidateBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        votePanel.add(voteButton, gbc);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(34, 34, 34));

        JPanel infoAndImagePanel = new JPanel(new BorderLayout(10, 10));
        infoAndImagePanel.setBackground(new Color(34, 34, 34));
        infoAndImagePanel.add(infoPanel, BorderLayout.CENTER);

        imageLabel.setPreferredSize(new Dimension(120, 140));
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        infoAndImagePanel.add(imageLabel, BorderLayout.WEST);

        centerPanel.add(infoAndImagePanel, BorderLayout.NORTH);
        centerPanel.add(votePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        statusLabel.setForeground(Color.ORANGE);
        statusLabel.setHorizontalAlignment(SwingConstants.CENTER);
        statusLabel.setFont(font);
        add(statusLabel, BorderLayout.SOUTH);

        voteButton.addActionListener(e -> castVote());
    }

    public void setDashboardPanel(DashboardPanel dashboardPanel) {
        this.dashboardPanel = dashboardPanel;
    }

    public void setVoter(Voter voter) {
        if (voter == null) {
            statusLabel.setText("Voter not found.");
            return;
        }

        this.currentVoter = voter;
        this.currentVoterId = voter.getStdId();

        nameLabel.setText("Name: " + voter.getName());
        ageLabel.setText("Age: " + voter.getAge());
        dobLabel.setText("DOB: " + voter.getDob());
        mobileLabel.setText("Mobile: " + voter.getMobile());
        cityLabel.setText("City: " + voter.getCity());
        stateLabel.setText("State: " + voter.getState());
        pincodeLabel.setText("Pincode: " + voter.getPincode());

        byte[] imageBytes = voter.getImageBytes();
        if (imageBytes != null && imageBytes.length > 0) {
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBytes));
                if (bufferedImage != null) {
                    Image scaledImage = bufferedImage.getScaledInstance(100, 120, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    imageLabel.setIcon(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                imageLabel.setIcon(null);
            }
        } else {
            imageLabel.setIcon(null);
        }

        statusLabel.setText("");
        voteButton.setEnabled(true);
        candidateBox.setEnabled(true);
    }

    private void castVote() {
        if (currentVoterId == null || currentVoterId.isEmpty()) {
            statusLabel.setText("No voter ID found.");
            return;
        }

        if (voteService.hasAlreadyVoted(currentVoterId)) {
            statusLabel.setText("⚠️ You have already voted.");
            voteButton.setEnabled(false);
            candidateBox.setEnabled(false);
            return;
        }

        String candidate = (String) candidateBox.getSelectedItem();
        boolean success = voteService.recordVote(currentVoterId, candidate);

        if (success) {
            statusLabel.setText("✅ Vote recorded successfully!");
            voteButton.setEnabled(false);
            candidateBox.setEnabled(false);

            String hash = voteService.getLastBlockHashForVoter(currentVoterId);
            if (hash == null || hash.isEmpty()) hash = "N/A";

            if (dashboardPanel != null) {
                dashboardPanel.setVoterInfo(
                    currentVoter.getName(),
                    currentVoter.getAge(),
                    currentVoter.getDob(),
                    currentVoter.getCity(),
                    currentVoter.getState(),
                    "Vote recorded successfully",
                    hash
                );
            }

            if (cardLayout != null && mainPanel != null) {
                cardLayout.show(mainPanel, "DashboardPanel");
            }

        } else {
            statusLabel.setText("❌ Failed to record vote.");
        }
    }
    private JButton createBackButton(JPanel mainPanel, CardLayout cardLayout) {
        JButton backButton = new JButton("⬅ Back to Dashboard");
        backButton.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        backButton.setBackground(new Color(50, 50, 50));
        backButton.setForeground(Color.CYAN);
        backButton.setFocusPainted(false);

        backButton.addActionListener(e -> cardLayout.show(mainPanel, "DashboardPanel"));
        return backButton;
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
