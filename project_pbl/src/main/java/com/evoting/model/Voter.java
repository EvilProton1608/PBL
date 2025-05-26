package com.evoting.model;

public class Voter {
    private String stdId;
    private String name;
    private String mobile;
    
    public Voter(String stdId, String name, String mobile) {
        this.stdId = stdId;
        this.name = name;
        this.mobile = mobile;
    }
    
    // Getters
    public String getStdId() { return stdId; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
}