package com.shellming.sparkathon.model;

import com.shellming.sparkathon.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by ruluo1992 on 12/11/2015.
 */
public abstract class AbstractWeather implements Weather{

    protected String formatDate(long time){
        Date d = new Date(time * 1000);
        //  首先按照d(日)格式化获取日期。
        SimpleDateFormat format = new SimpleDateFormat("d");
        String temp = format.format(d);
        //判断日期如果为1结尾且不是11则 用"dd'st'MMMMyyyy,EEEEHH:mm, yyyy"格式,设置语言环境为英语。其它类似。
        if(temp.endsWith("1") && !temp.endsWith("11")){
            format = new SimpleDateFormat("dd'st' MMMM yyyy, EEEE HH:mm", Locale.ENGLISH);
        }else if(temp.endsWith("2") && !temp.endsWith("12")){
            format = new SimpleDateFormat("dd'nd' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }else if(temp.endsWith("3") && !temp.endsWith("13")){
            format = new SimpleDateFormat("dd'rd' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }else{
            format = new SimpleDateFormat("dd'th' MMMM yyyy, EEEE HH:mm",Locale.ENGLISH);
        }
        return format.format(d);
    }

    protected Integer getWeatherImgRes(Integer code){
        Integer[] resources = {
                R.drawable.icon0,
                R.drawable.icon1,
                R.drawable.icon2,
                R.drawable.icon3,
                R.drawable.icon4,
                R.drawable.icon5,
                R.drawable.icon6,
                R.drawable.icon7,
                R.drawable.icon8,
                R.drawable.icon9,
                R.drawable.icon10,
                R.drawable.icon11,
                R.drawable.icon12,
                R.drawable.icon13,
                R.drawable.icon14,
                R.drawable.icon15,
                R.drawable.icon16,
                R.drawable.icon17,
                R.drawable.icon18,
                R.drawable.icon19,
                R.drawable.icon20,
                R.drawable.icon21,
                R.drawable.icon22,
                R.drawable.icon23,
                R.drawable.icon24,
                R.drawable.icon25,
                R.drawable.icon26,
                R.drawable.icon27,
                R.drawable.icon28,
                R.drawable.icon29,
                R.drawable.icon30,
                R.drawable.icon31,
                R.drawable.icon32,
                R.drawable.icon33,
                R.drawable.icon34,
                R.drawable.icon35,
                R.drawable.icon36,
                R.drawable.icon37,
                R.drawable.icon38,
                R.drawable.icon39,
                R.drawable.icon40,
                R.drawable.icon41,
                R.drawable.icon42,
                R.drawable.icon43,
                R.drawable.icon44,
                R.drawable.icon45,
                R.drawable.icon46,
                R.drawable.icon47
        };
        if(code == null || code < 0 || code > 47)
            return R.drawable.icon_wet;
        return resources[code];
    }
}
