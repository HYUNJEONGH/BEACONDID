package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-08-31.
 */
public class TicketData {
    String ticketbookID;
    String ticketID;
    String ticketName;
    String ticketDate;
    String seatlocation;
    String stadiumInfo;

    public TicketData(String ticketbookID, String ticketID, String ticketName, String ticketDate, String seatlocation, String stadiumInfo) {
        this.ticketbookID = ticketbookID;
        this.ticketID = ticketID;
        this.ticketName = ticketName;
        this.ticketDate = ticketDate;
        this.seatlocation = seatlocation;
        this.stadiumInfo = stadiumInfo;
    }

    public String getTicketbookID() {
        return ticketbookID;
    }

    public void setTicketbookID(String ticketbookID) {
        this.ticketbookID = ticketbookID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getTicketName() {
        return ticketName;
    }

    public void setTicketName(String ticketName) {
        this.ticketName = ticketName;
    }

    public String getTicketDate() {
        return ticketDate;
    }

    public void setTicketDate(String ticketDate) {
        this.ticketDate = ticketDate;
    }

    public String getStadiumInfo() {
        return stadiumInfo;
    }

    public void setStadiumInfo(String stadiumInfo) {
        this.stadiumInfo = stadiumInfo;
    }

    public String getSeatlocation() {
        return seatlocation;
    }

    public void setSeatlocation(String seatlocation) {
        this.seatlocation = seatlocation;
    }

}
