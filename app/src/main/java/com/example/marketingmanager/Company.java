package com.example.marketingmanager;

import java.util.ArrayList;
import java.util.List;

public class Company {
    String nameOfCompany;
    List<ContactDetails> contacts;
    Boolean proposalSent, subTeam, active;
    List<LogData> logs;

    public Company(String nameOfCompany, List<ContactDetails> contacts, Boolean proposalSent, List<LogData> logs, Boolean subTeam, Boolean active) {
        this.nameOfCompany = nameOfCompany;
        this.contacts = contacts;
        this.proposalSent = proposalSent;
        this.logs = logs;
        this.subTeam = subTeam;
        this.active = active;
    }

    public Company(String nameOfCompany, Boolean subTeam) {
        this.nameOfCompany = nameOfCompany;
        this.subTeam = subTeam;
        active = true;
        proposalSent = false;
        contacts = new ArrayList<>();
        logs = new ArrayList<>();
    }

    public Boolean isSubTeam() {
        return subTeam;
    }

    public Boolean isActive() {
        return active;
    }

    public String getName() {
        return nameOfCompany;
    }
}
