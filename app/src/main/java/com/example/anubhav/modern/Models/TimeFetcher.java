package com.example.anubhav.modern.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Anubhav on 10-10-2017.
 */

public class TimeFetcher {
    DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
    String phoneDate = df.format(Calendar.getInstance().getTime());
    DateFormat df1 = new SimpleDateFormat("hh:mm a");
    String phoneTime = df1.format(Calendar.getInstance().getTime());

    public String getPhoneDate() {
        return phoneDate;
    }

    public String getPhoneTime() {
        return phoneTime;
    }
}
