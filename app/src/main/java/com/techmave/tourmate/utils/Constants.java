package com.techmave.tourmate.utils;

public interface Constants {

    String PREF_NAME = "app_data";
    String PREF_INSTALLED = "installed";
    String PREF_UID = "uid";
    String PREF_EMAIL = "user_email";
    String PREF_TOKEN = "user_token";

    String USER_EMAIL = "userEmail";

    String DATABASE_NAME = "user_data";
    int DATABASE_VERSION = 1;

    String TABLE_IMAGES = "images";
    String TABLE_EVENTS = "events";

    String IMAGE_ID = "image_id";
    String IMAGE_URI = "image_uri";
    String IMAGE_CAPTURE_TIME = "capture_time";
    String EVENT_ID = "event_id";
    String EVENT_DESCRIPTION = "event_description";
    String EVENT_BUDGET = "event_budget";
    String EVENT_START_DATE = "from_date";
    String EVENT_END_DATE = "to_date";

    int HOME_FRAGMENT = 1;

    String USER_NOT_FOUND = "ERROR_USER_NOT_FOUND";
    String USER_DISABLED = "ERROR_USER_DISABLED";
}
