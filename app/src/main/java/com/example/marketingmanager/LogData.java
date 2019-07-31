package com.example.marketingmanager;

public class LogData {
    String date;
    String time;
    String whatHappened;
    Boolean proposalSent;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getWhatHappened() {
        return whatHappened;
    }

    public void setWhatHappened(String whatHappened) {
        this.whatHappened = whatHappened;
    }
}
