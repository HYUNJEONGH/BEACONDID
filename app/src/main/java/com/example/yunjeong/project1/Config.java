package com.example.yunjeong.project1;

/**
 * Created by YunJeong on 2016-09-01.
 */
public class Config {
    //Keys for Sharedpreferences
    //This would be the name of our shared preferences
    public static final String SHARED_PREF_NAME = "myloginapp";

    //This would be used to store the id of current logged in user
    public static final String USERID_SHARED_PREF = "id";
    public static final String USEREMAIL_SHARED_PREF = "email";
    public static final String USERNAME_SHARED_PREF = "name";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";

}
