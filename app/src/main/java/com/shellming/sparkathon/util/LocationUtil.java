package com.shellming.sparkathon.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.shellming.sparkathon.api.GoogleMapApi;
import com.shellming.sparkathon.api.WeatherApi;
import com.shellming.sparkathon.constant.GlobalConstant;

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by ruluo1992 on 11/30/2015.
 */
public class LocationUtil {

    public static ReadWriteLock lock = new ReentrantReadWriteLock(false);

    public static void getLocation(Context context) {
//        lock.writeLock().lock();
        try {
            // 获取位置管理服务
            LocationManager locationManager;
            String serviceName = Context.LOCATION_SERVICE;
            locationManager = (LocationManager) context.getSystemService(serviceName);

            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                System.out.println("!!!!!!!!!! have no permission");
                return;
            }

            Location location = getLastKnownLocation(locationManager);
            System.out.println("!!!!!!!!!!!!!!!!!!!!!! altitude" + location.getAltitude());
            System.out.println("!!!!!!!!!!!!!!!!!!!!! logitude:" + location.getLongitude());

            GlobalConstant.latitude = String.format("%.2f", location.getLatitude());
            GlobalConstant.logitude = String.format("%.2f", location.getLongitude());
        }catch (Exception e){
            GlobalConstant.latitude = "34.53";
            GlobalConstant.logitude = "84.50";
            ToastUtil.showToast(context, "Get Location Error", Toast.LENGTH_SHORT);
        }
//        lock.writeLock().unlock();
//        WeatherApi.getCurrentWeather(GlobalConstant.latitude, GlobalConstant.logitude);
//        WeatherApi.getTendayForecast(GlobalConstant.latitude, GlobalConstant.logitude);
        GoogleMapApi.getCityName(GlobalConstant.latitude, GlobalConstant.logitude);
    }

    private static Location getLastKnownLocation(LocationManager mLocationManager) {
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}
