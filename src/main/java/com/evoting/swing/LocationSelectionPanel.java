package com.evoting.swing;

import com.evoting.model.StateData;
import com.evoting.util.LocationDataLoader;

import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LocationSelectionPanel extends JPanel {
    private final JComboBox<String> stateBox;
    private final JComboBox<String> cityBox;
    private final JTextField pincodeField;
    private final JButton proceedButton;
    
    private  JPanel mainPanel;
    private  CardLayout cardLayout;
    private final DashboardPanel dashboardPanel;

    private StateData stateData;
	private JFrame parentFrame;

    public LocationSelectionPanel(JPanel mainPanel, CardLayout cardLayout, DashboardPanel dashboardPanel) {
        this.mainPanel = mainPanel;
        this.cardLayout = cardLayout;
        this.dashboardPanel = dashboardPanel;

        setLayout(new GridBagLayout());
        setBackground(new Color(30, 30, 30));

        JLabel titleLabel = new JLabel("Select Your Location");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        stateBox = new JComboBox<>();
        cityBox = new JComboBox<>();
        pincodeField = new JTextField(10);

        proceedButton = new JButton("Proceed to Dashboard");
        proceedButton.setBackground(new Color(25, 25, 25));
        proceedButton.setForeground(Color.CYAN);
        proceedButton.setFocusPainted(false);
        proceedButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Load states and cities
        stateData = LocationDataLoader.loadLocationData();
        if (stateData != null) {
            for (StateData.State state : stateData.states) {
                stateBox.addItem(state.name);
            }
            updateCitiesForSelectedState();
        }

        // Update cities when a new state is selected
        stateBox.addActionListener(e -> updateCitiesForSelectedState());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        gbc.gridy = 0; add(titleLabel, gbc);
        gbc.gridy = 1; add(stateBox, gbc);
        gbc.gridy = 2; add(cityBox, gbc);

        gbc.gridy = 3;
        JLabel pinLabel = new JLabel("Enter Pincode:");
        pinLabel.setForeground(Color.WHITE);
        add(pinLabel, gbc);

        gbc.gridy = 4; add(pincodeField, gbc);
        gbc.gridy = 5; add(proceedButton, gbc);

        proceedButton.addActionListener(e -> {
            String state = (String) stateBox.getSelectedItem();
            String city = (String) cityBox.getSelectedItem();
            String pincode = pincodeField.getText(); // or box if you use dropdown

            dashboardPanel.setLocationDisplay(state, city, pincode); // ✅ Only sets location label

            cardLayout.show(mainPanel, "DashboardPanel");
        });

    }

    private void updateCitiesForSelectedState() {
        String selectedState = (String) stateBox.getSelectedItem();
        cityBox.removeAllItems();

        if (stateData != null) {
            for (StateData.State state : stateData.states) {
                if (state.name.equals(selectedState)) {
                    for (String city : state.cities) {
                        cityBox.addItem(city);
                    }
                    break;
                }
            }
        }
    }


}
