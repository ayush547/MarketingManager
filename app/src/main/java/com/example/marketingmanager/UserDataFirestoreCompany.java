package com.example.marketingmanager;

public class UserDataFirestoreCompany {
    String companyName;
    Boolean subTeam, active;

    public UserDataFirestoreCompany(String companyName, Boolean subTeam) {
        this.companyName = companyName;
        this.subTeam = subTeam;
        active = true;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public UserDataFirestoreCompany() {
    }

    public String getCompanyName() {
        return companyName;
    }

    public Boolean getSubTeam() {
        return subTeam;
    }
}
