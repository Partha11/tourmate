package com.syntaxerror.tourmate;

public class SingleUser {

    private String firstName;
    private String lastName;
    private String userEmail;
    private String userPassword;
    private String userPhoneNo;

    public SingleUser(String firstName, String lastName, String userEmail, String userPassword, String userPhoneNo) {

        this.firstName = firstName;
        this.lastName = lastName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userPhoneNo = userPhoneNo;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public String getUserEmail() {

        return userEmail;
    }

    public String getUserPassword() {

        return userPassword;
    }

    public String getUserPhoneNo() {

        return userPhoneNo;
    }
}
