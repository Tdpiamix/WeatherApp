package com.example.weatherapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.weatherapp.R;
import com.example.weatherapp.service.AutoUpdateService;
import com.example.weatherapp.util.MyApplication;
import com.example.weatherapp.util.QueryUtility;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import java.util.Calendar;
import static com.example.weatherapp.util.QueryUtility.queryAQIInfo;

/**
 * Created by Lian on 2017/2/4.
 */

public class WeatherActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String APIKey = "d4ff76f262dd43c09e327baad5f95fbd";
    private static final int GET_INFO_FAIL = 0;
    private static final int WEATHER_SUCCEED = 1;
    private LinearLayout bgLayout;
    private RelativeLayout selectCity,
            btnAQI;

    private PullToRefreshScrollView pullToRefreshScrollView;
    private ScrollView scrollView;
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

    private MyHandler myHandler = null;
    private MyApplication myApplication = null;

    public class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case WEATHER_SUCCEED:
                    /*Toast.makeText(WeatherActivity.this, "service test ok", Toast.LENGTH_SHORT).show();*/
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_weather);

        bgLayout = (LinearLayout) findViewById(R.id.bg_layout);
        selectCity = (RelativeLayout) findViewById(R.id.select_city);
        btnAQI = (RelativeLayout) findViewById(R.id.btn_aqi);
        pullToRefreshScrollView = (PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
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

        myHandler = new MyHandler();
        myApplication = (MyApplication) getApplication();
        myApplication.setHandler(myHandler);

        String countyCode = getIntent().getStringExtra("county_code");

        if (!TextUtils.isEmpty(countyCode)) {
            tvPublishTime.setText("同步中...");
            /*todayInfoLayout.setVisibility(View.INVISIBLE);
            forecastInfoLayout.setVisibility(View.INVISIBLE);*/
            Toast.makeText(myApplication, countyCode, Toast.LENGTH_SHORT).show();
            tvCityName.setVisibility(View.INVISIBLE);
            QueryUtility.queryWeatherCode(countyCode, myHandler);
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        } else {
            showWeather();
            Intent intent = new Intent(this, AutoUpdateService.class);
            startService(intent);
        }
        selectCity.setOnClickListener(this);
        btnAQI.setOnClickListener(this);
        pullToRefreshScrollView.getLoadingLayoutProxy().setPullLabel("下拉刷新");
        pullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel("释放刷新");
        pullToRefreshScrollView.getLoadingLayoutProxy().setRefreshingLabel("正在刷新...");
        pullToRefreshScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ScrollView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
                new GetDataTask().execute();
            }
        });
        scrollView = pullToRefreshScrollView.getRefreshableView();
    }

    private class GetDataTask extends AsyncTask<Void, Void, String[]> {
        @Override
        protected String[] doInBackground(Void... params) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
            String weatherCode = prefs.getString("city_id", "");
            if (!TextUtils.isEmpty(weatherCode)) {
                QueryUtility.queryWeatherInfo(weatherCode, myHandler);
            }
            return null;
        }

        @Override
        protected void onPostExecute(String[] result) {
            pullToRefreshScrollView.onRefreshComplete();
            super.onPostExecute(result);
        }
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
            case R.id.btn_aqi:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
                String weatherCode = prefs.getString("city_id", "");
                queryAQIInfo(weatherCode, myHandler);
                Intent intent2 = new Intent(this, AQIActivity.class);
                startActivity(intent2);
                finish();
                break;
            default:
                break;
        }
    }

    private void showWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        if (hour >= 18) {
            bgLayout.setBackgroundResource(R.drawable.bg_night);
        } else {
            bgLayout.setBackgroundResource(R.drawable.bg_day);
        }
        tvCityName.setText(prefs.getString("city_name", "0"));
        tvPublishTime.setText("" + System.currentTimeMillis());
        tvNowTemperature.setText(prefs.getString("now_tmp", "0"));
        ivNowCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("now_code", ""), "drawable", getPackageName()));
        tvNowCondition.setText(prefs.getString("now_cond", "0"));
        tvAQI.setText("空气质量 " + prefs.getString("qlty", "0") + " " + prefs.getString("aqi", "0"));
        tvTodayDate.setText(prefs.getString("today_date", "0"));
        tvTodayTemperature.setText(prefs.getString("today_tmp_high", "0") + "° / " + prefs.getString("today_tmp_low", "0") + "°");
        tv10Am.setText(prefs.getString("0", ""));
        tv1Pm.setText(prefs.getString("1", ""));
        tv4Pm.setText(prefs.getString("2", ""));
        tv7Pm.setText(prefs.getString("3", ""));
        tv10Pm.setText(prefs.getString("4", ""));
        iv10AmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("0_code", ""), "drawable", getPackageName()));
        iv1PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("1_code", ""), "drawable", getPackageName()));
        iv4PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("2_code", ""), "drawable", getPackageName()));
        iv7PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("3_code", ""), "drawable", getPackageName()));
        iv10PmCondition.setImageResource(getResources().getIdentifier("icon" + prefs.getString("4_code", ""), "drawable", getPackageName()));
        tv10AmTemperature.setText(prefs.getString("0_tmp", ""));
        tv1PmTemperature.setText(prefs.getString("1_tmp", ""));
        tv4PmTemperature.setText(prefs.getString("2_tmp", ""));
        tv7PmTemperature.setText(prefs.getString("3_tmp", ""));
        tv10PmTemperature.setText(prefs.getString("4_tmp", ""));
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