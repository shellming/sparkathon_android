package com.shellming.sparkathon.model;

/**
 * Created by ruluo1992 on 1/3/2016.
 */
public class RunExpModel {
    private Integer wspd;           //风速
    private String temp;           //温度，摄氏度， 3~10
    private Integer rh;             //相对湿度，%
    private String user = "common";            // 用户id，默认为common
    private String description;     // 天气描述
    private Integer descCode;        // 描述编号
    private Double exp;             // 当前天气跑步指数


    public RunExpModel(Integer rh, String temp, Integer wspd, String description, Integer descCode) {
        this.wspd = wspd;
        this.temp = temp;
        this.rh = rh;
        this.description = description;
        this.descCode = descCode;
    }

    public static RunExpModel fromWeather(Weather weather){
        RunExpModel model = new RunExpModel(
                weather.getWet(),
                weather.getTemp(),
                weather.getWindSpeed(),
                weather.getWeatherDesc(),
                weather.getDecCode());
        return model;
    }

    public Integer getDescCode() {
        return descCode;
    }

    public void setDescCode(Integer descCode) {
        this.descCode = descCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getExp() {
        return exp;
    }

    public void setExp(Double exp) {
        this.exp = exp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Integer getRh() {
        return rh;
    }

    public void setRh(Integer rh) {
        this.rh = rh;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public Integer getWspd() {
        return wspd;
    }

    public void setWspd(Integer wspd) {
        this.wspd = wspd;
    }
}
