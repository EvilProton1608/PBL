package com.evoting.blockchain;

import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private List<Block> chain;

    public Blockchain() {
        this.chain = new ArrayList<>();
        chain.add(new Block("GENESIS", "N/A", "0"));
    }

    public Block getLatestBlock() {
        return chain.get(chain.size() - 1);
    }

    public void addBlock(String voterId, String candidate) {
        Block newBlock = new Block(voterId, candidate, getLatestBlock().hash);
        chain.add(newBlock);
        System.out.println("Block added to chain: " + newBlock.hash);
    }

    public boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            Block current = chain.get(i);
            Block previous = chain.get(i - 1);

            if (!current.hash.equals(current.calculateHash())) {
                return false;
            }

            if (!current.previousHash.equals(previous.hash)) {
                return false;
            }
        }
        return true;
    }
}