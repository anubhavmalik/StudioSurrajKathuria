package com.example.anubhav.modern.Models;

/**
 * Created by Anubhav on 13-10-2017.
 */

public class InquiryItem {
    String byuser, date, details, epoch, time, title, usernumber;

    public InquiryItem() {
    }

    public InquiryItem(String byuser, String date, String details, String epoch, String time, String title, String usernumber) {

        this.byuser = byuser;
        this.date = date;
        this.details = details;
        this.epoch = epoch;
        this.time = time;
        this.title = title;
        this.usernumber = usernumber;
    }

    public String getByuser() {
        return byuser;
    }

    public String getDate() {
        return date;
    }

    public String getDetails() {
        return details;
    }

    public String getEpoch() {
        return epoch;
    }

    public String getTime() {
        return time;
    }

    public String getTitle() {
        return title;
    }

    public String getUsernumber() {
        return usernumber;
    }
}
