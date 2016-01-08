package com.shellming.sparkathon.util;

import android.util.Base64;

import java.io.File;

/**
 * Created by ruluo1992 on 1/7/2016.
 */
public class WeatherUtil {
    public static void main(String[] args) {
        String dir = "F:\\Courseware\\高级软件工程\\data\\2015-12-25";
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles();
        for(File file : files){
            String encodeName = file.getName();
            System.out.println(Base64.decode(encodeName, Base64.DEFAULT));
        }
    }
}
