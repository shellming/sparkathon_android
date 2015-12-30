package com.shellming.sparkathon.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by ruluo1992 on 11/6/2015.
 */
public class ToastUtil {

    private static Toast mToast;

    public static void showToast(Context context, String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, duration);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }
}
