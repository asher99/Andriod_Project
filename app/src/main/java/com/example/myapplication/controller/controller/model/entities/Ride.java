package com.example.myapplication.controller.controller.model.entities;

public class Ride {

    private String destination;
    private String location;
    private Long phone;
    private String email;

    public Ride(String destination,String loca, Long phone, String email) {
        this.destination = destination;
        this.location = loca;
        this.phone = phone;
        this.email = email;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLocation() { return location; }

    public void setLocation(String location) { this.location = location; }
    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }











}
