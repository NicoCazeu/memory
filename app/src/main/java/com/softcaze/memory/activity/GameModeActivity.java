package com.softcaze.memory.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.model.CardTheme;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModeActivity extends AppCompatActivity {

    private TextView btnCareer, btnAgainstTime, btnSuddenDeath, btnSurvival, txtCoin, txtBonus;
    private ImageView imgCoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_mode);

        btnCareer = (TextView) findViewById(R.id.btn_career);
        btnAgainstTime = (TextView) findViewById(R.id.btn_against_time);
        btnSuddenDeath = (TextView) findViewById(R.id.btn_sudden_death);
        btnSurvival = (TextView) findViewById(R.id.btn_survival);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        imgCoin = (ImageView) findViewById(R.id.img_coin);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");

        btnCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.CAREER);

                List<LevelRow> levelRowList = new ArrayList<>();
                LevelRow levelRow = new LevelRow(new Level(1, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT), new Level(2, 2, LevelState.UNLOCK, 6, CardTheme.FRUTT), new Level(3, 2, LevelState.UNLOCK, 8, CardTheme.FRUTT));
                LevelRow levelRow2 = new LevelRow(new Level(4, 0, LevelState.UNLOCK, 10, CardTheme.FRUTT), new Level(5, 2, LevelState.UNLOCK, 16, CardTheme.FRUTT), new Level(6, 1, LevelState.UNLOCK, 14, CardTheme.FRUTT));
                LevelRow levelRow3 = new LevelRow(new Level(7, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new Level(8, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new Level(9, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT));
                LevelRow levelRow4 = new LevelRow(new Level(10, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new Level(11, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new Level(12, 0, LevelState.UNLOCK, 4, CardTheme.FLAG));
                LevelRow levelRow5 = new LevelRow(new Level(13, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new Level(14, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new Level(15, 0, LevelState.UNLOCK, 4, CardTheme.FLAG));
                LevelRow levelRow6 = new LevelRow(new Level(16, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new Level(17, 2, LevelState.LOCK, 20, CardTheme.FLAG), new Level(18, 0, LevelState.LOCK, 4, CardTheme.FLAG));
                levelRow.getLevel3().setScoreLimit(Arrays.asList(new Integer[]{12, 10, 8}));
                levelRow.getLevel1().setScoreLimit(Arrays.asList(new Integer[]{8,6,4}));
                levelRowList.add(levelRow);
                levelRowList.add(levelRow2);
                levelRowList.add(levelRow3);
                levelRowList.add(levelRow4);
                levelRowList.add(levelRow5);
                levelRowList.add(levelRow6);

                List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
                GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.CAREER);

                if(ApplicationConstants.needTutorialCareer) {
                    Intent intent = new Intent(GameModeActivity.this, TutorialActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
                    return;
                }

                Intent intent = new Intent(GameModeActivity.this, LevelListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnAgainstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.AGAINST_TIME);

                List<LevelRow> levelRowList = new ArrayList<>();
                LevelRow levelRow = new LevelRow(new Level(1, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT), new Level(2, 2, LevelState.UNLOCK, 6, CardTheme.FRUTT), new Level(3, 2, LevelState.UNLOCK, 8, CardTheme.FRUTT));
                levelRowList.add(levelRow);

                List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
                GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.AGAINST_TIME);

                if(ApplicationConstants.needTutorialAgainsTime) {
                    Intent intent = new Intent(GameModeActivity.this, TutorialActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
                    return;
                }

                Intent intent = new Intent(GameModeActivity.this, LevelListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnSurvival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.SURVIVAL);
                Intent intent = new Intent(GameModeActivity.this, LevelListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnSuddenDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.SUDDEN_DEATH);
                Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
                intent.putExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL, String.valueOf(1));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent = new Intent(GameModeActivity.this, MainMenuActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) this).toBundle());
    }
}
