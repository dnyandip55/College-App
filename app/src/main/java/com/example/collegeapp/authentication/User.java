package com.example.collegeapp.authentication;

public class User {
    private String fullName;
    private String email;
    private String dob;
    private String gender;
    private String mobile;

    // Empty constructor required for Firebase
    public User() {}

    // Constructor with all fields
    public User(String fullName, String email, String dob, String gender, String mobile) {
        this.fullName = fullName;
        this.email = email;
        this.dob = dob;
        this.gender = gender;
        this.mobile = mobile;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }
}
