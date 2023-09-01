package model;

import java.time.*;

/**
 * This class constructs the Appointment object and has getters and setters
 */
public class Appointment {
    private int appointmentID;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalDate startDate;
    private LocalDate endDate;
    private int customerID;
    private int contactID;
    private int userID;
    private String customerName;

    public Appointment(int appointmentID, String title, String description, String location, String type,
                       LocalDateTime startTime, LocalDateTime endTime, LocalDate startDate, LocalDate endDate,
                       int customerID, int contactID, int userId){
        this.appointmentID = appointmentID;
        this.title = title;
        this.description = description;
        this.location = location;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.startDate = startDate;
        this.endDate = endDate;
        this.customerID =customerID;
        this.contactID = contactID;
        this.userID = userId;

    }

    /**
     * This object is created when the user wants to get the appointment information when a customer is selected.
     * @param aID
     * @param title
     * @param cID
     * @param customerName
     */
    public Appointment(int aID, String title, int cID, String customerName) {
        this.appointmentID = aID;
        this.title = title;
        this.customerID = cID;
        this.customerName = customerName;
    }
    

    public String getCustomerName(){return customerName;}

    public void setCustomerName(String customerName) {this.customerName = customerName;}

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID){
        this.appointmentID = appointmentID;
    }

    public String getTitle() {
        return title;
    }

    public void set(String title){
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getStartDate(){
        return startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public LocalDateTime getStartTime() {return startTime;}

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
     return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public int getContactID() {
        return contactID;
    }

    public void setContactID(int contactID) {
        this.contactID = contactID;
    }


}
