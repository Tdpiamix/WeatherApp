package com.example.weatherapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.weatherapp.R;
import java.util.Calendar;

/**
 * Created by Lian on 2017/3/4.
 */

public class AQIActivity extends AppCompatActivity {

    private LinearLayout bgLayout;
    private TextView tvPM10,
            tvPM25,
            tvNO2,
            tvSO2,
            tvO3,
            tvCO;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_aqi);

        bgLayout = (LinearLayout) findViewById(R.id.bg_layout);
        tvPM10 = (TextView) findViewById(R.id.tv_pm10);
        tvPM25 = (TextView) findViewById(R.id.tv_pm25);
        tvNO2 = (TextView) findViewById(R.id.tv_no2);
        tvSO2 = (TextView) findViewById(R.id.tv_so2);
        tvO3 = (TextView) findViewById(R.id.tv_o3);
        tvCO = (TextView) findViewById(R.id.tv_co);
        showAQI();
    }

    private void showAQI() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 18) {
            bgLayout.setBackgroundResource(R.drawable.bg_night);
        } else {
            bgLayout.setBackgroundResource(R.drawable.bg_day);
        }

        tvPM10.setText(prefs.getString("pm10", "0") + " μg/m3");
        tvPM25.setText(prefs.getString("pm25", "0") + " μg/m3");
        tvNO2.setText(prefs.getString("no2", "0") + " μg/m3");
        tvSO2.setText(prefs.getString("so2", "0") + " μg/m3");
        tvO3.setText(prefs.getString("o3", "0") + " μg/m3");
        tvCO.setText(prefs.getString("co", "0") + " μg/m3");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
        finish();
    }
}
