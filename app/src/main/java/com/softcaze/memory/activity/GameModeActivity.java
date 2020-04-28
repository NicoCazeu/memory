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
import com.softcaze.memory.database.Dao;
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

public class GameModeActivity extends Activity {

    private TextView btnCareer, btnAgainstTime, btnSuddenDeath, btnSurvival, txtCoin, txtBonus;
    private ImageView imgCoin;
    protected Dao dao;

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

        dao = new Dao(this);

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

                /*if(launchTutorial(dao, GameMode.CAREER)){
                    return;
                }*/

                Intent intent = new Intent(GameModeActivity.this, LevelListActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(GameModeActivity.this).toBundle());
            }
        });

        btnAgainstTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                GameInformation.getInstance().setCurrentMode(GameMode.AGAINST_TIME);

                /*if(launchTutorial(dao, GameMode.AGAINST_TIME)) {
                    return;
                }*/

                dao.open();
                GameInformation.getInstance().setNumCurrentLevel(dao.getNumLevelByMode(GameMode.AGAINST_TIME_DATABASE));
                dao.close();
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

                /*if(launchTutorial(dao, GameMode.SURVIVAL)) {
                    return;
                }*/

                dao.open();
                GameInformation.getInstance().setNumCurrentLevel(dao.getNumLevelByMode(GameMode.SURVIVAL_DATABASE));
                dao.close();
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

                /*if(launchTutorial(dao, GameMode.SUDDEN_DEATH)) {
                    return;
                }*/

                dao.open();
                GameInformation.getInstance().setNumCurrentLevel(dao.getNumLevelByMode(GameMode.SUDDEN_DEATH_DATABASE));
                dao.close();
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
