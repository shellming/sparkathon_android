package com.shellming.sparkathon.model;

import com.shellming.sparkathon.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public class WeatherForecastDaily implements WeatherResponse{
    private MetaData metadata;
    private List<ForecastDaily> forecasts;

    @Override
    public List<Weather> getWeatherData() {
        List<Weather> result = new ArrayList<>();
        result.addAll(forecasts);
        return result;
    }

    class ForecastDay{
        private Integer icon_code;      //图标标号
        private Integer wspd;           //风速
        private Integer gust;           //最大风速
        private Double vis;             //能见度
        private Double mslp;            //压强，毫巴
        private Integer temp;           //温度，摄氏度
        private Integer rh;             //相对湿度，%
        private String phrase_32char;     //天气描述，可以指定语言
    }

    class ForecastDaily extends AbstractWeather implements Weather{
        private Long expire_time_gmt;     //过期时间
        private Long fcst_valid;          // 预测时间
        private String sunrise;         //日出时间，Example: 20130523T06:32:00-04:00
        private String sunset;          //日落时间，Example: 20130523T20:37:00-04:00


        private Integer max_temp;
        private Integer min_temp;

        private ForecastDay day;


        @Override
        public String getFormatDate() {
            return formatDate(fcst_valid);
        }

        @Override
        public String getWeatherDesc() {
            return day.phrase_32char;
        }

        @Override
        public String getTemp() {
            return min_temp + "~" + max_temp;
        }

        @Override
        public Integer getIcon() {
            return getWeatherImgRes(day.icon_code);
        }

        @Override
        public Integer getWet() {
            return day.rh;
        }

        @Override
        public Integer getWindSpeed() {
            return day.wspd;
        }

        @Override
        public Double getVisit() {
            return day.vis;
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
            return day.mslp;
        }
    }
}
