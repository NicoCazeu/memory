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
import com.softcaze.memory.model.AgainstTimeLevel;
import com.softcaze.memory.model.CardTheme;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelRow;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.LifeLevel;
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
        if(User.getInstance().getBonus() == null) {
            txtBonus.setText(0 + "");
        } else {
            txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        }

        if(User.getInstance().getCoin() == null) {
            txtCoin.setText(0 + "");
        } else {
            txtCoin.setText(User.getInstance().getCoin().getAmount() + "");
        }

        btnCareer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.CAREER);

                List<LevelRow> levelRowList = new ArrayList<>();
                LevelRow levelRow = new LevelRow(new CareerLevel(1, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT), new CareerLevel(2, 2, LevelState.UNLOCK, 6, CardTheme.FRUTT), new CareerLevel(3, 2, LevelState.UNLOCK, 8, CardTheme.FRUTT));
                LevelRow levelRow2 = new LevelRow(new CareerLevel(4, 0, LevelState.UNLOCK, 10, CardTheme.FRUTT), new CareerLevel(5, 2, LevelState.UNLOCK, 16, CardTheme.FRUTT), new CareerLevel(6, 1, LevelState.UNLOCK, 14, CardTheme.FRUTT));
                LevelRow levelRow3 = new LevelRow(new CareerLevel(7, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new CareerLevel(8, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new CareerLevel(9, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT));
                LevelRow levelRow4 = new LevelRow(new CareerLevel(10, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new CareerLevel(11, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new CareerLevel(12, 0, LevelState.UNLOCK, 4, CardTheme.FLAG));
                LevelRow levelRow5 = new LevelRow(new CareerLevel(13, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new CareerLevel(14, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new CareerLevel(15, 0, LevelState.UNLOCK, 4, CardTheme.FLAG));
                LevelRow levelRow6 = new LevelRow(new CareerLevel(16, 0, LevelState.UNLOCK, 24, CardTheme.FRUTT), new CareerLevel(17, 2, LevelState.UNLOCK, 20, CardTheme.FLAG), new CareerLevel(18, 0, LevelState.LOCK, 4, CardTheme.FLAG));
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
                LevelRow levelRow = new LevelRow(new AgainstTimeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 30), new AgainstTimeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 9), new AgainstTimeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 12));
                LevelRow levelRow1 = new LevelRow(new AgainstTimeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 12), new AgainstTimeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 14), new AgainstTimeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 20));
                levelRowList.add(levelRow);
                levelRowList.add(levelRow1);

                List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
                GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.AGAINST_TIME);

                if(ApplicationConstants.needTutorialAgainsTime) {
                    Intent intent = new Intent(GameModeActivity.this, TutorialActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
                    return;
                }

                // TODO: Load current level from database
                GameInformation.getInstance().setNumCurrentLevel(1);
                Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
                intent.putExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL, String.valueOf(GameInformation.getInstance().getNumCurrentLevel()));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnSurvival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.SURVIVAL);

                List<LevelRow> levelRowList = new ArrayList<>();
                LevelRow levelRow = new LevelRow(new LifeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 3), new LifeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 3), new LifeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 3));
                LevelRow levelRow1 = new LevelRow(new LifeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 3), new LifeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 3), new LifeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 3));
                levelRowList.add(levelRow);
                levelRowList.add(levelRow1);

                List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
                GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SURVIVAL);

                if(ApplicationConstants.needTutorialSurvival) {
                    Intent intent = new Intent(GameModeActivity.this, TutorialActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
                    return;
                }

                // TODO: Load current level from database
                GameInformation.getInstance().setNumCurrentLevel(1);
                Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
                intent.putExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL, String.valueOf(GameInformation.getInstance().getNumCurrentLevel()));
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnSuddenDeath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.SUDDEN_DEATH);

                List<LevelRow> levelRowList = new ArrayList<>();
                LevelRow levelRow = new LevelRow(new LifeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 1), new LifeLevel(2, LevelState.UNLOCK, 6, CardTheme.FRUTT, 1), new LifeLevel(3, LevelState.UNLOCK, 8, CardTheme.FRUTT, 1));
                LevelRow levelRow1 = new LevelRow(new LifeLevel(4, LevelState.UNLOCK, 12, CardTheme.FRUTT, 1), new LifeLevel(5, LevelState.UNLOCK, 14, CardTheme.FRUTT, 1), new LifeLevel(6, LevelState.UNLOCK, 16, CardTheme.FRUTT, 1));
                levelRowList.add(levelRow);
                levelRowList.add(levelRow1);

                List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);
                GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SUDDEN_DEATH);

                if(ApplicationConstants.needTutorialSuddenDeath) {
                    Intent intent = new Intent(GameModeActivity.this, TutorialActivity.class);
                    startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
                    return;
                }

                // TODO: Load current level from database
                GameInformation.getInstance().setNumCurrentLevel(1);
                Intent intent = new Intent(GameModeActivity.this, GameActivity.class);
                intent.putExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL, String.valueOf(GameInformation.getInstance().getNumCurrentLevel()));
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
