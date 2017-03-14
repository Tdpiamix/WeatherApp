package com.example.weatherapp.util;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import static com.example.weatherapp.activity.WeatherActivity.APIKey;

/**
 * Created by Lian on 2017/2/28.
 */

public class QueryUtility {

    private static final int GET_INFO_FAIL = 0;

    public static void queryWeatherCode(String countyCode, Handler mHandler) {
        String address = "http://www.weather.com.cn/data/list3/city" + countyCode + ".xml";
        queryFromServer(address, "countyCode", mHandler);
    }

    public static void queryAQIInfo(String weatherCode, Handler mHandler) {
        String address = "https://free-api.heweather.com/v5/weather?city=" + weatherCode + "&key=" + APIKey;
        queryFromServer(address, "AQIInfo", mHandler);
    }

    public static void queryWeatherInfo(String weatherCode, Handler mHandler) {
        String address = "https://free-api.heweather.com/v5/weather?city=" + weatherCode + "&key=" + APIKey;
        queryFromServer(address, "weatherInfo", mHandler);
    }

    private static void queryFromServer(final String address, final String type, final Handler mHandler) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(final String response) {
                if ("countyCode".equals(type)) {
                    if (!TextUtils.isEmpty(response)) {
                        String[] array = response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode = "CN" + array[1];
                            queryWeatherInfo(weatherCode, mHandler);
                        }
                    }
                } else if ("weatherInfo".equals(type)) {
                    Utility.handleWeatherResponse(MyApplication.getContext(), response, mHandler);
                } else if ("AQIInfo".equals(type)) {
                    Utility.handleAQIResponse(MyApplication.getContext(), response, mHandler);
                }
            }

            @Override
            public void onError(Exception e) {
                Message message = new Message();
                message.what = GET_INFO_FAIL;
                mHandler.sendMessage(message);
            }
        });
    }
}
