package com.example.marketingmanager;

import java.util.ArrayList;
import java.util.List;

public class CompanyDataFirestore {
    String nameOfCompany;
    List<ContactDetails> contacts;
    Boolean proposalSent, subTeam;
    List<LogData> logs;

    public CompanyDataFirestore(String nameOfCompany, List<ContactDetails> contacts, Boolean proposalSent, List<LogData> logs, Boolean subTeam) {
        this.nameOfCompany = nameOfCompany;
        this.contacts = contacts;
        this.proposalSent = proposalSent;
        this.logs = logs;
        this.subTeam = subTeam;
    }

    public CompanyDataFirestore(String nameOfCompany, Boolean subTeam) {
        this.nameOfCompany = nameOfCompany;
        this.subTeam = subTeam;
        proposalSent = false;
        contacts = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public CompanyDataFirestore() {
    }

    public String getNameOfCompany() {
        return nameOfCompany;
    }

    public void setNameOfCompany(String nameOfCompany) {
        this.nameOfCompany = nameOfCompany;
    }

    public List<ContactDetails> getContacts() {
        return contacts;
    }

    public void setContacts(List<ContactDetails> contacts) {
        this.contacts = contacts;
    }

    public Boolean getProposalSent() {
        return proposalSent;
    }

    public void setProposalSent(Boolean proposalSent) {
        this.proposalSent = proposalSent;
    }

    public Boolean getSubTeam() {
        return subTeam;
    }

    public void setSubTeam(Boolean subTeam) {
        this.subTeam = subTeam;
    }

    public List<LogData> getLogs() {
        return logs;
    }

    public void setLogs(List<LogData> logs) {
        this.logs = logs;
    }
}
