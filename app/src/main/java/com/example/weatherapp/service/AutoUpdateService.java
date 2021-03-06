package com.example.weatherapp.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import com.example.weatherapp.activity.WeatherActivity;
import com.example.weatherapp.receiver.AutoUpdateReceiver;
import com.example.weatherapp.util.MyApplication;
import com.example.weatherapp.util.QueryUtility;

/**
 * Created by Lian on 2017/2/5.
 */

public class AutoUpdateService extends Service {

    private MyApplication myApplication = null;
    private WeatherActivity.MyHandler myHandler = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        myApplication = (MyApplication) getApplication();
        myHandler = myApplication.getHandler();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
                String weatherCode = prefs.getString("city_id", "");
                QueryUtility.queryWeatherInfo(weatherCode, myHandler);
                Looper.loop();
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int anHour = 10 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this, AutoUpdateReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }
}
