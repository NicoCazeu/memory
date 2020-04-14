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
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.model.AgainstTimeLevel;
import com.softcaze.memory.model.Bonus;
import com.softcaze.memory.model.CardTheme;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.Coin;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.LifeLevel;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.AsyncTaskUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    protected ImageView btnPlay, btnShop, btnChallenge, btnSetting, imgCoin, title;
    protected RelativeLayout footer, header;
    protected LinearLayout linearCoin, linearBonus;
    protected Timer timerPlayBtn;
    protected TextView txtCoin, txtBonus;
    protected Dao dao;

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

        dao = new Dao(this);

        initLevels();
        try {
            dao.open();

            dao.initUserDatabase();
            dao.initLevelsDatabase();
            User.getInstance().setCoin(new Coin(dao.getCoinUser()));
            User.getInstance().setBonus((new Bonus(dao.getBonusRevealUser())));
        } catch(Exception e) {

        } finally {
            dao.close();

            if(User.getInstance().getBonus() == null) {
                User.getInstance().setBonus(new Bonus(ApplicationConstants.AMOUNT_BONUS_START));
            }
            if(User.getInstance().getCoin() == null) {
                User.getInstance().setCoin(new Coin(ApplicationConstants.AMOUNT_COIN_START));
            }
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

    private void initLevels() {
        /**
         * Career levle
         */
        List<LevelRow> levelRowList = new ArrayList<>();
        LevelRow levelRow = new LevelRow(new CareerLevel(1, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT), new CareerLevel(2, 0, LevelState.UNLOCK, 6, CardTheme.FRUTT), new CareerLevel(3, 0, LevelState.UNLOCK, 8, CardTheme.FRUTT));
        LevelRow levelRow2 = new LevelRow(new CareerLevel(4, 0, LevelState.UNLOCK, 10, CardTheme.FRUTT), new CareerLevel(5, 0, LevelState.UNLOCK, 16, CardTheme.FRUTT), new CareerLevel(6, 0, LevelState.UNLOCK, 14, CardTheme.FRUTT));
        LevelRow levelRow3 = new LevelRow(new CareerLevel(7, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new CareerLevel(8, 0, LevelState.UNLOCK, 20, CardTheme.FLAG), new CareerLevel(9, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT));
        LevelRow levelRow4 = new LevelRow(new CareerLevel(10, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(11, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(12, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        LevelRow levelRow5 = new LevelRow(new CareerLevel(13, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(14, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(15, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        LevelRow levelRow6 = new LevelRow(new CareerLevel(16, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(17, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(18, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        ((CareerLevel) levelRow.getLevel3()).setScoreLimit(Arrays.asList(new Integer[]{12, 10, 8}));
        ((CareerLevel) levelRow.getLevel1()).setScoreLimit(Arrays.asList(new Integer[]{8,6,4}));

        levelRowList.add(levelRow);
        levelRowList.add(levelRow2);
        levelRowList.add(levelRow3);
        levelRowList.add(levelRow4);
        levelRowList.add(levelRow5);
        levelRowList.add(levelRow6);

        List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.CAREER);

        /**
         * Against time levle
         */
        levelRowList = new ArrayList<>();
        levelRow = new LevelRow(new AgainstTimeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 15), new AgainstTimeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 9), new AgainstTimeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 12));
        LevelRow levelRow1 = new LevelRow(new AgainstTimeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 12), new AgainstTimeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 14), new AgainstTimeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 20));
        levelRowList.add(levelRow);
        levelRowList.add(levelRow1);

        listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.AGAINST_TIME);

        /**
         * Survival level
         */
        levelRowList = new ArrayList<>();
        levelRow = new LevelRow(new LifeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 3), new LifeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 3), new LifeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 3));
        levelRow1 = new LevelRow(new LifeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 3), new LifeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 3), new LifeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 3));
        levelRowList.add(levelRow);
        levelRowList.add(levelRow1);

        listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SURVIVAL);

        /**
         * Sudden death level
         */
        levelRowList = new ArrayList<>();
        levelRow = new LevelRow(new LifeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 1), new LifeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 1), new LifeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 1));
        levelRow1 = new LevelRow(new LifeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 1), new LifeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 1), new LifeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 1));
        levelRowList.add(levelRow);
        levelRowList.add(levelRow1);

        listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SUDDEN_DEATH);
    }
}
