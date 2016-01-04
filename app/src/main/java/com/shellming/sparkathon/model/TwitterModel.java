package com.shellming.sparkathon.model;

import com.shellming.sparkathon.constant.GlobalConstant;
import com.shellming.sparkathon.util.TwitterUtil;

import twitter4j.Status;

/**
 * Created by ruluo1992 on 1/4/2016.
 */
public class TwitterModel {
    private String dateTime;
    private String location;
    private Integer like;
    private String avatar;

    public static TwitterModel fromTwitter(Status status){
        String content = status.getText();
        if(content == null)
            return null;
        String[] parts = content.split("\n");
        if(parts.length != 3)
            return null;
        if(!parts[0].equals(GlobalConstant.TWITTER_TAG))
            return null;

        TwitterModel model = new TwitterModel(parts[1], parts[2], status.getFavoriteCount());
        model.setAvatar(status.getUser().getProfileImageURL());
        return model;
    }

    public TwitterModel(String dateTime, String location, Integer like){
        this.dateTime = dateTime;
        this.location = location;
        this.like = like;
    }

    public TwitterModel(String date, String time, String location) {
        dateTime = date + ", " + time;
        this.location = location;
        like = 0;
    }

    @Override
    public String toString() {
        String content = String.format("%s\nLocation : %s\nTime : %s",
                GlobalConstant.TWITTER_TAG,
                location,
                dateTime);
        return content;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getLike() {
        return like;
    }

    public void setLike(Integer like) {
        this.like = like;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
