package com.softcaze.memory.asynctask;

import android.os.AsyncTask;
import android.view.View;

import com.softcaze.memory.R;
import com.softcaze.memory.util.AnimationUtil;

/**
 * Created by Nicolas on 26/01/2020.
 */

public class AnimatePlayButtonAsyncTask extends AbstractAsyncTask<Object, Void, Void> {
    @Override
    protected Void doInBackground(Object... objects) {
        boolean animationIsFinished = false;
        if(objects.length > 0) {
            final View view = (View) objects[0];
            while(true) {
                if(isCancelled()) {
                    break;
                }
                try {
                    Thread.sleep(1500);
                    AnimationUtil.playAnimation(view.getContext(), R.anim.play_button, view);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
