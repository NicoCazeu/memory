package com.softcaze.memory.asynctask;

import android.os.AsyncTask;
import android.os.Build;

/**
 * Created by Nicolas on 27/01/2020.
 */

public abstract class AbstractAsyncTask<Params, Progress, Result> extends AsyncTask<Object, Object, Object> {
    public void executeAsyncTask(Params param) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) { // Android 3.0 to
            // Android 4.3
            // Parallel AsyncTasks are not possible unless using executeOnExecutor
            executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, param);
        } else { // Below Android 3.0
            // Parallel AsyncTasks are possible, with fixed thread-pool size
            execute(param);
        }
    }
}
