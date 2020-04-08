package com.softcaze.memory.util;

import android.os.AsyncTask;

/**
 * Created by Nicolas on 26/01/2020.
 */

public class AsyncTaskUtil {
    public static void closeAsyncTask(AsyncTask... asyncs) {
        for(AsyncTask async: asyncs) {
            async.cancel(true);
        }
    }
}
