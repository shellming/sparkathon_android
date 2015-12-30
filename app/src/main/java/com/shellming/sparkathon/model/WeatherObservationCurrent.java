package com.shellming.sparkathon.model;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public class WeatherObservationCurrent implements WeatherResponse{
    private MetaData metadata;
    private Observation observation;

    @Override
    public List<Weather> getWeatherData(){
        List<Weather> weathers = new ArrayList<>();
        weathers.add(observation);
        return weathers;
    }

    class Observation extends AbstractWeather implements Weather{
        private Long expire_time_gmt;     //过期时间
        private Long obs_time;           //观测时间
        private String phrase_32char;     //天气描述，可以指定语言
        private String sky_cover;        //天空可见度，可选值：Clear，Partly Cloudy，Mostly Cloudy，Cloudy
        private String sunrise;         //日出时间，Example: 20130523T06:32:00-04:00
        private String sunset;          //日落时间，Example: 20130523T20:37:00-04:00
        private String day_ind;          //当前是白天还是黑夜，可选值：D = Day, N = Night, X = missing
        private String wxman;           //户外活动指数，Example: wx1050
        private Integer icon_code;      //图标标号
        private String dow;             //星期几
        private Metric metric;

        @Override
        public String getFormatDate() {
            return formatDate(obs_time);
        }

        @Override
        public String getWeatherDesc() {
            return phrase_32char;
        }

        @Override
        public String getTemp() {
            return metric.temp.toString();
        }

        @Override
        public Integer getIcon() {
            return getWeatherImgRes(icon_code);
        }

        @Override
        public Integer getWet() {
            return metric.rh;
        }

        @Override
        public Integer getWindSpeed() {
            return metric.wspd;
        }

        @Override
        public Double getVisit() {
            return metric.vis;
        }

        @Override
        public String getSunrise() {
            return sunrise;
        }

        @Override
        public String getSunset() {
            return sunset;
        }

        @Override
        public Double getPressure() {
            return metric.mslp;
        }
    }

    class Metric {
        private Integer wspd;           //风速
        private Integer gust;           //最大风速
        private Double vis;             //能见度
        private Double mslp;            //压强，毫巴
        private Integer temp;           //温度，摄氏度
        private Integer rh;             //相对湿度，%
        private Integer wc;             //体感温度，综合了温度和风速
    }
}
