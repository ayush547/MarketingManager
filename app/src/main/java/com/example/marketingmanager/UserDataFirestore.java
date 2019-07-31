package com.example.marketingmanager;

import java.util.ArrayList;
import java.util.List;

public class UserDataFirestore {
    public static String Key_ID = "uid";
    public static String Key_CompanyList = "companies";

    String uid;
    List<UserDataFirestoreCompany> companies;

    public UserDataFirestore() {
    }

    public UserDataFirestore(String uid) {
        this.uid = uid;
        companies = new ArrayList<>();
    }

    public UserDataFirestore(String uid, List<UserDataFirestoreCompany> companies) {
        this.uid = uid;
        this.companies = companies;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<UserDataFirestoreCompany> getCompanies() {
        return companies;
    }

    public void addCompanies(UserDataFirestoreCompany company) {
        this.companies.add(company);
    }

}
