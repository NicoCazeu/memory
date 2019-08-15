package com.softcaze.memory.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.activity.GameActivity;
import com.softcaze.memory.activity.GameModeActivity;
import com.softcaze.memory.activity.LevelListActivity;
import com.softcaze.memory.model.Level;
import com.softcaze.memory.model.LevelState;
import com.softcaze.memory.singleton.GameInformation;
import com.softcaze.memory.util.ApplicationConstants;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class EndLevelView extends RelativeLayout {
    RelativeLayout btnRevive, btnAgain, btnMenu, btnNext;
    List<ImageView> starList;
    TextView txtNumLevel, txtScore, txtBestScore;
    Level currentLevel;

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

        btnRevive = (RelativeLayout) findViewById(R.id.btn_revive);
        btnAgain = (RelativeLayout) findViewById(R.id.btn_again);
        btnMenu = (RelativeLayout) findViewById(R.id.btn_menu);
        btnNext = (RelativeLayout) findViewById(R.id.btn_next);

        txtNumLevel = (TextView) findViewById(R.id.txt_num_level_game);
        txtScore = (TextView) findViewById(R.id.score);
        txtBestScore = (TextView) findViewById(R.id.bestscore);

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

        switch (currentLevel.getScoreNumberStar(score)) {
            case 1:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(0).setAlpha(255);
                break;
            case 2:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(1).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).getBackground().setAlpha(100);
                break;
            case 3:
                starList.get(0).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(1).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(2).setBackground(getResources().getDrawable(R.drawable.star_score));
                starList.get(0).setAlpha(255);
                starList.get(1).setAlpha(255);
                starList.get(2).setAlpha(255);
                break;
            default:
                break;
        }

        currentLevel.resetScore();

        btnRevive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnAgain.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GameInformation.getInstance().setGoNextLevel(false);
                Intent intent = new Intent(getContext(), LevelListActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

        btnNext.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                GameInformation.getInstance().setGoNextLevel(true);
                Intent intent = new Intent(getContext(), GameActivity.class);
                getContext().startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) getContext()).toBundle());
            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // TODO: consider storing these as member variables to reduce
        // allocations per draw cycle.
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = getWidth() - paddingLeft - paddingRight;
        int contentHeight = getHeight() - paddingTop - paddingBottom;
    }
}
