package com.shellming.sparkathon.tasks;

import android.os.AsyncTask;

import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.util.TwitterUtil;

import twitter4j.GeoLocation;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * Created by ruluo1992 on 12/21/2015.
 */
public class TwitterPostTweetTask extends AsyncTask<String, Integer, Boolean> {

    @Override
    protected Boolean doInBackground(String... params) {
        StatusUpdate update = new StatusUpdate(params[0]);
        Double latitude = Double.valueOf(GlobalConstant.latitude);
        Double longitude = Double.valueOf(GlobalConstant.logitude);
        GeoLocation location = new GeoLocation(latitude, longitude);
        update.setLocation(location);
        Twitter twitter = TwitterUtil.getInstance().getTwitter();
        try {
            twitter.updateStatus(update);
            return true;
        } catch (TwitterException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean success) {
        System.out.println("post success:" + success);
    }
}
