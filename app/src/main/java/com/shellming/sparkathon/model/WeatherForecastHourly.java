package com.shellming.sparkathon.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ruluo1992 on 12/30/2015.
 */
public class WeatherForecastHourly implements WeatherResponse {
    private MetaData metadata;
    private List<ForecastHourly> forecasts;

    @Override
    public List<Weather> getWeatherData() {
        List<Weather> result = new ArrayList<>();
        result.addAll(forecasts);
        return result;
    }

    class ForecastHourly extends AbstractWeather implements Weather{
        private Long expire_time_gmt;     //过期时间
        private Long fcst_valid;          // 预测时间
        private Integer wspd;           //风速
        private Double vis;             //能见度
        private Double mslp;            //压强，毫巴
        private Integer temp;           //温度，摄氏度
        private Integer rh;             //相对湿度，%
        private String phrase_32char;     //天气描述，可以指定语言

        @Override
        public String getFormatDate() {
            Date d = new Date(fcst_valid * 1000);
            //  首先按照d(日)格式化获取日期。
            SimpleDateFormat format = new SimpleDateFormat("HH");
            String temp = format.format(d);
            return temp;
        }

        @Override
        public String getWeatherDesc() {
            return phrase_32char;
        }

        @Override
        public String getTemp() {
            return temp.toString();
        }

        @Override
        public Integer getIcon() {
            return null;
        }

        @Override
        public Integer getWet() {
            return rh;
        }

        @Override
        public Integer getWindSpeed() {
            return wspd;
        }

        @Override
        public Double getVisit() {
            return vis;
        }

        @Override
        public String getSunrise() {
            return null;
        }

        @Override
        public String getSunset() {
            return null;
        }

        @Override
        public Double getPressure() {
            return mslp;
        }
    }

}
