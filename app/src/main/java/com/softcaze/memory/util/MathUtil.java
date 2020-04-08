package com.softcaze.memory.util;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.softcaze.memory.model.Position;
import com.softcaze.memory.model.User;

/**
 * Created by Nicolas on 27/01/2020.
 */

public class MathUtil {
    /**
     * Convert dp to pixel
     * @param dp
     * @return
     */
    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Convert pixel to dp
     * @param px
     * @return
     */
    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Convert sp to pixel
     * @param sp
     * @return
     */
    public static int spToPx(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, Resources.getSystem().getDisplayMetrics());
    }
}
