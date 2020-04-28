package com.softcaze.memory.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.softcaze.memory.R;
import com.softcaze.memory.database.Dao;
import com.softcaze.memory.listener.AdVideoRewardListener;
import com.softcaze.memory.model.AgainstTimeLevel;
import com.softcaze.memory.model.CardState;
import com.softcaze.memory.model.CardType;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.ChallengeType;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LifeLevel;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.MathUtil;
import com.softcaze.memory.view.CardView;
import com.softcaze.memory.view.EndAllLevelsView;
import com.softcaze.memory.view.EndLevelView;
import com.softcaze.memory.view.GameOverView;

import java.util.ArrayList;
import java.util.List;

public class GameActivity extends Activity implements RewardedVideoAdListener {

    protected TextView txtGameMode, txtNumLevel, txtBonus, txtCoin;
    protected Level currentLevel;
    protected int currentNumLevel = 0;
    protected LinearLayout gameContent, linearBonus;
    protected RelativeLayout relativeContentGame, gameHelper;
    protected EndLevelView endLevelView;
    protected GameOverView gameOverView;
    protected EndAllLevelsView endAllLevelsView;
    protected ImageView imgCoin;
    protected Dao dao;
    protected ObjectAnimator animation;
    protected RewardedVideoAd rewardedVideoAd;
    protected AdVideoRewardListener adVideoRewardListener;
    protected boolean canHaveAdVideoReward = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_game);
        GameInformation.getInstance().setCanPlay(false);

        txtGameMode = (TextView) findViewById(R.id.txt_game_mode);
        txtNumLevel = (TextView) findViewById(R.id.txt_num_level);
        txtBonus = (TextView) findViewById(R.id.txt_bonus);
        txtCoin = (TextView) findViewById(R.id.txt_coin);
        gameHelper = (RelativeLayout) findViewById(R.id.game_helper);

        gameContent = (LinearLayout) findViewById(R.id.game_content);
        linearBonus = (LinearLayout) findViewById(R.id.linear_bonus);

        relativeContentGame = (RelativeLayout) findViewById(R.id.relative_content_game);
        imgCoin = (ImageView) findViewById(R.id.img_coin);

        dao = new Dao(this);

        rewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        rewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        txtBonus.setText(User.getInstance().getBonus().getAmount() + "");
        txtCoin.setText(User.getInstance().getCoin().getAmount() + "");

        //currentNumLevel = Integer.valueOf(getIntent().getStringExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL));
        if(GameInformation.getInstance().goNextLevel() && GameInformation.getInstance().hasNextLevel()) {
            GameInformation.getInstance().setNumCurrentLevel(GameInformation.getInstance().getNumNextLevel());
        }

        currentNumLevel = GameInformation.getInstance().getNumCurrentLevel();

        if(!GameInformation.getInstance().getCurrentMode().equals(GameMode.CAREER)) {
            if(currentNumLevel > 1) {
                int previousNumLevel = currentNumLevel - 1;

                try {
                    dao.open();
                    /**
                     * check challenge END LEVEL
                     */
                    GameInformation.getInstance().checkChallengeDone(dao, this, relativeContentGame, ChallengeType.END_LEVEL, previousNumLevel);
                } finally {
                    dao.close();
                }
            }
        }

        GameInformation.getInstance().resetCards();

        if(GameInformation.getInstance().getCurrentMode() != null) {
            txtGameMode.setText(GameInformation.getInstance().getCurrentMode().toString(this));
        }
        txtNumLevel.setText(String.valueOf(currentNumLevel));

        if (currentNumLevel != 0) {
            currentLevel = GameInformation.getInstance().getLevelByNumAndGameMode(currentNumLevel, GameInformation.getInstance().getCurrentMode());
            currentLevel.setUsedBonus(false);
            currentLevel.setMadeMistake(false);

            if(currentLevel instanceof CareerLevel) {
                ((CareerLevel) currentLevel).resetScore();
            }

            buildContentGame(currentLevel);

            if (GameInformation.getInstance().getCardViews() != null && !GameInformation.getInstance().getCardViews().isEmpty()) {
                for (CardView card : GameInformation.getInstance().getCardViews()) {
                    card.flipCardOnVerso(3000);
                }
            }

            initialisationTxtGameHelper(GameInformation.getInstance().getCurrentMode());
        }

        // TODO

        linearBonus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(GameInformation.getInstance().isCanPlay()) {
                    int value = User.getInstance().getBonus().getAmount();

                    if (value > 0) {
                        GameInformation.getInstance().setCanPlay(false);
                        for (CardView card : GameInformation.getInstance().getCardViews()) {
                            card.eyesBonusAction(3000, txtBonus);
                        }
                        User.getInstance().getBonus().setAmount(value - 1);

                        currentLevel.setUsedBonus(true);

                        try {
                            dao.open();
                            dao.setBonusRevealUser(User.getInstance().getBonus().getAmount());
                        } finally {
                            dao.close();
                        }

                        /** Display value **/
                        txtBonus.setText("" + User.getInstance().getBonus().getAmount());
                    }
                }
            }
        });
    }

    private void buildContentGame(Level level) {
        int numberCard = level.getCountCard();
        int numberLane = 0;
        int elementByLane = 0;
        int elementLastLane = 0;
        int previousRelative = 0;
        int elements = 0;
        int numeroCard = 0;
        List<String> listCard = GameInformation.getInstance().getRandomListByNumberCard(numberCard, level.getTheme());

        if ((numberCard / 2) <= 5) {
            numberLane = 2;
            elementByLane = numberCard / 2;
        } else if (numberCard == 12) {
            numberLane = 3;
            elementByLane = numberCard / 3;
        } else if (numberCard / 2 > 5 && numberCard / 2 <= 10) {
            numberLane = 4;

            if (numberCard / 2 == 10) {
                elementByLane = 5;
            } else {
                elementByLane = 4;
            }

            elementLastLane = numberCard % 4;
        } else if (numberCard / 2 > 10) {
            numberLane = 5;
            elementByLane = 5;
            elementLastLane = numberCard % 5;
        }

        for (int i = 0; i < numberLane; i++) {
            RelativeLayout relativeLayout = new RelativeLayout(this);
            relativeLayout.setId(i);

            if (elementLastLane == 0 || i != (numberLane - 1)) {
                elements = elementByLane;
            } else {
                elements = elementLastLane;
            }

            int previousCard = 0;
            for (int j = 1; j <= elements; j++) {
                final CardView card = new CardView(this);
                card.setId((i * 10) + j);
                //card.setSaveImgCard(getResources().getDrawable(GameInformation.getInstance().getRandomDrawableByTheme(level.getTheme())));
                card.setType(CardType.valueOf(listCard.get(numeroCard)));
                numeroCard++;

                if (card.getCardState() != null) {
                    card.setImgCard(card.getSaveImgCard());
                }

                RelativeLayout.LayoutParams cardParams = new RelativeLayout.LayoutParams
                        ((int) getResources().getDimension(R.dimen.card_background_width), (int) getResources().getDimension(R.dimen.card_background_height));

                if (j > 1) {
                    cardParams.addRule(RelativeLayout.RIGHT_OF, (i * 10) + previousCard);
                }

                card.setLayoutParams(cardParams);

                card.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //if(GameInformation.getInstance().isCanPlay()) {
                            if (GameInformation.getInstance().getCardsFlip().size() <= 2) {
                                if (card.getCardState().equals(CardState.VERSO)) {
                                    if(currentLevel instanceof LifeLevel) {
                                        card.flipCard(CardState.RECTO, gameHelper);
                                    } else {
                                        card.flipCard(CardState.RECTO);
                                    }
                                } else {
                                    if(currentLevel instanceof LifeLevel) {
                                        int numberLife = gameHelper.getChildCount();
                                        ImageView imgGameHelper = (ImageView) gameHelper.getChildAt(numberLife-1);
                                        gameHelper.removeView(imgGameHelper);

                                        if(gameHelper.getChildCount() == 0) {
                                            Intent gameOver = new Intent(ApplicationConstants.INTENT_GAME_OVER_LEVEL);
                                            getApplicationContext().sendBroadcast(gameOver);
                                        }
                                    }
                                    card.flipCard(CardState.VERSO);
                                    GameInformation.getInstance().getCardsFlip().clear();
                                }

                                if(GameInformation.getInstance().isCanPlay()) {
                                    if (GameInformation.getInstance().getCurrentMode().equals(GameMode.CAREER) && card.getCardState().equals(CardState.VERSO)) {
                                        if (currentLevel instanceof CareerLevel) {
                                            int touchUsed = ((CareerLevel) currentLevel).getTouchUsed() + 1;
                                            ((CareerLevel) currentLevel).setTouchUsed(touchUsed);

                                            if (gameHelper.getChildCount() > 0) {
                                                TextView txtGameHelper = (TextView) gameHelper.getChildAt(0);
                                                if (touchUsed > 1) {
                                                    txtGameHelper.setText(touchUsed + " " + getResources().getString(R.string.label_game_hits));
                                                } else {
                                                    txtGameHelper.setText(touchUsed + " " + getResources().getString(R.string.label_game_hit));
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        //}
                    }
                });

                relativeLayout.addView(card);
                GameInformation.getInstance().addCardView(card);

                previousCard = j;
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            if (i > 0) {
                params.addRule(RelativeLayout.BELOW, previousRelative);
            }
            //params.addRule(RelativeLayout.CENTER_HORIZONTAL);
            relativeLayout.setGravity(Gravity.CENTER);
            gameContent.addView(relativeLayout);

            previousRelative = i;
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(ApplicationConstants.INTENT_END_LEVEL.equals(intent.getAction())) {
                if(currentLevel instanceof CareerLevel && GameInformation.getInstance().getCurrentMode().equals(GameMode.CAREER)) {
                    relativeContentGame.removeAllViews();
                    endLevelView = new EndLevelView(context, rewardedVideoAd);
                    adVideoRewardListener = endLevelView.getAdVideoRewardListener();
                    relativeContentGame.addView(endLevelView);
                } else {
                    /*if(currentLevel instanceof AgainstTimeLevel) {
                        if(gameHelper.getChildCount() > 0) {
                            ProgressBar progressBarHelper = (ProgressBar) gameHelper.getChildAt(0);
                            progressBarHelper.getAnimation().cancel();
                        }
                    }*/
                    if(currentLevel instanceof AgainstTimeLevel) {
                        animation.removeAllListeners();
                    }

                    GameInformation.getInstance().setNumCurrentLevel(GameInformation.getInstance().getNumCurrentLevel() + 1);

                    dao.open();

                    switch (GameInformation.getInstance().getCurrentMode()) {
                        case AGAINST_TIME:
                            dao.saveNumLevelByMode(GameInformation.getInstance().getNumCurrentLevel(), GameMode.AGAINST_TIME_DATABASE);
                            break;
                        case SURVIVAL:
                            dao.saveNumLevelByMode(GameInformation.getInstance().getNumCurrentLevel(), GameMode.SURVIVAL_DATABASE);
                            break;
                        case SUDDEN_DEATH:
                            dao.saveNumLevelByMode(GameInformation.getInstance().getNumCurrentLevel(), GameMode.SUDDEN_DEATH_DATABASE);
                            break;
                    }

                    dao.close();
                    Intent intentNextLevel = new Intent(GameActivity.this, GameActivity.class);
                    intentNextLevel.putExtra(ApplicationConstants.INTENT_GAME_NUM_LEVEL, String.valueOf(GameInformation.getInstance().getNumCurrentLevel()));
                    startActivity(intentNextLevel, ActivityOptions.makeSceneTransitionAnimation(GameActivity.this).toBundle());
                }
            } else if(ApplicationConstants.INTENT_END_ALL_LEVELS.equals(intent.getAction())) {
                relativeContentGame.removeAllViews();
                endAllLevelsView = new EndAllLevelsView(context);
                relativeContentGame.addView(endAllLevelsView);
            } else if(ApplicationConstants.INTENT_GAME_OVER_LEVEL.equals(intent.getAction())) {
                if(currentLevel instanceof AgainstTimeLevel) {
                    if(animation != null) {
                        animation.removeAllListeners();
                    }
                }
                //relativeContentGame.removeAllViews();
                gameOverView = new GameOverView(context, rewardedVideoAd, currentLevel);
                relativeContentGame.addView(gameOverView);
            }
        }
    };

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(ApplicationConstants.INTENT_END_LEVEL));
        registerReceiver(receiver, new IntentFilter(ApplicationConstants.INTENT_END_ALL_LEVELS));
        registerReceiver(receiver, new IntentFilter(ApplicationConstants.INTENT_GAME_OVER_LEVEL));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        super.onPause();
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void initialisationTxtGameHelper(GameMode mode) {
        switch (mode) {
            case CAREER:
                TextView txtHit = new TextView(this);
                txtHit.setTextColor(getResources().getColor(R.color.whiteColor));
                txtHit.setText("0 " + getResources().getString(R.string.label_game_hit));
                txtHit.setTextSize(20);
                txtHit.setTypeface(ResourcesCompat.getFont(this,    R.font.roof_runners_active));
                gameHelper.addView(txtHit);
                break;
            case AGAINST_TIME:
                RelativeLayout.LayoutParams lpAgainstTimeHourglass = new RelativeLayout.LayoutParams(MathUtil.dpToPx(25), MathUtil.dpToPx(25));
                RelativeLayout.LayoutParams lpAgainstTimeProgressBar = new RelativeLayout.LayoutParams(MathUtil.dpToPx(200), MathUtil.dpToPx(4));
                lpAgainstTimeProgressBar.topMargin = lpAgainstTimeHourglass.width/2;

                ImageView imgHourglass = new ImageView(this);
                imgHourglass.setId(View.generateViewId());
                imgHourglass.setImageResource(R.drawable.hourglass);
                imgHourglass.setLayoutParams(lpAgainstTimeHourglass);

                lpAgainstTimeProgressBar.addRule(RelativeLayout.RIGHT_OF, imgHourglass.getId());
                ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
                progressBar.setId(View.generateViewId());
                progressBar.setScaleY(2);
                lpAgainstTimeProgressBar.setMarginStart(MathUtil.dpToPx(-25/2));
                progressBar.setLayoutParams(lpAgainstTimeProgressBar);
                progressBar.setProgress(0);

                gameHelper.addView(progressBar);
                gameHelper.addView(imgHourglass);

                animation = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);

                if(currentLevel instanceof AgainstTimeLevel) {
                    animation.setDuration(((AgainstTimeLevel) currentLevel).getTimeScore() * 1000);
                }
                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) { }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        Intent gameOver = new Intent(ApplicationConstants.INTENT_GAME_OVER_LEVEL);
                        getApplicationContext().sendBroadcast(gameOver);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {
                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
                animation.start();

                break;
            case SUDDEN_DEATH:
                RelativeLayout.LayoutParams layoutParamsSuddenDeath = new RelativeLayout.LayoutParams(MathUtil.dpToPx(20), MathUtil.dpToPx(20));
                ImageView imgSuddenDeathHeart = new ImageView(this);
                imgSuddenDeathHeart.setImageResource(R.drawable.heart_pink);
                imgSuddenDeathHeart.setLayoutParams(layoutParamsSuddenDeath);
                gameHelper.addView(imgSuddenDeathHeart);
                break;
            case SURVIVAL:
                RelativeLayout.LayoutParams layoutParamsSurvival;
                List<ImageView> imgHeartList = new ArrayList<>();
                imgHeartList.add(new ImageView(this));
                imgHeartList.add(new ImageView(this));
                imgHeartList.add(new ImageView(this));

                for(int i = 0; i < imgHeartList.size(); i++) {
                    layoutParamsSurvival = new RelativeLayout.LayoutParams(MathUtil.dpToPx(20), MathUtil.dpToPx(20));
                    layoutParamsSurvival.setMarginEnd(MathUtil.dpToPx(5));
                    imgHeartList.get(i).setId(View.generateViewId());
                    if(i == 1) {
                        layoutParamsSurvival.addRule(RelativeLayout.RIGHT_OF, imgHeartList.get(0).getId());
                    } else if(i == 2) {
                        layoutParamsSurvival.addRule(RelativeLayout.RIGHT_OF, imgHeartList.get(1).getId());
                    }

                    imgHeartList.get(i).setLayoutParams(layoutParamsSurvival);
                    imgHeartList.get(i).setImageResource(R.drawable.heart_pink);
                    gameHelper.addView(imgHeartList.get(i));
                }

                break;
        }
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        canHaveAdVideoReward = false;
    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        if(canHaveAdVideoReward) {
            if(currentLevel instanceof LifeLevel) {
                ((LifeLevel) currentLevel).setHasAdVideoReward(true);
                relativeContentGame.removeView(gameOverView);

                RelativeLayout.LayoutParams layoutParamsSuddenDeath = new RelativeLayout.LayoutParams(MathUtil.dpToPx(20), MathUtil.dpToPx(20));
                ImageView imgSuddenDeathHeart = new ImageView(this);
                imgSuddenDeathHeart.setImageResource(R.drawable.heart_pink);
                imgSuddenDeathHeart.setLayoutParams(layoutParamsSuddenDeath);
                gameHelper.addView(imgSuddenDeathHeart);
            } else {
                if(adVideoRewardListener != null) {
                    adVideoRewardListener.adVideoReward();
                }
            }
        }
        loadRewardedVideoAd();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        canHaveAdVideoReward = true;
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {

    }

    private void loadRewardedVideoAd(){
        rewardedVideoAd.loadAd(ApplicationConstants.ID_AD_VIDEO_REWARD_TEST,
                new AdRequest.Builder().build());
    }
}
