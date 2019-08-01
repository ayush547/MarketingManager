package com.example.marketingmanager;

public class LogData {
    String date;
    String whatHappened;
    Boolean proposalSent;

    public LogData() {
    }

    public LogData(String date, String whatHappened, Boolean proposalSent) {
        this.date = date;
        this.whatHappened = whatHappened;
        this.proposalSent = proposalSent;
    }

    public Boolean getProposalSent() {
        return proposalSent;
    }

    public void setProposalSent(Boolean proposalSent) {
        this.proposalSent = proposalSent;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getWhatHappened() {
        return whatHappened;
    }

    public void setWhatHappened(String whatHappened) {
        this.whatHappened = whatHappened;
    }
}
