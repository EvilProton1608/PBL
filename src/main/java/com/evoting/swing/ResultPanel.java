package com.evoting.swing;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;

public class ResultPanel extends JPanel {

    private JTable resultTable;
    private DefaultTableModel tableModel;

    public ResultPanel() {
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("Election Results", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 22));
        add(titleLabel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new Object[]{"Candidate", "Votes"}, 0);
        resultTable = new JTable(tableModel);
        add(new JScrollPane(resultTable), BorderLayout.CENTER);

        JButton refreshButton = new JButton("Refresh Results");
        refreshButton.addActionListener(e -> fetchResults());
        add(refreshButton, BorderLayout.SOUTH);
        

        // Load results on panel load
        fetchResults();
    }

    private void fetchResults() {
        try {
            String apiUrl = "http://localhost:5505/api/votes/results";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            // Read the response
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder responseText = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                responseText.append(line);
            }
            in.close();

            // Parse JSON
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Integer> results = mapper.readValue(responseText.toString(), LinkedHashMap.class);

            // Update table
            tableModel.setRowCount(0); // Clear previous results
            for (Map.Entry<String, Integer> entry : results.entrySet()) {
                tableModel.addRow(new Object[]{entry.getKey(), entry.getValue()});
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching results", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
