package com.evoting.service;

import java.util.HashMap;
import java.util.Map;

import com.evoting.model.Voter;

public class DatabaseService {
    private static final Map<String, Voter> voters = new HashMap<>();

    static {
        voters.put("23011230", new Voter("23011230", "Aditya Mani ", "Dehradun", 25));
        voters.put("230112421", new Voter("230112421", "Priyanshu Negi", "Dehradun", 30));
    }

    public Voter getVoter(String voterId) {
        return voters.get(voterId);
    }

    public boolean markAsVoted(String voterId) {
        Voter voter = voters.get(voterId);
        if (voter != null && !voter.hasVoted()) {
            voter.setVoted(true);
            return true;
        }
    
        return false;
    }
        public boolean isValidVoter(String voterId) {
            return voterId.matches("V\\d{4}");  
        
    }
}