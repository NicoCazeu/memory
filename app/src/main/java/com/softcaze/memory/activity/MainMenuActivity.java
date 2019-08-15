package com.softcaze.memory.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.softcaze.memory.R;

public class MainMenuActivity extends AppCompatActivity {

    protected ImageView btnPlay;
    protected ImageView btnShop;
    protected ImageView btnChallenge;
    protected ImageView btnSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnShop = (ImageView) findViewById(R.id.btn_shop);
        btnChallenge = (ImageView) findViewById(R.id.btn_challenge);
        btnSetting = (ImageView) findViewById(R.id.btn_setting);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, GameModeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ShopActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, ChallengeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainMenuActivity.this, SettingActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });
    }
}
