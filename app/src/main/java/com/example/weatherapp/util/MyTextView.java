package com.example.weatherapp.util;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * Created by Lian on 2017/3/10.
 */

public class MyTextView extends AppCompatTextView {
    public MyTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setTypeface(FontCustom.setFont(context));
    }
}
