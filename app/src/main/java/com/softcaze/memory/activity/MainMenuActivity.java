package com.softcaze.memory.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.asynctask.AnimatePlayButtonAsyncTask;
import com.softcaze.memory.model.Bonus;
import com.softcaze.memory.model.Coin;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.AsyncTaskUtil;

public class MainMenuActivity extends AppCompatActivity {

    protected ImageView btnPlay, btnShop, btnChallenge, btnSetting, imgCoin, title;
    protected RelativeLayout footer, header;
    protected LinearLayout linearCoin, linearBonus;
    protected Timer timerPlayBtn;
    protected TextView txtCoin, txtBonus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        btnPlay = (ImageView) findViewById(R.id.btn_play);
        btnShop = (ImageView) findViewById(R.id.btn_shop);
        btnChallenge = (ImageView) findViewById(R.id.btn_challenge);
        btnSetting = (ImageView) findViewById(R.id.btn_setting);
        imgCoin = (ImageView) findViewById(R.id.img_coin);
        title = (ImageView) findViewById(R.id.title);
        footer = (RelativeLayout) findViewById(R.id.footer);
        header = (RelativeLayout) findViewById(R.id.header);
        linearCoin = (LinearLayout) findViewById(R.id.linear_coin);
        linearBonus = (LinearLayout) findViewById(R.id.linear_bonus);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        txtCoin = (TextView) findViewById(R.id.txt_coin);

        /** TODO: Handle Database **/
        if(User.getInstance().getBonus() == null) {
            User.getInstance().setBonus(new Bonus(ApplicationConstants.AMOUNT_BONUS_START));
        }
        if(User.getInstance().getCoin() == null) {
            User.getInstance().setCoin(new Coin(ApplicationConstants.AMOUNT_COIN_START));
        }

        /** Init text field **/
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        Runnable playBtnRunnable = new Runnable() {
            @Override
            public void run() {
                btnPlay.animate().scaleX(1.2F).scaleY(1.2F).alpha(0.8F).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        btnPlay.animate().scaleX(1F).scaleY(1F).alpha(1F).setDuration(300);
                    }
                });
            }
        };

        timerPlayBtn = new Timer(playBtnRunnable, 1500, true);

        /** Manage animations **/
        AnimationUtil.playAnimation(this, R.anim.translate_from_top, title, header, linearCoin, linearBonus);
        AnimationUtil.playAnimation(this, R.anim.translate_from_left, btnPlay);
        AnimationUtil.playAnimation(this, R.anim.translate_from_bottom, footer, btnChallenge, btnSetting, btnShop);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, GameModeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, ShopActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, ChallengeActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, SettingActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
            }
        });
    }
}
