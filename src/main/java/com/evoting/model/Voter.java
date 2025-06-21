package com.evoting.model;

public class Voter {
    private String stdId;
    private String name;
    private String mobile;
    private String age;
    private String dob;
    private String city;
    private String state;
    private String pincode;
    private byte[] imageBytes;

    public Voter(String stdId, String name, String mobile, String age, String dob,
                 String city, String state, String pincode, String imagePath, byte[] imageBytes) {
        this.stdId = stdId;
        this.name = name;
        this.mobile = mobile;
        this.age = age;
        this.dob = dob;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
        this.imageBytes = imageBytes;
    }

    // Getters
    public String getStdId() { return stdId; }
    public String getName() { return name; }
    public String getMobile() { return mobile; }
    public String getAge() { return age; }
    public String getDob() { return dob; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public byte[] getImageBytes() {
        return imageBytes;
    }
}
