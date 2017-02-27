package com.example.weatherapp.util;

/**
 * Created by Lian on 2017/1/31.
 */

public interface HttpCallbackListener {

    void onFinish(String response);

    void onError(Exception e);
}
