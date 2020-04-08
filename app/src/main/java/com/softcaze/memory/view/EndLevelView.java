package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.LevelListActivity;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class EndLevelView extends RelativeLayout {
    RelativeLayout btnMoreCoin, btnAgain, btnMenu, btnNext;
    LinearLayout linearEarnCoin;
    List<ImageView> starList;
    TextView txtNumLevel, txtScore, txtBestScore, txtEarnCoin;
    Level currentLevel;
    ImageView earnCoin;
    int counter = 0;
    Timer timer, timerMoreCoin;

    public EndLevelView(Context context) {
        super(context);
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

        linearEarnCoin = (LinearLayout) findViewById(R.id.linear_earn_coin);

        txtNumLevel = (TextView) findViewById(R.id.txt_num_level_game);
        txtScore = (TextView) findViewById(R.id.score);
        txtBestScore = (TextView) findViewById(R.id.bestscore);
        txtEarnCoin = (TextView) findViewById(R.id.txt_earn_coin);

        earnCoin = (ImageView) findViewById(R.id.earn_coin);

        /** Rotate coin **/
        AnimationUtil.rotateCoin(earnCoin);

        Runnable moreCoinRunnable = new Runnable() {
            @Override
            public void run() {
                btnMoreCoin.animate().scaleX(.8F).scaleY(0.8F).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        btnMoreCoin.animate().scaleX(1F).scaleY(1F).setDuration(300);
                    }
                });
            }
        };

        timerMoreCoin = new Timer(moreCoinRunnable, 800, true);


        int numLevel = GameInformation.getInstance().getNumCurrentLevel();
        int nextLevel = GameInformation.getInstance().getNumNextLevel();

        currentLevel = GameInformation.getInstance().getLevelByNumAndGameMode(numLevel, GameInformation.getInstance().getCurrentMode());

        LevelState nextLevelState = GameInformation.getInstance().getLevelByNumAndGameMode(nextLevel, GameInformation.getInstance().getCurrentMode()).getState();

        txtNumLevel.setText(String.valueOf(numLevel));

        if(!GameInformation.getInstance().hasNextLevel() || nextLevelState.equals(LevelState.LOCK)) {
            btnNext.setVisibility(View.INVISIBLE);
        }

        Integer score = 0;
        switch (GameInformation.getInstance().getCurrentMode()) {
            case CAREER:
            case SURVIVAL:
                score = currentLevel.getScore().getTouchUsed();
                break;
            case AGAINST_TIME:
                //score = currentLevel.getScore().getTimeUsed();
                break;
            case SUDDEN_DEATH:
                break;
        }

        txtScore.setText(String.valueOf(score));
        // TODO BEST SCORE
        txtBestScore.setText(String.valueOf(score));
        int earnCoin = 0;

        switch (currentLevel.getScoreNumberStar(score)) {
            case 1:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 1, this);
                starList.get(0).setAlpha(255);
                earnCoin = 10;
                break;
            case 2:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 2, this);
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).getBackground().setAlpha(100);
                earnCoin = 20;
                break;
            case 3:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                AnimationUtil.animateStarsEndLevel(starList, 3, this);
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).setAlpha(255);
                earnCoin = 30;
                break;
            default:
                break;
        }

        /** TODO: change the value, is depending of the level and score **/
        final int earnCoinFixed = earnCoin;

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
        User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + earnCoinFixed);

        currentLevel.resetScore();

        btnMoreCoin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
            }
        });

        btnAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), LevelListActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
                GameInformation.getInstance().setGoNextLevel(true);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
