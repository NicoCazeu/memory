package com.softcaze.memory.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.res.ResourcesCompat;
import android.widget.TextView;

import com.softcaze.memory.R;

public class UIUtil {
    public static void setTypeFaceText(Context context, TextView... views) {
        for(TextView txtView: views) {
            txtView.setTypeface(ResourcesCompat.getFont(context, R.font.roof_runners_active));
        }
    }
}
