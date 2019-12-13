package com.techmave.tourmate.utils;

import android.util.Patterns;

import java.util.Date;
import java.util.regex.Matcher;

public class Utility {

    public static String getNameFromEmail(String email) {

        String userName = "";
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(email);

        if (matcher.matches()) {

            String[] part = email.split("@");
            userName = part[0];
        }

        return userName;
    }

    public static boolean isValidEmail(String email) {

        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean compareDates(Date d1, Date d2) {

        return d1.compareTo(d2) >= 0;
    }
}
