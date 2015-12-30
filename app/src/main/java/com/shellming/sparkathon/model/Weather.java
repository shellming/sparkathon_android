package com.shellming.sparkathon.model;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public interface Weather {
    public String getFormatDate();      // 获取天气的有效时间，Example：11th December 2015, Friday 10:42
    public String getWeatherDesc();     // 天气描述
    public String getTemp();            // 温度
    public Integer getIcon();           // 天气图标， 0-49
    public Integer getWet();            // 获取湿度，百分数
    public Integer getWindSpeed();      // 获取风速
    public Double getVisit();          // 获取能见度
    public String getSunrise();         // 获取日出时间
    public String getSunset();          // 获取日落时间
    public Double getPressure();        // 获取压强
}
