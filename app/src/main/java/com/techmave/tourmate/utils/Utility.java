package com.techmave.tourmate.utils;

import android.util.Patterns;

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
}
