package com.example.marketingmanager;

public class ContactDetails {
    String nameOfPOC;
    String designation;
    String contactNo;
    String email;

    public ContactDetails() {
    }

    public ContactDetails(String nameOfPOC, String designation, String contactNo, String email) {
        this.nameOfPOC = nameOfPOC;
        this.designation = designation;
        this.contactNo = contactNo;
        this.email = email;
    }

    public String getNameOfPOC() {
        return nameOfPOC;
    }

    public void setNameOfPOC(String nameOfPOC) {
        this.nameOfPOC = nameOfPOC;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
