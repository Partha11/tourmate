package com.techmave.tourmate.model;

public class SingleUser {

    private String userId;
    private String userMail;
    private String userName;
    private String userGender;
    private String userAge;
    private String profileImage;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserAge() {
        return userAge;
    }

    public void setUserAge(String userAge) {
        this.userAge = userAge;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {

            return false;

        } else if (!(obj instanceof SingleUser)) {

            return false;
        }

        SingleUser user = (SingleUser) obj;
        return user.userMail.equals(this.userMail) || user.userName.equals(this.userName);
    }
}
