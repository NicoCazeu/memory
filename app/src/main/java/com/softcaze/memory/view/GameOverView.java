package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.GameModeActivity;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;

public class GameOverView extends RelativeLayout {
    TextView txtNumLevel;
    RelativeLayout btnMoreLife, btnAgain, btnMenu;
    Timer timerMoreCoin;

    public GameOverView(Context context) {
        super(context);
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

        btnMoreLife = (RelativeLayout) findViewById(R.id.btn_more_coin);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);

        txtNumLevel = (TextView) findViewById(R.id.txt_num_level_game);

        Runnable moreCoinRunnable = new Runnable() {
            @Override
            public void run() {
                btnMoreLife.animate().scaleX(.8F).scaleY(0.8F).setDuration(300).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        btnMoreLife.animate().scaleX(1F).scaleY(1F).setDuration(300);
                    }
                });
            }
        };

        timerMoreCoin = new Timer(moreCoinRunnable, 800, true);

        int numLevel = GameInformation.getInstance().getNumCurrentLevel();

        txtNumLevel.setText(String.valueOf(numLevel));

        btnMoreLife.setOnClickListener(new OnClickListener() {
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
