package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.GameModeActivity;
import com.softcaze.memory.model.CareerLevel;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LifeLevel;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;

public class EndAllLevelsView extends RelativeLayout {
    RelativeLayout btnMoreCoin, btnAgain, btnMenu;
    Level currentLevel;
    Timer timerMoreCoin;

    public EndAllLevelsView(Context context) {
        super(context);
        init(null, 0);
    }

    public EndAllLevelsView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EndAllLevelsView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.end_all_level, this);

        btnMoreCoin = (RelativeLayout) findViewById(R.id.btn_more_coin);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);

        int numLevel = GameInformation.getInstance().getNumCurrentLevel();
        currentLevel = GameInformation.getInstance().getLevelByNumAndGameMode(numLevel, GameInformation.getInstance().getCurrentMode());

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

        if(currentLevel instanceof CareerLevel) {
            ((CareerLevel) currentLevel).resetScore();
        }

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
                Intent intent = new Intent(getContext(), GameModeActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });
    }
}
