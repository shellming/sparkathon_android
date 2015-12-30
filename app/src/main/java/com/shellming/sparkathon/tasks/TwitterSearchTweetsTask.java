package com.shellming.sparkathon.tasks;

import android.os.AsyncTask;

import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.util.TwitterUtil;

import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Twitter;
import twitter4j.TwitterException;

/**
 * Created by ruluo1992 on 12/21/2015.
 */
public class TwitterSearchTweetsTask extends AsyncTask<String, Integer, List> {

    @Override
    protected void onPostExecute(List list) {
        if(list != null){
            System.out.println("search tweets success");
        }
    }

    @Override
    protected List doInBackground(String... params) {
        Query query = new Query(params[0]);
        Double latitude = Double.valueOf(GlobalConstant.latitude);
        Double longitude = Double.valueOf(GlobalConstant.logitude);
        GeoLocation location = new GeoLocation(latitude, longitude);
        query.setGeoCode(location, 1, Query.Unit.km);
        Twitter twitter = TwitterUtil.getInstance().getTwitter();
        try {
            QueryResult result =twitter.search(query);
            return result.getTweets();
        } catch (TwitterException e) {
            e.printStackTrace();
            return null;
        }
    }
}
