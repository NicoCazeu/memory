package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.softcaze.memory.R;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.UIUtil;

public class SplachScreenActivity extends Activity {
    protected InterstitialAd interstitialAd;
    protected TextView loadingTxt;
    protected ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);

        progressBar = (ProgressBar) findViewById(R.id.progressbar_splash);
        loadingTxt = (TextView) findViewById(R.id.loading_txt);
        UIUtil.setTypeFaceText(this, loadingTxt);

        try {
            MobileAds.initialize(this, ApplicationConstants.ID_AD_MOBILE);
        } catch (Exception e) {
            Log.i("SlapchScreenActivity", "Erreur initalize mobile ad : " + e);
        }

        Runnable progressBarLoading = new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                    Intent intent = new Intent(SplachScreenActivity.this, MainMenuActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread thread = new Thread(progressBarLoading);
        thread.start();
    }
}
