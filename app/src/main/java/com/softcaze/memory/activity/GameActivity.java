package com.softcaze.memory.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.model.CardState;
import com.softcaze.memory.model.CardType;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelScore;
import com.softcaze.memory.model.User;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.ApplicationConstants;
import com.softcaze.memory.util.MathUtil;
import com.softcaze.memory.view.CardView;
import com.softcaze.memory.view.EndLevelView;

import java.util.List;

public class GameActivity extends AppCompatActivity {

    protected TextView txtGameMode, txtNumLevel, txtBonus, txtCoin;
    protected Level currentLevel;
    protected int currentNumLevel = 0;
    protected LinearLayout gameContent, linearBonus;
    protected RelativeLayout relativeContentGame, gameHelper;
    protected EndLevelView endLevelView;
    protected ImageView imgCoin;

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

        initialisationTxtGameHelper(GameInformation.getInstance().getCurrentMode());

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

        GameInformation.getInstance().resetCards();

        if(GameInformation.getInstance().getCurrentMode() != null) {
            txtGameMode.setText(GameInformation.getInstance().getCurrentMode().toString(this));
        }
        txtNumLevel.setText(String.valueOf(currentNumLevel));

        if (currentNumLevel != 0) {
            currentLevel = GameInformation.getInstance().getLevelByNumAndGameMode(currentNumLevel, GameInformation.getInstance().getCurrentMode());

            buildContentGame(currentLevel);

            if (GameInformation.getInstance().getCardViews() != null && !GameInformation.getInstance().getCardViews().isEmpty()) {
                for (CardView card : GameInformation.getInstance().getCardViews()) {
                    card.flipCardOnVerso(3000);
                }
            }
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
                        if(GameInformation.getInstance().isCanPlay()) {
                            if (GameInformation.getInstance().getCardsFlip().size() <= 2) {
                                if (card.getCardState().equals(CardState.VERSO)) {
                                    card.flipCard(CardState.RECTO);
                                } else {
                                    card.flipCard(CardState.VERSO);
                                }

                                // TODO : add score;
                                LevelScore levelScore = new LevelScore();
                                levelScore.setTouchUsed(currentLevel.getScore().getTouchUsed() + 1);
                                currentLevel.setScore(levelScore);

                                if(GameInformation.getInstance().getCurrentMode().equals(GameMode.CAREER)) {
                                    if(gameHelper.getChildCount() > 0) {
                                        TextView txtGameHelper = (TextView) gameHelper.getChildAt(0);
                                        if (currentLevel.getScore().getTouchUsed() > 1) {

                                            txtGameHelper.setText(currentLevel.getScore().getTouchUsed() + " " + getResources().getString(R.string.label_game_hits));
                                        } else {
                                            txtGameHelper.setText(currentLevel.getScore().getTouchUsed() + " " + getResources().getString(R.string.label_game_hit));
                                        }
                                    }
                                }
                            }
                        }
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
            relativeContentGame.removeAllViews();
            endLevelView = new EndLevelView(context);
            relativeContentGame.addView(endLevelView);
        }
        }
    };

    @Override
    protected void onResume() {
        registerReceiver(receiver, new IntentFilter(ApplicationConstants.INTENT_END_LEVEL));
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
                ProgressBar progressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);

                gameHelper.addView(progressBar);
                progressBar.setProgress(0);
                progressBar.getLayoutParams().width = MathUtil.dpToPx(200);

                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 100, 0);
                animation.setDuration(10000);
                //animation.setInterpolator(new DecelerateInterpolator());
                animation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) { }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        //do something when the countdown is complete
                        relativeContentGame.removeAllViews();
                        EndLevelView endLevelView = new EndLevelView(relativeContentGame.getContext());
                        relativeContentGame.addView(endLevelView);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) { }

                    @Override
                    public void onAnimationRepeat(Animator animator) { }
                });
                animation.start();

                break;
            case SUDDEN_DEATH:
                break;
            case SURVIVAL:
                break;
        }
    }
}
