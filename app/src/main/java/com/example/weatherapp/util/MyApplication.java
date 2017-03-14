package com.example.weatherapp.util;

import android.app.Application;
import android.content.Context;
import com.example.weatherapp.activity.WeatherActivity.MyHandler;

/**
 * Created by Lian on 2017/3/1.
 */

public class MyApplication extends Application {

    private static Context context;
    private MyHandler handler = null;

    public void setHandler(MyHandler handler) {
        this.handler = handler;
    }

    public MyHandler getHandler() {
        return handler;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
