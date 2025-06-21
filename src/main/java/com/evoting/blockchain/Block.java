package com.evoting.blockchain;

import java.util.Date;

public class Block {
    public String hash;
    public String previousHash;
    private String voterId;
    private String candidate;
    private long timeStamp;

    public Block(String voterId, String candidate, String previousHash) {
        this.voterId = voterId;
        this.candidate = candidate;
        this.previousHash = previousHash;
        this.timeStamp = new Date().getTime();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(
                previousHash +
                timeStamp +
                voterId +
                candidate
        );
    }
}