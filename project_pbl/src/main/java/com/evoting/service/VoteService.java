package com.evoting.service;

import com.evoting.blockchain.Blockchain;

public class VoteService {
    private final Blockchain blockchain;
    
    public VoteService() {
        this.blockchain = new Blockchain(); 
    }

    
    public boolean isChainValid() {
        return blockchain.isChainValid();
    }

    public void recordVote(String voterId, String candidate) {
        blockchain.addBlock(voterId, candidate); 
     
    }
    public void printVoteReceipt(String voterId, String candidate) {
        System.out.println("\n--- RECEIPT ---");
        System.out.println("Voter: " + voterId);
        System.out.println("Candidate: " + candidate);
        System.out.println("Time: " + new java.util.Date());
    }
}