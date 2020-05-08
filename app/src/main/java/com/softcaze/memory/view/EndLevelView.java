package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.softcaze.memory.R;
import com.softcaze.memory.listener.AdVideoRewardListener;
import com.softcaze.memory.listener.RatePopupListener;
import com.softcaze.memory.model.ChallengeType;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.LevelListActivity;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class EndLevelView extends RelativeLayout {
    protected RelativeLayout btnMoreCoin, btnAgain, btnMenu, btnNext, relativeContent, parent, rateView;
    protected LinearLayout linearEarnCoin;
    protected List<ImageView> starList;
    protected TextView txtNumLevel, txtScore, txtBestScore, txtEarnCoin, txtLabelLevel, moreCoinTxt, againTxt, nextTxt, labelBestScore, labelScore;
    protected ProgressBar loadingAdProgressBar;
    protected Level currentLevel;
    protected ImageView earnCoin, moreCoinImg;
    protected int counter = 0;
    protected Timer timer, timerLoadingAdReward;
    protected Dao dao;
    protected RewardedVideoAd rewardedVideoAd;
    protected AdVideoRewardListener adVideoRewardListener;
    protected int earnCoinAmount = 0;
    protected InterstitialAd interstitialAd;
    protected FirebaseAnalytics firebaseAnalytics;

    public EndLevelView(Context context, RewardedVideoAd videoAd, InterstitialAd interAd) {
        super(context);
        this.rewardedVideoAd = videoAd;
        this.interstitialAd = interAd;
        init(null, 0);
    }

    public EndLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EndLevelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.endlevel, this);

        firebaseAnalytics = FirebaseAnalytics.getInstance(getContext());

        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EndLevelView, defStyle, 0);

        starList = new ArrayList<>();

        starList.add((ImageView) findViewById(R.id.star1));
        starList.add((ImageView) findViewById(R.id.star2));
        starList.add((ImageView) findViewById(R.id.star3));

        btnMoreCoin = (RelativeLayout) findViewById(R.id.btn_more_coin);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);
        btnNext = (RelativeLayout) findViewById(R.id.btn_next);
        relativeContent = (RelativeLayout) findViewById(R.id.relative_content);
        parent = (RelativeLayout) findViewById(R.id.parent);

        loadingAdProgressBar = (ProgressBar) findViewById(R.id.loading_ad_progressbar);

        linearEarnCoin = (LinearLayout) findViewById(R.id.linear_earn_coin);

        txtNumLevel = (TextView) findViewById(R.id.txt_num_level_game);
        txtScore = (TextView) findViewById(R.id.score);
        txtBestScore = (TextView) findViewById(R.id.bestscore);
        txtEarnCoin = (TextView) findViewById(R.id.txt_earn_coin);
        txtLabelLevel = (TextView) findViewById(R.id.txt_label_level);
        moreCoinTxt = (TextView) findViewById(R.id.more_coin_txt);
        againTxt = (TextView) findViewById(R.id.again_txt);
        nextTxt = (TextView) findViewById(R.id.next_txt);
        labelBestScore = (TextView) findViewById(R.id.label_bestscore);
        labelScore = (TextView) findViewById(R.id.label_score);

        earnCoin = (ImageView) findViewById(R.id.earn_coin);
        moreCoinImg = (ImageView) findViewById(R.id.more_coin_img);

        UIUtil.setTypeFaceText(this.getContext(), txtScore, txtNumLevel, txtBestScore, txtEarnCoin, moreCoinTxt, againTxt, nextTxt, labelBestScore, labelScore, txtLabelLevel);

        rateView = new RateView(View.generateViewId(), getContext(), new RatePopupListener() {
            @Override
            public void closePopup() {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(ApplicationConstants.MY_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(ApplicationConstants.SHOW_NEVER, true);
                editor.apply();
                parent.removeView(rateView);
            }

            @Override
            public void clickLater() {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences(ApplicationConstants.MY_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(ApplicationConstants.SESSION_COUNT, 0);
                editor.apply();
                parent.removeView(rateView);
            }
        });

        if(displayedRatePopup(getContext())) {
            parent.addView(rateView);
        }

        this.setAdVideoRewardListener(new AdVideoRewardListener() {
            @Override
            public void adVideoReward() {
                btnMoreCoin.setVisibility(View.INVISIBLE);
                AnimationUtil.breathingAnimation(btnNext);

                counter = earnCoinAmount;
                earnCoinAmount *= 2;
                animateAddingCoin(earnCoinAmount, counter);

                try {
                    dao.open();
                    dao.setCoinUser(User.getInstance().getCoin().getAmount());
                } finally {
                    dao.close();
                }
            }
        });

        dao = new Dao(getContext());

        int numLevel = GameInformation.getInstance().getNumCurrentLevel();
        int nextLevel = GameInformation.getInstance().getNumNextLevel();

        /**
         * check challenge END LEVEL
         */
        try {
            dao.open();
            GameInformation.getInstance().checkChallengeDone(dao, getContext(), parent, ChallengeType.END_LEVEL, numLevel);

            if(dao.getCountAd() >= ApplicationConstants.COUNT_INTERSTITIAL_AD) {
                if(interstitialAd.isLoaded()) {
                    interstitialAd.show();
                    dao.setCountAd(0);
                }
            } else {
                dao.setCountAd(dao.getCountAd() + 1);
            }
        } finally {
            dao.close();
        }

        /** Rotate coin **/
        AnimationUtil.rotateCoin(earnCoin);

        currentLevel = GameInformation.getInstance().getLevelByNumAndGameMode(numLevel, GameInformation.getInstance().getCurrentMode());
        txtNumLevel.setText(String.valueOf(numLevel));

        int score = 0;
        int bestscore = 0;
        int numberStar = 0;
        int numberStarDatabase = 0;
        boolean usedBonus = currentLevel.isUsedBonus();
        boolean madeMistake = currentLevel.isMadeMistake();

        score = ((CareerLevel) currentLevel).getTouchUsed();
        numberStar = ((CareerLevel) currentLevel).getScoreNumberStar(score);

        try {
            dao.open();
            bestscore = dao.getBestScoreCareerLevel(numLevel);
            numberStarDatabase = dao.getNumberStarCareerLevel(numLevel);

            /**
             * check challenge END LEVEL THREE STARS
             */
            if(numberStar >= 3) {
                GameInformation.getInstance().checkChallengeDone(dao, getContext(), parent, ChallengeType.END_LEVEL_THREE_STARS, numLevel);
            }

            /**
             * check challenge END LEVEL WITHOUT BONUS
             */
            if(!usedBonus) {
                GameInformation.getInstance().checkChallengeDone(dao, getContext(), parent, ChallengeType.END_LEVEL_WITHOUT_BONUS, numLevel);
            }

            /**
             * check challenge END LEVEL ONE LIFE
             */
            if(!madeMistake) {
                GameInformation.getInstance().checkChallengeDone(dao, getContext(), parent, ChallengeType.END_LEVEL_ONE_LIFE, numLevel);
            }

            if(score < bestscore || bestscore == 0) {
                bestscore = score;
                dao.saveBestScoreCareerLevel(numLevel, bestscore);
            }

            if(numberStar > numberStarDatabase) {
                dao.saveNumberStarCareerLevel(numLevel, numberStar);
                GameInformation.getInstance().setOverallNumberStars(dao.getOverallNumberStars());

                /**
                 * check challenge GLOBAL STARS
                 */
                GameInformation.getInstance().checkChallengeDone(dao, getContext(), parent, ChallengeType.GLOBAL_STAR, GameInformation.getInstance().getOverallNumberStars());
            }

            if(numberStar > 0 && GameInformation.getInstance().hasNextLevel()) {
                dao.saveStateCareerLevel(nextLevel, LevelState.UNLOCK);
                GameInformation.getInstance().getLevelByNumAndGameMode(nextLevel, GameInformation.getInstance().getCurrentMode()).setState(LevelState.UNLOCK);
            }
        } finally {
            dao.close();
        }

        LevelState nextLevelState = GameInformation.getInstance().getLevelByNumAndGameMode(nextLevel, GameInformation.getInstance().getCurrentMode()).getState();
        if(!GameInformation.getInstance().hasNextLevel() || nextLevelState.equals(LevelState.LOCK)) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        txtScore.setText(String.valueOf(score));
        txtBestScore.setText(String.valueOf(bestscore));

        switch (numberStar) {
            case 1:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 1, this);
                starList.get(0).setAlpha(255);
                if(numberStarDatabase < 1) {
                    earnCoinAmount = 10;
                }
                break;
            case 2:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 2, this);
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).getBackground().setAlpha(100);
                if(numberStarDatabase == 0) {
                    earnCoinAmount = 20;
                } else if(numberStarDatabase == 1) {
                    earnCoinAmount = 10;
                }
                break;
            case 3:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 3, this);
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).setAlpha(255);
                if(numberStarDatabase == 0) {
                    earnCoinAmount = 30;
                } else if(numberStarDatabase == 1) {
                    earnCoinAmount = 20;
                } else if(numberStarDatabase == 2) {
                    earnCoinAmount = 10;
                }
                break;
            default:
                break;
        }

        if(earnCoinAmount == 0) {
            btnMoreCoin.setVisibility(View.INVISIBLE);
            AnimationUtil.breathingAnimation(btnNext);
        } else {
            AnimationUtil.breathingAnimation(btnMoreCoin);
        }

        Runnable runnableLoadingAdReward = new Runnable() {
            @Override
            public void run() {
                    EndLevelView self = EndLevelView.this;
                    try {
                        if(rewardedVideoAd.isLoaded()) {
                            moreCoinImg.setVisibility(View.VISIBLE);
                            loadingAdProgressBar.setVisibility(View.INVISIBLE);
                            self.timerLoadingAdReward.stopTimer();
                        } else {
                            moreCoinImg.setVisibility(View.INVISIBLE);
                            loadingAdProgressBar.setVisibility(View.VISIBLE);
                        }
                    } catch (Exception e) {
                        ;
                    }
            }
        };

        animateAddingCoin(earnCoinAmount, 0);

        timerLoadingAdReward = new Timer(runnableLoadingAdReward, 1000, true);

        try {
            dao.open();
            dao.setCoinUser(User.getInstance().getCoin().getAmount());
        } finally {
            dao.close();
        }

        if(firebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putLong(FirebaseAnalytics.Param.LEVEL, GameInformation.getInstance().getNumCurrentLevel());
            bundle.putString(ApplicationConstants.TAG_GAME_MODE, GameInformation.getInstance().getCurrentMode().name());
            bundle.putInt(ApplicationConstants.TAG_LEVEL_END_BESTSCORE, bestscore);
            bundle.putInt(ApplicationConstants.TAG_LEVEL_END_NUMBER_STAR, numberStar);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.LEVEL_END, bundle);
        }

        ((CareerLevel) currentLevel).resetScore();

        btnMoreCoin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateUsPopupOpened()) {
                    return;
                }
                AnimationUtil.btnClickedAnimation(view, getContext());

                if(firebaseAnalytics != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ApplicationConstants.TAG_CLICK_BUTTON_NAME, "moreCoinBtn");
                    firebaseAnalytics.logEvent(ApplicationConstants.TAG_CLICK_BUTTON, bundle);
                }

                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }
            }
        });

        btnAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateUsPopupOpened()) {
                    return;
                }
                AnimationUtil.btnClickedAnimation(view, getContext());

                if(firebaseAnalytics != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ApplicationConstants.TAG_CLICK_BUTTON_NAME, "againBtn");
                    firebaseAnalytics.logEvent(ApplicationConstants.TAG_CLICK_BUTTON, bundle);
                }

                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                ((Activity) getContext()).finish();
            }
        });

        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateUsPopupOpened()) {
                    return;
                }
                AnimationUtil.btnClickedAnimation(view, getContext());

                if(firebaseAnalytics != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ApplicationConstants.TAG_CLICK_BUTTON_NAME, "menuBtn");
                    firebaseAnalytics.logEvent(ApplicationConstants.TAG_CLICK_BUTTON, bundle);
                }

                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), LevelListActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                ((Activity) getContext()).finish();
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rateUsPopupOpened()) {
                    return;
                }
                AnimationUtil.btnClickedAnimation(view, getContext());

                if(firebaseAnalytics != null) {
                    Bundle bundle = new Bundle();
                    bundle.putString(ApplicationConstants.TAG_CLICK_BUTTON_NAME, "nextBtn");
                    firebaseAnalytics.logEvent(ApplicationConstants.TAG_CLICK_BUTTON, bundle);
                }

                GameInformation.getInstance().setGoNextLevel(true);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                ((Activity) getContext()).finish();
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    public AdVideoRewardListener getAdVideoRewardListener() {
        return adVideoRewardListener;
    }

    public void setAdVideoRewardListener(AdVideoRewardListener adVideoRewardListener) {
        this.adVideoRewardListener = adVideoRewardListener;
    }

    public void animateAddingCoin(final int earnCoinFixed, int earnCoinAlreadySaved) {
        Runnable runnableEarnCoin = new Runnable () {
            @Override
            public void run() {
                EndLevelView self = EndLevelView.this;
                int newValue = self.counter;
                txtEarnCoin.setText("+" + newValue);
                if (self.counter == earnCoinFixed) {
                    self.counter = 0;
                    self.timer.stopTimer();

                    linearEarnCoin.animate().scaleY(1.3F).scaleX(1.3F).setDuration(300).withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            linearEarnCoin.animate().scaleX(1F).scaleY(1F).setDuration(300);
                        }
                    });
                }
                self.counter++;
            }
        };

        timer = new Timer(runnableEarnCoin, 50, true);

        User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + (earnCoinFixed - earnCoinAlreadySaved));
    }

    public boolean displayedRatePopup(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ApplicationConstants.MY_PREFS, Context.MODE_PRIVATE);

        if(sharedPreferences.getBoolean(ApplicationConstants.SHOW_NEVER, false)) {
            return false;
        }

        int sessionCount = sharedPreferences.getInt(ApplicationConstants.SESSION_COUNT, 0);

        if(sessionCount >= ApplicationConstants.SESSION_OPEN_APP) {
            return true;
        } else {
            sessionCount++;

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(ApplicationConstants.SESSION_COUNT, sessionCount);
            editor.apply();
        }
        return false;
    }

    private boolean rateUsPopupOpened() {
        return (parent.findViewById(rateView.getId()) != null);
    }
}
