package com.techmave.tourmate.pojo;

public class FullName {

    private String firstName;
    private String lastName;

    public FullName() {

    }

    public FullName(String firstName, String lastName) {

        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {

        return firstName;
    }

    public String getLastName() {

        return lastName;
    }
}
