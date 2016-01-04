package com.shellming.sparkathon.model;

/**
 * Created by ruluo1992 on 1/3/2016.
 */
public class RunExpModel {
    private Integer wspd;           //风速
    private String temp;           //温度，摄氏度， 3~10
    private Integer rh;             //相对湿度，%

    public RunExpModel(Integer rh, String temp, Integer wspd) {
        this.rh = rh;
        this.temp = temp;
        this.wspd = wspd;
    }

    public static RunExpModel fromWeather(Weather weather){
        RunExpModel model = new RunExpModel(
                weather.getWet(),
                weather.getTemp(),
                weather.getWindSpeed());
        return model;
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
