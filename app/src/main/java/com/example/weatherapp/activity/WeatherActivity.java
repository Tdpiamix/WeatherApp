package com.example.weatherapp.activity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.weatherapp.R;
import com.example.weatherapp.service.AutoUpdateService;
import com.example.weatherapp.util.HttpCallbackListener;
import com.example.weatherapp.util.HttpUtil;
import com.example.weatherapp.util.MyApplication;
import com.example.weatherapp.util.QueryUtility;
import com.example.weatherapp.util.Utility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import org.w3c.dom.Text;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Lian on 2017/2/4.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APIKey = "d4ff76f262dd43c09e327baad5f95fbd";
    private static final int GET_INFO_FAIL = 0;
    private static final int QUERY_SUCCEED = 1;
    private RelativeLayout selectCity,
            btnAQI;
            ;
    private TextView tvCityName,
            tvPublishTime,
            tvNowTemperature,
            tvNowCondition,
            tvAQI,
            tvTodayDate,
            tvTodayTemperature,
            tv10Am,
            tv1Pm,
            tv4Pm,
            tv7Pm,
            tv10Pm,
            tv10AmTemperature,
            tv1PmTemperature,
            tv4PmTemperature,
            tv7PmTemperature,
            tv10PmTemperature,
            tvTomorrowDate,
            tvTomorrowTemperatureHigh,
            tvTomorrowTemperatureLow,
            tvThirddayDate,
            tvThirddayTemperatureHigh,
            tvThirddayTemperatureLow;
    private ImageView ivNowCondition,
            iv10AmCondition,
            iv1PmCondition,
            iv4PmCondition,
            iv7PmCondition,
            iv10PmCondition,
            ivTomorrowCondition,
            ivThirddayCondition;
    private Button btnRefreshWeather;

    
    private MyHandler myHandler = null;
    private MyApplication myApplication = null;

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case QUERY_SUCCEED:
                    Toast.makeText(WeatherActivity.this, "service test ok", Toast.LENGTH_SHORT).show();
                    showWeather();
                    break;
                case GET_INFO_FAIL:
                    Toast.makeText(WeatherActivity.this, "get info fail", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    }

    /*ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Messenger messenger = new Messenger(mHandler);

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_weather);

        selectCity = (RelativeLayout) findViewById(R.id.select_city);
        btnAQI = (RelativeLayout) findViewById(R.id.btn_aqi);

        tvCityName = (TextView) findViewById(R.id.tv_city_name);
        tvPublishTime = (TextView) findViewById(R.id.tv_publish_time);
        tvNowTemperature = (TextView) findViewById(R.id.tv_now_tmp);
        tvNowCondition = (TextView) findViewById(R.id.tv_now_cond);
        tvAQI = (TextView) findViewById(R.id.tv_aqi);
        tvTodayDate = (TextView) findViewById(R.id.tv_today_date);
        tvTodayTemperature = (TextView) findViewById(R.id.tv_today_tmp);
        tv10Am = (TextView) findViewById(R.id.tv_10am);
        tv1Pm = (TextView) findViewById(R.id.tv_1pm);
        tv4Pm = (TextView) findViewById(R.id.tv_4pm);
        tv7Pm = (TextView) findViewById(R.id.tv_7pm);
        tv10Pm = (TextView) findViewById(R.id.tv_10pm);
        tv10AmTemperature = (TextView) findViewById(R.id.tv_10am_tmp);
        tv1PmTemperature = (TextView) findViewById(R.id.tv_1pm_tmp);
        tv4PmTemperature = (TextView) findViewById(R.id.tv_4pm_tmp);
        tv7PmTemperature = (TextView) findViewById(R.id.tv_7pm_tmp);
        tv10PmTemperature = (TextView) findViewById(R.id.tv_10pm_tmp);
        tvTomorrowDate = (TextView) findViewById(R.id.tv_tomorrow_date);
        tvTomorrowTemperatureHigh = (TextView) findViewById(R.id.tv_tomorrow_tmp_high);
        tvTomorrowTemperatureLow = (TextView) findViewById(R.id.tv_tomorrow_tmp_low);
        tvThirddayDate = (TextView) findViewById(R.id.tv_thirdday_date);
        tvThirddayTemperatureHigh = (TextView) findViewById(R.id.tv_thirdday_tmp_high);
        tvThirddayTemperatureLow = (TextView) findViewById(R.id.tv_thirdday_tmp_low);
        ivNowCondition = (ImageView) findViewById(R.id.iv_now_cond);
        iv10AmCondition = (ImageView) findViewById(R.id.iv_10am_cond);
        iv1PmCondition = (ImageView) findViewById(R.id.iv_1pm_cond);
        iv4PmCondition = (ImageView) findViewById(R.id.iv_4pm_cond);
        iv7PmCondition = (ImageView) findViewById(R.id.iv_7pm_cond);
        iv10PmCondition = (ImageView) findViewById(R.id.iv_10pm_cond);
        ivTomorrowCondition = (ImageView) findViewById(R.id.iv_tomorrow_cond);
        ivThirddayCondition = (ImageView) findViewById(R.id.iv_thirdday_cond);
        btnRefreshWeather = (Button) findViewById(R.id.btn_refresh_weather);

        myHandler = new MyHandler();
        myApplication = (MyApplication) getApplication();
        myApplication.setHandler(myHandler);


        String countyCode = getIntent().getStringExtra("county_code");
        Toast.makeText(myApplication, countyCode, Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(countyCode)) {
            tvPublishTime.setText("同步中...");
            /*todayInfoLayout.setVisibility(View.INVISIBLE);
            forecastInfoLayout.setVisibility(View.INVISIBLE);*/
            tvCityName.setVisibility(View.INVISIBLE);
            QueryUtility.queryWeatherCode(countyCode, myHandler);


        } else {
            showWeather();

        }
        selectCity.setOnClickListener(this);
        btnRefreshWeather.setOnClickListener(this);
        btnAQI.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_city:
                Intent intent = new Intent(this, ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_refresh_weather:
                tvPublishTime.setText("同步中...");
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("city_id", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    QueryUtility.queryWeatherInfo(weatherCode, myHandler);
                }
                break;
            case R.id.btn_aqi:
                SharedPreferences prefs2 = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode2 = prefs2.getString("city_id", "");
                Intent intent2 = new Intent(this, AQIActivity.class);
                intent2.putExtra("weatherCode", weatherCode2);
                startActivity(intent2);
                finish();
                break;
            default:
                break;
        }
    }

    /*private void queryWeatherCode(String countyCode ) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    private void queryWeatherInfo(String weatherCode) {
        String address = "https://free-api.heweather.com/v5/weather?city=" + weatherCode + "&key=" + APIKey;
        queryFromServer(address, "weatherInfo");
    }

    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = "CN" + array[1];
                            queryWeatherInfo(weatherCode);
                        }
                    }
                } else if ("weatherInfo".equals(type)) {
                    Utility.handleWeatherResponse(WeatherActivity.this, response, mHandler);

                }runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showWeather();
                    }
                });
            }

            @Override
            public void onError(Exception e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tvPublishTime.setText("同步失败");
                    }
                });
            }
        });
    }*/

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        tvCityName.setText(prefs.getString("city_name", "0"));
        tvPublishTime.setText("" + System.currentTimeMillis());
        tvNowTemperature.setText(prefs.getString("now_tmp", "0"));
        ivNowCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("now_code", ""), "drawable", getPackageName()));
        tvNowCondition.setText(prefs.getString("now_cond", "0"));
        tvAQI.setText("空气质量 " + prefs.getString("qlty", "0") + " " + prefs.getString("aqi", "0"));
        tvTodayDate.setText(prefs.getString("today_date", "0"));
        tvTodayTemperature.setText(prefs.getString("today_tmp_high", "0") + "° / " + prefs.getString("today_tmp_low", "0") + "°");
        tv10Am.setText(prefs.getString("0", "0"));
        tv1Pm.setText(prefs.getString("1", "0"));
        tv4Pm.setText(prefs.getString("2", "0"));
        tv7Pm.setText(prefs.getString("3", "0"));
        tv10Pm.setText(prefs.getString("4", "0"));
        iv10AmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("0_code", ""), "drawable", getPackageName()));
        iv1PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("1_code", ""), "drawable", getPackageName()));
        iv4PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("2_code", ""), "drawable", getPackageName()));
        iv7PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("3_code", ""), "drawable", getPackageName()));
        iv10PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("4_code", ""), "drawable", getPackageName()));
        tv10AmTemperature.setText(prefs.getString("0_tmp", "0") + "°");
        tv1PmTemperature.setText(prefs.getString("1_tmp", "0") + "°");
        tv4PmTemperature.setText(prefs.getString("2_tmp", "0") + "°");
        tv7PmTemperature.setText(prefs.getString("3_tmp", "0") + "°");
        tv10PmTemperature.setText(prefs.getString("4_tmp", "0") + "°");
        tvTomorrowDate.setText(prefs.getString("fcst_date_1", ""));
        ivTomorrowCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("code_1", ""), "drawable", getPackageName()));
        tvTomorrowTemperatureHigh.setText(prefs.getString("tmp_high_1", "0") + "°");
        tvTomorrowTemperatureLow.setText(prefs.getString("tmp_low_1", "0") + "°");
        tvThirddayDate.setText(prefs.getString("fcst_date_2", "0"));
        ivThirddayCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("code_2", ""), "drawable", getPackageName()));
        tvThirddayTemperatureHigh.setText(prefs.getString("tmp_high_2", "0") + "°");
        tvThirddayTemperatureLow.setText(prefs.getString("tmp_low_2", "0") + "°");

        /*todayInfoLayout.setVisibility(View.VISIBLE);
        forecastInfoLayout.setVisibility(View.VISIBLE);*/

        tvCityName.setVisibility(View.VISIBLE);

    }
}