package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.GameModeActivity;
import com.softcaze.memory.listener.AdVideoRewardListener;
import com.softcaze.memory.model.GameMode;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LifeLevel;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

public class GameOverView extends RelativeLayout {
    protected TextView txtNumLevel, txtLabelLevel, labelGameOver, extraLifeTxt, againTxt, menuTxt;
    protected RelativeLayout btnRevive, btnAgain, btnMenu;
    protected ImageView icoRevive;
    protected ProgressBar loadingAdProgressBar;
    protected RewardedVideoAd rewardedVideoAd;
    protected Timer timerLoadingAdReward;
    protected Level currentLevel;

    public GameOverView(Context context, RewardedVideoAd videoAd, Level level) {
        super(context);
        this.currentLevel = level;
        this.rewardedVideoAd = videoAd;
        init(null, 0);
    }

    public GameOverView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public GameOverView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.gameover, this);

        btnRevive = (RelativeLayout) findViewById(R.id.btn_revive);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);

        loadingAdProgressBar = (ProgressBar) findViewById(R.id.loading_ad_progressbar);
        icoRevive = (ImageView) findViewById(R.id.ico_revive);

        txtNumLevel = (TextView) findViewById(R.id.txt_num_level_game);
        txtLabelLevel = (TextView) findViewById(R.id.txt_label_level);
        labelGameOver = (TextView) findViewById(R.id.label_game_over);
        extraLifeTxt = (TextView) findViewById(R.id.extra_life_txt);
        againTxt = (TextView) findViewById(R.id.again_txt);
        menuTxt = (TextView) findViewById(R.id.menu_txt);

        UIUtil.setTypeFaceText(this.getContext(), txtNumLevel, txtLabelLevel, labelGameOver, extraLifeTxt, againTxt, menuTxt);

        /**
         * Loading Ad Video
         */
        Runnable runnableLoadingAdReward = new Runnable() {
            @Override
            public void run() {
                GameOverView self = GameOverView.this;
                try {
                    if(rewardedVideoAd.isLoaded()) {
                        icoRevive.setVisibility(View.VISIBLE);
                        loadingAdProgressBar.setVisibility(View.INVISIBLE);
                        self.timerLoadingAdReward.stopTimer();
                    } else {
                        icoRevive.setVisibility(View.INVISIBLE);
                        loadingAdProgressBar.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    ;
                }
            }
        };

        timerLoadingAdReward = new Timer(runnableLoadingAdReward, 1000, true);

        txtNumLevel.setText(String.valueOf(currentLevel.getId()));

        if (GameInformation.getInstance().getCurrentMode().equals(GameMode.AGAINST_TIME) || (currentLevel instanceof LifeLevel && ((LifeLevel) currentLevel).isHasAdVideoReward())) {
            btnRevive.setVisibility(View.INVISIBLE);
            AnimationUtil.breathingAnimation(btnAgain);
        } else {
            AnimationUtil.breathingAnimation(btnRevive);
        }

        btnRevive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());

                if (rewardedVideoAd.isLoaded()) {
                    rewardedVideoAd.show();
                }
            }
        });

        btnAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });

        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), GameModeActivity.class);
                getContext().startActivity(intent);
                ((Activity) getContext()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }
        });
    }
}
