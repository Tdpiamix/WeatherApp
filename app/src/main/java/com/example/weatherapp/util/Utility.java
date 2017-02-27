package com.example.weatherapp.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.example.weatherapp.model.City;
import com.example.weatherapp.model.County;
import com.example.weatherapp.model.Province;
import com.example.weatherapp.model.WeatherDB;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Dictionary;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import static android.R.attr.data;

/**
 * Created by Lian on 2017/1/31.
 */

public class Utility {

    public synchronized static boolean handleProvincesResponse(WeatherDB weatherDB, String response) {
        String[] allProvinces = response.split(",");
        if (allProvinces != null && allProvinces.length > 0) {
            for (String p : allProvinces) {
                String[] array = p.split("\\|");
                Province province = new Province();
                province.setProvinceCode(array[0]);
                province.setProvinceName(array[1]);
                weatherDB.saveProvince(province);
            }
            return true;
        }
        return false;
    }

    public static boolean handleCitiesResponse(WeatherDB weatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities = response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array = c.split("\\|");
                    City city = new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);
                    weatherDB.saveCity(city);
                }
                return true;
            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(WeatherDB weatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCounties = response.split(",");
            if (allCounties != null && allCounties.length > 0) {
                for (String c : allCounties) {
                    String[] array = c.split("\\|");
                    County county = new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    weatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            SharedPreferences.Editor editor = PreferenceManager
                    .getDefaultSharedPreferences(context).edit();
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE", Locale.CHINA);
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(new Date());
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("HeWeather5");
            JSONObject weatherInfo = jsonArray.getJSONObject(0);
            JSONObject aqiInfo = weatherInfo.getJSONObject("aqi").getJSONObject("city");
            editor.putString("aqi", aqiInfo.getString("aqi"));
            editor.putString("qlty", aqiInfo.getString("qlty"));
            editor.putString("city_name", weatherInfo.getJSONObject("basic").getString("city"));
            editor.putString("city_id", weatherInfo.getJSONObject("basic").getString("id"));
            editor.putString("publish_time", weatherInfo.getJSONObject("basic").getJSONObject("update").getString("loc"));
            JSONArray dailyInfo = weatherInfo.getJSONArray("daily_forecast");
            editor.putString("today_date", sdf.format(calendar.getTime()));
            editor.putString("today_tmp_high" , dailyInfo.getJSONObject(0).getJSONObject("tmp").getString("max"));
            editor.putString("today_tmp_low", dailyInfo.getJSONObject(0).getJSONObject("tmp").getString("min"));
            for (int i = 1; i < 3; i++) {
                calendar.add(calendar.DATE, i);
                editor.putString("fcst_date_" + i, sdf.format(calendar.getTime()));
                calendar.add(calendar.DATE, -i);
                editor.putString("cond_" + i, dailyInfo.getJSONObject(i).getJSONObject("cond").getString("txt_d"));
                editor.putString("code_" + i, dailyInfo.getJSONObject(i).getJSONObject("cond").getString("code_d"));
                editor.putString("tmp_high_" + i, dailyInfo.getJSONObject(i).getJSONObject("tmp").getString("max"));
                editor.putString("tmp_low_" + i, dailyInfo.getJSONObject(i).getJSONObject("tmp").getString("min"));
            }



            JSONArray hourlyInfo = weatherInfo.getJSONArray("hourly_forecast");
            for (int i = 0; i < 3; i++) {
                editor.putString(i + "_code", hourlyInfo.getJSONObject(i).getJSONObject("cond").getString("code"));
                String date = hourlyInfo.getJSONObject(i).getString("date");
                String[] array = date.split(" ");
                editor.putString(i + "", array[1]);
                editor.putString(i + "_tmp", hourlyInfo.getJSONObject(i).getString("tmp"));
            }



            JSONObject nowInfo = weatherInfo.getJSONObject("now");
            editor.putString("now_cond", nowInfo.getJSONObject("cond").getString("txt"));
            editor.putString("now_code", nowInfo.getJSONObject("cond").getString("code"));
            editor.putString("now_tmp", nowInfo.getString("tmp"));

            editor.putBoolean("city_selected", true);

            editor.commit();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
