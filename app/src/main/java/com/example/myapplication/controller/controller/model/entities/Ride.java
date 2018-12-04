package com.example.myapplication.controller.controller.model.entities;

/**
 * a ride class allows a new user of the app to request a ride via database
 * needs the location of the user
 */
public class Ride {

    // ************ fields **************
    private String destination;
    private String location;
    private Long phone;
    private String email;
    private ClientRequestStatus status;

    /**
     * constructor
     *
     * @param destination
     * @param loca
     * @param phone
     * @param email
     */
    public Ride(String destination, String loca, Long phone, String email) {
        this.destination = destination;
        this.location = loca;
        this.phone = phone;
        this.email = email;
        status = ClientRequestStatus.WAITING;
    }

    // *********** getters & setters ************

    /**
     * gets destination
     *
     * @return String
     */
    public String getDestination() {
        return destination;
    }

    /**
     * sets the destination
     *
     * @param destination
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * gets a location of the phone
     *
     * @return String
     */
    public String getLocation() {
        return location;
    }

    /**
     * sets the location
     *
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * gets the phone number
     *
     * @return Long
     */
    public Long getPhone() {
        return phone;
    }

    /**
     * sets the phone number
     *
     * @param phone
     */
    public void setPhone(Long phone) {
        this.phone = phone;
    }

    /**
     * gets the email
     *
     * @return String
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets the email
     *
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    public ClientRequestStatus getStatus() {
        return status;
    }

    public void setStatus(ClientRequestStatus status) {
        this.status = status;
    }
}
