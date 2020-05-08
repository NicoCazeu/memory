package com.softcaze.memory.util;

import com.softcaze.memory.R;

/**
 * Created by Nicolas on 21/06/2019.
 */

public class ApplicationConstants {
    public static final String INTENT_GAME_NUM_LEVEL = "INTENT_GAME_NUM_LEVEL";
    public static final String INTENT_END_LEVEL = "INTENT_END_LEVEL";
    public static final String INTENT_END_ALL_LEVELS = "INTENT_END_ALL_LEVELS";
    public static final String INTENT_GAME_OVER_LEVEL = "GAME_OVER_LEVEL";
    public static final String INTENT_SCORE_LEVEL = "INTENT_SCORE_LEVEL";
    public static final String INTENT_REWARD_MORE_COIN_DONE = "INTENT_REWARD_MORE_COIN_DONE";
    public static final String INTENT_REWARD_MORE_COIN_LOADED = "INTENT_REWARD_MORE_COIN_LOADED";
    public static final int VERSO_BACKGROUND_CARD = R.drawable.card_verso;
    public static final int RECTO_BACKGROUND_CARD = R.drawable.card_recto;
    public static final int AMOUNT_COIN_START = 0;
    public static final int AMOUNT_BONUS_START = 2;
    public static final boolean needTutorialCareer = false;
    public static final boolean needTutorialAgainsTime = false;
    public static final boolean needTutorialSuddenDeath = true;
    public static final boolean needTutorialSurvival = false;
    public static final int FLIP_CARD_DURATION_1 = 3000; // 4 6 8
    public static final int FLIP_CARD_DURATION_2 = 4000; // 10 12 14
    public static final int FLIP_CARD_DURATION_3 = 6000; // 16 18 20
    public static final int FLIP_CARD_DURATION_4 = 8000; // 22 24
    public static final int COUNT_INTERSTITIAL_AD = 4;

    /** Rating popup **/
    public static final int SESSION_OPEN_APP = 5;
    public static final String SHOW_NEVER = "show_never";
    public static final String SESSION_COUNT = "session_count";
    public static final String MY_PREFS = "RatingMemory";

    /** ADs **/
    public static final String ID_AD_MOBILE = "ca-app-pub-9468199307439621~4757497614";
    public static final String ID_AD_INTERSTITIAL = "ca-app-pub-9468199307439621/4621959766";
    public static final String ID_AD_VIDEO_REWARD_ONE_LIFE = "ca-app-pub-9468199307439621/5907121279";
    public static final String ID_AD_VIDEO_REWARD_MORE_COIN = "ca-app-pub-9468199307439621/9463222906";
    public static final String ID_AD_VIDEO_REWARD_SHOP_COINS = "ca-app-pub-9468199307439621/7792732791";

    public static final String ID_AD_BANER_TEST = "ca-app-pub-3940256099942544/6300978111";
    public static final String ID_AD_INTERSTITIAL_TEST = "ca-app-pub-3940256099942544/1033173712";
    public static final String ID_AD_VIDEO_REWARD_TEST = "ca-app-pub-3940256099942544/5224354917";

    public static final String IN_APP_BILLING_ITEM_1 = "android.item.coin.1";
    public static final String IN_APP_BILLING_ITEM_2 = "android.item.coin.2";
    public static final String IN_APP_BILLING_PURCHASED_TEST = "android.test.purchased";
    public static final int IN_APP_BILLING_ITEM_1_AMOUNT = 250;
    public static final int IN_APP_BILLING_ITEM_2_AMOUNT = 800;

    /** Tag Manager Google **/
    public static final String TAG_GAME_MODE = "gameMode";
    public static final String TAG_LEVEL_END_ALL = "levelEndAll";
    public static final String TAG_LEVEL_END_BESTSCORE = "bestScoreLevelEnd";
    public static final String TAG_LEVEL_END_NUMBER_STAR = "numberStarLevelEnd";
    public static final String TAG_LEVEL_GAME_OVER = "levelGameOver";
    public static final String TAG_CHALLENGE_EVENT = "challengeEvent";
    public static final String TAG_CHALLENGE_UNLOCK_NAME = "challengeUnlockName";
    public static final String TAG_CLICK_BUTTON = "clickButton";
    public static final String TAG_CLICK_BUTTON_NAME = "clickButtonName";
    public static final String TAG_CLICK_GET_CHALLENGE_AWARD = "getChallengeAward";
}
