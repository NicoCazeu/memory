package com.softcaze.memory.activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.softcaze.memory.util.FileUtils;

import java.io.IOException;
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
            dao.initCountAd();
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
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, ShopActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, ChallengeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getApplicationContext());
                Intent intent = new Intent(MainMenuActivity.this, SettingActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }

    private boolean launchTutorial(Dao database, GameMode mode) {
        try {
            database.open();
            GameInformation.getInstance().setCurrentMode(mode);
            if(database.needTutoByMode(mode)) {
                Intent intent = new Intent(MainMenuActivity.this, TutorialActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
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
        challengeList.add(new Challenge(1, AwardChallengeType.COIN, false, false, 30, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 10, this));
        challengeList.add(new Challenge(2, AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 20, this));
        challengeList.add(new Challenge(3, AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 50, this));
        challengeList.add(new Challenge(4, AwardChallengeType.COIN, false, false, 40, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 80, this));
        challengeList.add(new Challenge(5, AwardChallengeType.COIN, false, false, 30, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 100, this));
        challengeList.add(new Challenge(6, AwardChallengeType.COIN, false, false, 50, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 150, this));
        challengeList.add(new Challenge(7, AwardChallengeType.COIN, false, false, 100, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 200, this));
        challengeList.add(new Challenge(8, AwardChallengeType.COIN, false, false, 150, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 270, this));
        challengeList.add(new Challenge(9, AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 300, this));
        challengeList.add(new Challenge(10, AwardChallengeType.EYE_BONUS, false, false, 6, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 380, this));
        challengeList.add(new Challenge(11, AwardChallengeType.COIN, false, false, 80, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 420, this));
        challengeList.add(new Challenge(12, AwardChallengeType.COIN, false, false, 300, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 500, this));
        challengeList.add(new Challenge(13, AwardChallengeType.EYE_BONUS, false, false, 25, GameMode.CAREER, ChallengeType.GLOBAL_STAR, 550, this));

        challengeList.add(new Challenge(14, AwardChallengeType.COIN, false, false, 30, GameMode.CAREER, ChallengeType.END_LEVEL, 10, this));
        challengeList.add(new Challenge(15, AwardChallengeType.COIN, false, false, 50, GameMode.CAREER, ChallengeType.END_LEVEL, 30, this));
        challengeList.add(new Challenge(16,  AwardChallengeType.COIN, false, false, 80, GameMode.CAREER, ChallengeType.END_LEVEL, 50, this));
        challengeList.add(new Challenge(17,  AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.CAREER, ChallengeType.END_LEVEL, 100, this));
        challengeList.add(new Challenge(18,  AwardChallengeType.COIN, false, false, 200, GameMode.CAREER, ChallengeType.END_LEVEL, 125, this));
        challengeList.add(new Challenge(19,  AwardChallengeType.COIN, false, false, 240, GameMode.CAREER, ChallengeType.END_LEVEL, 150, this));
        challengeList.add(new Challenge(20,  AwardChallengeType.EYE_BONUS, false, false, 10, GameMode.CAREER, ChallengeType.END_LEVEL, 180, this));

        challengeList.add(new Challenge(21,  AwardChallengeType.COIN, false, false, 20, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 5, this));
        challengeList.add(new Challenge(22,  AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 15, this));
        challengeList.add(new Challenge(23,  AwardChallengeType.COIN, false, false, 25, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 32, this));
        challengeList.add(new Challenge(24,  AwardChallengeType.COIN, false, false, 40, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 55, this));
        challengeList.add(new Challenge(25,  AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 75, this));
        challengeList.add(new Challenge(26,  AwardChallengeType.EYE_BONUS, false, false, 10, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 118, this));
        challengeList.add(new Challenge(27,  AwardChallengeType.COIN, false, false, 200, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 180, this));
        challengeList.add(new Challenge(28,  AwardChallengeType.COIN, false, false, 250, GameMode.CAREER, ChallengeType.END_LEVEL_THREE_STARS, 177, this));

        challengeList.add(new Challenge(29,  AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 20, this));
        challengeList.add(new Challenge(30,  AwardChallengeType.COIN, false, false, 30, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 35, this));
        challengeList.add(new Challenge(31,  AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 60, this));
        challengeList.add(new Challenge(32,  AwardChallengeType.EYE_BONUS, false, false, 8, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 80, this));
        challengeList.add(new Challenge(33,  AwardChallengeType.COIN, false, false, 400, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 120, this));
        challengeList.add(new Challenge(34,  AwardChallengeType.COIN, false, false, 1000, GameMode.CAREER, ChallengeType.END_LEVEL_WITHOUT_BONUS, 160, this));

        challengeList.add(new Challenge(35,  AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 18, this));
        challengeList.add(new Challenge(36,  AwardChallengeType.COIN, false, false, 80, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 45, this));
        challengeList.add(new Challenge(37,  AwardChallengeType.EYE_BONUS, false, false, 7, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 65, this));
        challengeList.add(new Challenge(38,  AwardChallengeType.COIN, false, false, 400, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 95, this));
        challengeList.add(new Challenge(39,  AwardChallengeType.COIN, false, false, 1800, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 140, this));
        challengeList.add(new Challenge(40,  AwardChallengeType.EYE_BONUS, false, false, 40, GameMode.CAREER, ChallengeType.END_LEVEL_ONE_LIFE, 185, this));

        /**
         * Against time
         */
        challengeList.add(new Challenge(41, AwardChallengeType.COIN, false, false, 20, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 5, this));
        challengeList.add(new Challenge(42, AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 10, this));
        challengeList.add(new Challenge(43, AwardChallengeType.COIN, false, false, 80, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 20, this));
        challengeList.add(new Challenge(44, AwardChallengeType.COIN, false, false, 50, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 25, this));
        challengeList.add(new Challenge(45, AwardChallengeType.COIN, false, false, 80, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 40, this));
        challengeList.add(new Challenge(46, AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 50, this));
        challengeList.add(new Challenge(47, AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 70, this));
        challengeList.add(new Challenge(48, AwardChallengeType.COIN, false, false, 100, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 80, this));
        challengeList.add(new Challenge(49, AwardChallengeType.COIN, false, false, 100, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 100, this));
        challengeList.add(new Challenge(50, AwardChallengeType.COIN, false, false, 240, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 130, this));
        challengeList.add(new Challenge(51, AwardChallengeType.COIN, false, false, 280, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 155, this));
        challengeList.add(new Challenge(52, AwardChallengeType.EYE_BONUS, false, false, 30, GameMode.AGAINST_TIME, ChallengeType.END_LEVEL, 185, this));

        /**
         * Survival
         */
        challengeList.add(new Challenge(53, AwardChallengeType.COIN, false, false, 20, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 5, this));
        challengeList.add(new Challenge(54, AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 10, this));
        challengeList.add(new Challenge(55, AwardChallengeType.COIN, false, false, 80, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 20, this));
        challengeList.add(new Challenge(56, AwardChallengeType.COIN, false, false, 50, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 25, this));
        challengeList.add(new Challenge(57, AwardChallengeType.COIN, false, false, 80, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 40, this));
        challengeList.add(new Challenge(58, AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 50, this));
        challengeList.add(new Challenge(59, AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 70, this));
        challengeList.add(new Challenge(60, AwardChallengeType.COIN, false, false, 100, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 80, this));
        challengeList.add(new Challenge(61, AwardChallengeType.COIN, false, false, 100, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 100, this));
        challengeList.add(new Challenge(62, AwardChallengeType.COIN, false, false, 240, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 130, this));
        challengeList.add(new Challenge(63, AwardChallengeType.COIN, false, false, 280, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 155, this));
        challengeList.add(new Challenge(64, AwardChallengeType.EYE_BONUS, false, false, 30, GameMode.SURVIVAL, ChallengeType.END_LEVEL, 185, this));

        /**
         * Sudden death
         */
        challengeList.add(new Challenge(65, AwardChallengeType.COIN, false, false, 20, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 5, this));
        challengeList.add(new Challenge(66, AwardChallengeType.EYE_BONUS, false, false, 1, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 10, this));
        challengeList.add(new Challenge(67, AwardChallengeType.COIN, false, false, 80, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 20, this));
        challengeList.add(new Challenge(68, AwardChallengeType.COIN, false, false, 50, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 25, this));
        challengeList.add(new Challenge(69, AwardChallengeType.COIN, false, false, 80, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 40, this));
        challengeList.add(new Challenge(70, AwardChallengeType.EYE_BONUS, false, false, 2, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 50, this));
        challengeList.add(new Challenge(71, AwardChallengeType.EYE_BONUS, false, false, 5, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 70, this));
        challengeList.add(new Challenge(72, AwardChallengeType.COIN, false, false, 100, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 80, this));
        challengeList.add(new Challenge(73, AwardChallengeType.COIN, false, false, 100, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 100, this));
        challengeList.add(new Challenge(74, AwardChallengeType.COIN, false, false, 240, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 130, this));
        challengeList.add(new Challenge(75, AwardChallengeType.COIN, false, false, 280, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 155, this));
        challengeList.add(new Challenge(76, AwardChallengeType.EYE_BONUS, false, false, 30, GameMode.SUDDEN_DEATH, ChallengeType.END_LEVEL, 185, this));



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

    private LevelRow getLevelRowByFile(List<String> lines) {
        LevelRow levelRow = null;

        return levelRow;
    }

    private List<LevelRow> getListLevels(GameMode mode) {
        List<LevelRow> levelRowList = new ArrayList<>();
        final String DATA_SEPARATOR = ";";
        final String SCORE_LIMIT_SEPARATOR = ":";
        final int ID_INDEX = 0;
        final int COUNT_CARD_INDEX = 1;
        final int THEME_INDEX = 2;
        final int SCORE_LIMIT_INDEX = 3;
        final int TIME_SCORE_INDEX = 3;
        int nbrLife = 3;
        int indexFile = 0;

        switch (mode) {
            case CAREER:
                indexFile = R.raw.career_level;
                break;
            case AGAINST_TIME:
                indexFile = R.raw.against_time_level;
                break;
            case SURVIVAL:
                indexFile = R.raw.survival_level;
                nbrLife = 3;
                break;
            case SUDDEN_DEATH:
                indexFile = R.raw.sudden_death_level;
                nbrLife = 1;
                break;
        }

        try {
            List<List<String>> listLevels = FileUtils.readFile(this, indexFile);
            LevelRow levelsRow = new LevelRow();
            String[] datas = null;

            for(int i = 0; i < listLevels.size(); i++) {
                for(int j = 0; j < listLevels.get(i).size(); j++) {
                    String line = listLevels.get(i).get(j);
                    datas = line.split(DATA_SEPARATOR);

                    if(datas != null) {
                        int id = Integer.parseInt(datas[ID_INDEX]);
                        int countCard = Integer.parseInt(datas[COUNT_CARD_INDEX]);

                        switch (mode) {
                            case CAREER:
                                String[] scoreLimitStr = datas[SCORE_LIMIT_INDEX].split(SCORE_LIMIT_SEPARATOR);
                                List<Integer> scoreLimit = new ArrayList<>();

                                for(String str: scoreLimitStr) {
                                    scoreLimit.add(Integer.parseInt(str));
                                }

                                switch (j) {
                                    case 0:
                                        levelsRow.setLevel1(new CareerLevel(id, 0, LevelState.LOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), scoreLimit));
                                        break;
                                    case 1:
                                        levelsRow.setLevel2(new CareerLevel(id, 0, LevelState.LOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), scoreLimit));
                                        break;
                                    case 2:
                                        levelsRow.setLevel3(new CareerLevel(id, 0, LevelState.LOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), scoreLimit));
                                        break;
                                }
                                break;
                            case AGAINST_TIME:
                                int timeScore = Integer.parseInt(datas[TIME_SCORE_INDEX]);
                                switch (j) {
                                    case 0:
                                        levelsRow.setLevel1(new AgainstTimeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), timeScore));
                                        break;
                                    case 1:
                                        levelsRow.setLevel2(new AgainstTimeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), timeScore));
                                        break;
                                    case 2:
                                        levelsRow.setLevel3(new AgainstTimeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), timeScore));
                                        break;
                                }
                                break;
                            case SURVIVAL:
                            case SUDDEN_DEATH:
                                switch (j) {
                                    case 0:
                                        levelsRow.setLevel1(new LifeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), nbrLife));
                                        break;
                                    case 1:
                                        levelsRow.setLevel2(new LifeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), nbrLife));
                                        break;
                                    case 2:
                                        levelsRow.setLevel3(new LifeLevel(id, LevelState.UNLOCK, countCard, CardTheme.valueOf(datas[THEME_INDEX]), nbrLife));
                                        break;
                                }
                                break;
                        }
                    }
                }
                levelRowList.add(levelsRow);
                levelsRow = new LevelRow();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }


        return levelRowList;
    }

    private void initLevels() {
        /**
         * CAREER
         */
        List<Level> listLevel = GameInformation.getInstance().convertToLevelList(getListLevels(GameMode.CAREER));

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
         * AGAINST TIME
         */
        listLevel = GameInformation.getInstance().convertToLevelList(getListLevels(GameMode.AGAINST_TIME));
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.AGAINST_TIME);

        /**
         * Survival level
         */
        listLevel = GameInformation.getInstance().convertToLevelList(getListLevels(GameMode.SURVIVAL));
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SURVIVAL);

        /**
         * Sudden death level
         */
        listLevel = GameInformation.getInstance().convertToLevelList(getListLevels(GameMode.SUDDEN_DEATH));
        GameInformation.getInstance().setListLevelByGameMode(listLevel, GameMode.SUDDEN_DEATH);
    }

    @Override
    public void onBackPressed() {
    }
}
