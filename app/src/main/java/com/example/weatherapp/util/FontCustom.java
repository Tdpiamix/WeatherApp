package com.example.weatherapp.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Lian on 2017/3/10.
 */

public class FontCustom {
    private static String fongUrl = "KozGoPr6N-Light.otf";
    private static Typeface tf;

    public static Typeface setFont(Context context) {
        if (tf == null) {
            tf = Typeface.createFromAsset(context.getAssets(), fongUrl);
        }
        return tf;
    }
}
