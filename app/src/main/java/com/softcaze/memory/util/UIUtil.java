package com.softcaze.memory.util;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.TextView;

import com.softcaze.memory.R;

import androidx.core.content.res.ResourcesCompat;

public class UIUtil {
    public static void setTypeFaceText(Context context, TextView... views) {
        for(TextView txtView: views) {
            txtView.setTypeface(ResourcesCompat.getFont(context, R.font.roof_runners_active));
        }
    }
}
