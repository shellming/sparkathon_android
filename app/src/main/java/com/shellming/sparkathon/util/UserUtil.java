package com.shellming.sparkathon.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

/**
 * Created by ruluo1992 on 11/27/2015.
 */
public class UserUtil {

    private static final String LOGIN_STATUS = "LOGIN_STATUS";
    private static final String TWITTER_TOKEN = "TWITTER_TOKEN";
    private static final String TWITTER_TOKEN_SECRET = "TWITTER_TOKEN_SECRET";


    public static void login(Context context, String token, String tokenSecret){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, true);
        editor.putString(TWITTER_TOKEN, token);
        editor.putString(TWITTER_TOKEN_SECRET, tokenSecret);
        editor.commit();
    }

    public static void logout(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(LOGIN_STATUS, false);
        editor.putString(TWITTER_TOKEN, "");
        editor.putString(TWITTER_TOKEN_SECRET, "");
        editor.commit();
    }

    public static boolean isLogin(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getBoolean(LOGIN_STATUS, false);
    }

    public static String getToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(TWITTER_TOKEN, "");
    }
    public static String getTokenSecret(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString(TWITTER_TOKEN_SECRET, "");
    }

    public static AccessToken getAccessToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String token = sharedPreferences.getString(TWITTER_TOKEN, "");
        String tokenSecret = sharedPreferences.getString(TWITTER_TOKEN_SECRET, "");
        if("".equals(token) || "".equals(tokenSecret))
            return null;
        AccessToken accessToken = new AccessToken(token, tokenSecret);
        return accessToken;
    }

}
