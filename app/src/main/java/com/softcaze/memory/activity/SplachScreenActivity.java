package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.softcaze.memory.R;
import com.softcaze.memory.util.ApplicationConstants;

public class SplachScreenActivity extends Activity {
    protected InterstitialAd interstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splach_screen);

        try {
            MobileAds.initialize(this, ApplicationConstants.ID_AD_MOBILE);
        } catch (Exception e) {
            Log.i("SlapchScreenActivity", "Erreur initalize mobile ad : " + e);
        }

        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId(ApplicationConstants.ID_AD_INTERSTITIAL_TEST);

        AdRequest adRequest = new AdRequest.Builder().build();
        interstitialAd.loadAd(adRequest);
        interstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Intent intent = new Intent(SplachScreenActivity.this, MainMenuActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(SplachScreenActivity.this).toBundle());

                finish();
            }

            @Override
            public void onAdClosed() {
                Intent intent = new Intent(SplachScreenActivity.this, MainMenuActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }
}
