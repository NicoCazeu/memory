package com.softcaze.memory.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.GameModeActivity;
import com.softcaze.memory.activity.LevelListActivity;
import com.softcaze.memory.listener.PauseActionListener;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.UIUtil;

public class PauseView extends RelativeLayout {
    protected TextView menuTxt, againTxt, continueTxt;
    protected RelativeLayout btnMenu, btnAgain, btnContinue;
    protected PauseActionListener pauseActionListener;

    public PauseView(Context context, PauseActionListener listener) {
        super(context);
        this.pauseActionListener = listener;
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.pause_view, this);

        menuTxt = (TextView) findViewById(R.id.menu_txt);
        againTxt = (TextView) findViewById(R.id.again_txt);
        continueTxt = (TextView) findViewById(R.id.continue_txt);

        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnContinue = (RelativeLayout) findViewById(R.id.btn_continue);

        UIUtil.setTypeFaceText(getContext(), menuTxt, againTxt, continueTxt);

        AnimationUtil.breathingAnimation(btnContinue);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());

                pauseActionListener.clickMenu();
            }
        });

        btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());

                pauseActionListener.clickAgain();
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnimationUtil.btnClickedAnimation(view, getContext());

                pauseActionListener.clickContinue();
            }
        });
    }
}
