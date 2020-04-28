package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.model.AgainstTimeLevel;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Bonus;
import com.softcaze.memory.model.CardTheme;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.ChallengeType;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainMenuActivity extends Activity {

    protected ImageView btnPlay, btnShop, btnChallenge, btnSetting, imgCoin, title;
    protected RelativeLayout footer, header;
    protected LinearLayout linearCoin, linearBonus;
    protected Timer timerPlayBtn;
    protected TextView txtCoin, txtBonus;
    protected Dao dao;
    protected boolean playBtnAlreadyClicked = false;

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
        initChallenges();
        try {
            dao.open();

            dao.initUserDatabase();
            dao.initChallenges();
            dao.initLevelsDatabase();
            GameInformation.getInstance().setOverallNumberStars(dao.getOverallNumberStars());
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
        AnimationUtil.breathingAnimation(btnPlay);

        /** Manage animations **/
        AnimationUtil.playAnimation(this, R.anim.translate_from_top, title, header, linearCoin, linearBonus);
        AnimationUtil.playAnimation(this, R.anim.translate_from_left, btnPlay);
        AnimationUtil.playAnimation(this, R.anim.translate_from_bottom, footer, btnChallenge, btnSetting, btnShop);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnPlay.setEnabled(false);
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());

                if(launchTutorial(dao, GameMode.CAREER)) {
                    return;
                }
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

    private boolean launchTutorial(Dao database, GameMode mode) {
        try {
            database.open();
            GameInformation.getInstance().setCurrentMode(mode);
            if(database.needTutoByMode(mode)) {
                Intent intent = new Intent(MainMenuActivity.this, TutorialActivity.class);
                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(MainMenuActivity.this).toBundle());
                return true;
            }
        } finally {
            database.close();
        }
        return false;
    }

    private void initChallenges() {
        List<Challenge> challengeList = new ArrayList<>();
        /**
         * Career
         */
        challengeList.add(new Challenge(1,"Collectez 20 etoiles", AwardChallengeType.COIN, false, false, 50, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 20));
        challengeList.add(new Challenge(2,"Terminez le niveau 6", AwardChallengeType.COIN, false, false, 1, GameMode.CAREER, ChallengeType.END_LEVEL, 6));
        challengeList.add(new Challenge(3,"Terminez le niveau 2 avec 3 etoiles", AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 2));
        challengeList.add(new Challenge(4, "Terminez le niveau 3 sans utiliser de bonus", AwardChallengeType.COIN, false, false, 5, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 3));
        challengeList.add(new Challenge(5, "Terminez le niveau 1 sans erreur", AwardChallengeType.COIN, false, false, 5, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 1));

        /**
         * Against time
         */
        challengeList.add(new Challenge(6,"Terminez le niveau 4", AwardChallengeType.COIN, false, false, 1, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 4));

        /**
         * Survival
         */
        challengeList.add(new Challenge(7,"Terminez le niveau 1", AwardChallengeType.COIN, false, false, 1, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 1));

        /**
         * Sudden death
         */
        challengeList.add(new Challenge(8,"Terminez le niveau 20", AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 20));
        challengeList.add(new Challenge(9,"Terminez le niveau 30", AwardChallengeType.COIN, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 30));
        challengeList.add(new Challenge(10,"Terminez le niveau 50", AwardChallengeType.COIN, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 50));
        challengeList.add(new Challenge(11,"Terminez le niveau 60", AwardChallengeType.COIN, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 60));
        challengeList.add(new Challenge(12,"Terminez le niveau 80", AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 80));
        challengeList.add(new Challenge(13,"Terminez le niveau 100", AwardChallengeType.COIN, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 100));



        try {
            dao.open();
            for(Challenge challenge: challengeList) {
                challenge.setUnlockChallenge(dao.isUnlockChallenge(challenge.getId()));
                challenge.setGetAward(dao.getGetAwardChallenge(challenge.getId()));
            }
        } finally {
            dao.close();
        }

        GameInformation.getInstance().setChallenges(challengeList);
    }

    private void initLevels() {
        /**
         * Career levle
         */
        List<LevelRow> levelRowList = new ArrayList<>();
        LevelRow levelRow = new LevelRow(new CareerLevel(1, 0, LevelState.UNLOCK, 4, CardTheme.FRUTT), new CareerLevel(2, 0, LevelState.LOCK, 6, CardTheme.FRUTT), new CareerLevel(3, 0, LevelState.LOCK, 8, CardTheme.FRUTT));
        LevelRow levelRow2 = new LevelRow(new CareerLevel(4, 0, LevelState.LOCK, 10, CardTheme.FRUTT), new CareerLevel(5, 0, LevelState.LOCK, 16, CardTheme.FRUTT), new CareerLevel(6, 0, LevelState.LOCK, 4, CardTheme.FRUTT));
        LevelRow levelRow3 = new LevelRow(new CareerLevel(7, 0, LevelState.LOCK, 4, CardTheme.FRUTT), new CareerLevel(8, 0, LevelState.LOCK, 4, CardTheme.FLAG), new CareerLevel(9, 0, LevelState.LOCK, 4, CardTheme.FRUTT));
        LevelRow levelRow4 = new LevelRow(new CareerLevel(10, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(11, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(12, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        LevelRow levelRow5 = new LevelRow(new CareerLevel(13, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(14, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(15, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        LevelRow levelRow6 = new LevelRow(new CareerLevel(16, 0, LevelState.LOCK, 24, CardTheme.FRUTT), new CareerLevel(17, 0, LevelState.LOCK, 20, CardTheme.FLAG), new CareerLevel(18, 0, LevelState.LOCK, 4, CardTheme.FLAG));
        ((CareerLevel) levelRow.getLevel1()).setScoreLimit(Arrays.asList(new Integer[]{8,6,4}));
        ((CareerLevel) levelRow.getLevel2()).setScoreLimit(Arrays.asList(new Integer[]{10,8,6}));
        ((CareerLevel) levelRow.getLevel3()).setScoreLimit(Arrays.asList(new Integer[]{12, 10, 8}));
        ((CareerLevel) levelRow2.getLevel1()).setScoreLimit(Arrays.asList(new Integer[]{14, 12, 10}));
        ((CareerLevel) levelRow2.getLevel2()).setScoreLimit(Arrays.asList(new Integer[]{20, 18, 16}));
        ((CareerLevel) levelRow2.getLevel3()).setScoreLimit(Arrays.asList(new Integer[]{8, 6, 4}));
        ((CareerLevel) levelRow3.getLevel1()).setScoreLimit(Arrays.asList(new Integer[]{8, 6, 4}));
        ((CareerLevel) levelRow3.getLevel2()).setScoreLimit(Arrays.asList(new Integer[]{8, 6, 4}));
        ((CareerLevel) levelRow3.getLevel3()).setScoreLimit(Arrays.asList(new Integer[]{8, 6, 4}));

        levelRowList.add(levelRow);
        levelRowList.add(levelRow2);
        levelRowList.add(levelRow3);
        levelRowList.add(levelRow4);
        levelRowList.add(levelRow5);
        levelRowList.add(levelRow6);

        List<Level> listLevel = GameInformation.getInstance().convertToLevelList(levelRowList);

        try {
            dao.open();
            for(Level level: listLevel) {
                CareerLevel careerLevel = (CareerLevel) level;

                careerLevel.setState(dao.getStateCareerLevel(level.getId()));
                careerLevel.setTouchUsed(dao.getBestScoreCareerLevel(level.getId()));
                careerLevel.setNumberStar(dao.getNumberStarCareerLevel(level.getId()));
            }
        } finally {
            dao.close();
        }
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.CAREER);


        /**
         * Against time levle
         */
        levelRowList = new ArrayList<>();
        levelRow = new LevelRow(new AgainstTimeLevel(1, LevelState.UNLOCK, 4, CardTheme.FRUTT, 15), new AgainstTimeLevel(2, LevelState.UNLOCK, 4, CardTheme.FRUTT, 9), new AgainstTimeLevel(3, LevelState.UNLOCK, 4, CardTheme.FRUTT, 12));
        LevelRow levelRow1 = new LevelRow(new AgainstTimeLevel(4, LevelState.UNLOCK, 4, CardTheme.FRUTT, 12), new AgainstTimeLevel(5, LevelState.UNLOCK, 4, CardTheme.FRUTT, 14), new AgainstTimeLevel(6, LevelState.UNLOCK, 4, CardTheme.FRUTT, 20));
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
