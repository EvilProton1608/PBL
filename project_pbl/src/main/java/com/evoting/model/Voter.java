package com.evoting.model;

public class Voter {
    private String voterId;
    private String name;
    private String city;
    private int age;
    private boolean hasVoted;

   
    public Voter(String voterId, String name, String city, int age) {
        this.voterId = voterId;
        this.name = name;
        this.city = city;
        this.age = age;
        this.hasVoted = false;
    }

    public String getVoterId() { return voterId; }
    public String getName() { return name; }
    public boolean hasVoted() { return hasVoted; }
    public void setVoted(boolean voted) { this.hasVoted = voted; }
}