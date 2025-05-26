package com.evoting.swing;

import com.evoting.config.SpringContextBridge;
import com.evoting.service.VoteService;

import javax.swing.*;
import java.awt.*;

public class VotingPanel extends JPanel {
    private JFrame parentFrame;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private final VoteService voteService;

    private final JComboBox<String> candidateBox = new JComboBox<>(new String[]{"Alice", "Bob", "Charlie"});
    private final JButton voteButton = new JButton("Submit Vote");
    private final JLabel statusLabel = new JLabel();

    private String currentVoterId;

    public VotingPanel() {
        voteService = SpringContextBridge.getBean(VoteService.class);

        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.add(new JLabel("Select Candidate:"));
        formPanel.add(candidateBox);
        formPanel.add(voteButton);

        add(formPanel, BorderLayout.CENTER);
        add(statusLabel, BorderLayout.SOUTH);

        voteButton.addActionListener(e -> castVote());
    }

    public void setVoterId(String voterId) {
        this.currentVoterId = voterId;
    }

    private void castVote() {
        if (currentVoterId == null || currentVoterId.isEmpty()) {
            statusLabel.setText("No voter ID found.");
            return;
        }
        String candidate = (String) candidateBox.getSelectedItem();
        boolean success = voteService.recordVote(currentVoterId, candidate);

        if (success) {
            statusLabel.setText("Vote recorded successfully!");
        } else {
            statusLabel.setText("Failed to record vote.");
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
