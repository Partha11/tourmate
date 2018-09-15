package com.syntaxerror.tourmate.pojos;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleUser {

    private int id;

    private String userMail;
    private String userName;

    public SingleUser(int id, String userMail, String userName) {

        this.id = id;
        this.userMail = userMail;
        this.userName = userName;
    }

    public SingleUser(String userMail, String userName) {

        this.userMail = userMail;
        this.userName = userName;
    }

    public SingleUser(String userMail) {

        this.userMail = userMail;
    }

    public String getUserMail() {

        return userMail;
    }

    public String getUserName() {

        return userName;
    }

    public static String emailToName(String userMail) {

        String userName = "";
        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(userMail);

        if (matcher.matches()) {

            for (int i = 0; i < userMail.length(); i++) {

                if (userMail.charAt(i) == '@')

                    break;

                userName += userMail.charAt(i);
            }

            return userName;
        }

        else {

            return null;
        }
    }

    public static boolean isEmail(String userMail) {

        Matcher matcher = Patterns.EMAIL_ADDRESS.matcher(userMail);

        return matcher.matches();
    }
}
