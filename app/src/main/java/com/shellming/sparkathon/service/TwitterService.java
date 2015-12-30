package com.shellming.sparkathon.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by ruluo1992 on 12/15/2015.
 */
public class TwitterService extends Service {

    private Handler handler;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                System.out.println("!!!!!!!!!!!! one task");
            }
        }, 2000);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
