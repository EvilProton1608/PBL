package com.evoting;
import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);

        // Start the Swing UI
        SwingUtilities.invokeLater(() -> new com.evoting.swing.VotingApp());
    }
}
