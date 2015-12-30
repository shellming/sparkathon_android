package com.shellming.sparkathon.model;

/**
 * Created by ruluo1992 on 11/17/2015.
 */
public class MyMessage {

    public static final Integer SUCCESS = 0;

    public enum Type{
        REFRESH_USER, OBSERVATION_CURRENT, FORCAST_24HOUR, FORCAST_10DAY, GET_CITY_NAME
    }
    public Type type;
    public Object data;
    public int arg1;
    public int arg2;
    public int what;
}
