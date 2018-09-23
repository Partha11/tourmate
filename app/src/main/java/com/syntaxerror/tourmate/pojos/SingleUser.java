package com.syntaxerror.tourmate.pojos;

import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SingleUser {

    private String userId;
    private String userMail;
    private String userName;
    private String userGender;
    private String userAge;
    private FullName fullName;

    public SingleUser() {

    }

    public SingleUser(String userId, String userMail, String userName) {

        this.userId = userId;
        this.userMail = userMail;
        this.userName = userName;
    }

    public SingleUser(String userId, String userMail, String userName, String userGender, String userAge, FullName fullName) {

        this.userId = userId;
        this.userMail = userMail;
        this.userName = userName;
        this.userGender = userGender;
        this.userAge = userAge;
        this.fullName = fullName;
    }

    public SingleUser(String userMail, String userName, FullName fullName) {

        this.userMail = userMail;
        this.userName = userName;
        this.fullName = fullName;
    }

    public SingleUser(String userMail, String userName) {

        this.userMail = userMail;
        this.userName = userName;
    }

    public SingleUser(String userMail) {

        this.userMail = userMail;
    }

    public FullName getFullName() {

        return fullName;
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

    @Override
    public boolean equals(Object obj) {

        if (obj == null)

            return false;

        if (!(obj instanceof SingleUser))

            return false;

        SingleUser user = (SingleUser) obj;

        if (user.userMail.equals(this.userMail) || user.userName.equals(this.userName))

            return true;

        else

            return false;
    }
}
