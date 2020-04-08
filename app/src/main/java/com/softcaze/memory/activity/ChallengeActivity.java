package com.softcaze.memory.activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.softcaze.memory.R;
import com.softcaze.memory.adapter.ChallengeListAdapater;
import com.softcaze.memory.listener.ChallengeAnimationListener;
import com.softcaze.memory.model.Award;
import com.softcaze.memory.model.AwardChallengeType;
import com.softcaze.memory.model.Bonus;
import com.softcaze.memory.model.Challenge;
import com.softcaze.memory.model.Coin;
import com.softcaze.memory.model.Position;
import com.softcaze.memory.model.User;
import com.softcaze.memory.service.Timer;
import com.softcaze.memory.util.AnimationUtil;
import com.softcaze.memory.util.MathUtil;

import java.util.ArrayList;
import java.util.List;

public class ChallengeActivity extends AppCompatActivity implements ChallengeAnimationListener {
    public static boolean animGetAwardsFinished = true;
    protected ChallengeListAdapater challengeListAdapter;
    protected RecyclerView recyclerView;
    protected ImageView imgCoin, imgBonus;
    protected LinearLayout linearCoin, linearBonus;
    protected RelativeLayout challengeBackground;
    protected TextView textCoin, textBonus;
    protected int counter;
    Timer timer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgCoin = (ImageView) findViewById(R.id.img_coin);
        imgBonus = (ImageView) findViewById(R.id.img_bonus);
        linearCoin = (LinearLayout) findViewById(R.id.linear_coin);
        linearBonus = (LinearLayout) findViewById(R.id.linear_bonus);
        challengeBackground = (RelativeLayout) findViewById(R.id.challenge_background);
        textCoin = (TextView) findViewById(R.id.txt_coin);
        textBonus = (TextView) findViewById(R.id.txt_bonus);

        /** Start animation coin **/
        AnimationUtil.rotateCoin(imgCoin);

        /** Init text field **/
        textCoin.setText(User.getInstance().getCoin().getAmount() + "");
        textBonus.setText(User.getInstance().getBonus().getAmount() + "");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        List<Challenge> challengeList = new ArrayList<>();
        challengeList.add(new Challenge("Collectez 80 etoiles", AwardChallengeType.COIN, true, false, 50));
        challengeList.add(new Challenge("Terminez le niveau 150 en une seul tentative", AwardChallengeType.COIN, true, false, 1));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.EYE_BONUS, false, true, 1));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.EYE_BONUS, true, false, 2));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 35));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.EYE_BONUS, true, false, 15));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));
        challengeList.add(new Challenge("Terminez le niveau 150 avec 3 etoiles", AwardChallengeType.COIN, true, false, 40));

        challengeListAdapter = new ChallengeListAdapater(challengeList, this);

        recyclerView.setAdapter(challengeListAdapter);

        imgCoin.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgCoin.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] locations = new int[2];
                imgCoin.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                User.getInstance().getCoin().setX(x);
                User.getInstance().getCoin().setY(y);
            }
        });

        imgBonus.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                imgBonus.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                int[] locations = new int[2];
                imgBonus.getLocationOnScreen(locations);
                int x = locations[0];
                int y = locations[1];
                User.getInstance().getBonus().setX(x);
                User.getInstance().getBonus().setY(y);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ChallengeActivity.this, MainMenuActivity.class);
        startActivity(intent, ActivityOptions.makeSceneTransitionAnimation((Activity) this).toBundle());
    }

    /**
     * This method is called when user get awards
     * @param award
     */
    @Override
    public void startAnimation(final Award award) {
        setAnimGetAwardsFinished(false);
        final ImageView imgAward = new ImageView(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MathUtil.dpToPx(20), MathUtil.dpToPx(20));

        Log.i("NICOLAS", "Y : " + award.getY());
        params.leftMargin = award.getX();
        params.topMargin = award.getY() - params.height - recyclerView.computeVerticalScrollOffset();

        Log.i("NICOLAS", "OFFSET : " + recyclerView.computeVerticalScrollOffset());

        Position positionAward = new Position();
        int amountGeneric = 0;

        if(award.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
            positionAward.setX(User.getInstance().getBonus().getX());
            positionAward.setY(User.getInstance().getBonus().getY());
            amountGeneric = User.getInstance().getBonus().getAmount();
            imgAward.setImageDrawable(getDrawable(R.drawable.eyes_bonus));
        } else {
            positionAward.setX(User.getInstance().getCoin().getX());
            positionAward.setY(User.getInstance().getCoin().getY());
            amountGeneric = User.getInstance().getCoin().getAmount();
            imgAward.setImageDrawable(getDrawable(R.drawable.coin));
        }

        challengeBackground.addView(imgAward, params);

        imgAward.animate().translationYBy(positionAward.getY() - award.getY())
                .translationXBy(positionAward.getX() - award.getX())
                .setDuration(300)
                .withEndAction(new Runnable() {
            @Override
            public void run() {
                challengeBackground.removeView(imgAward);
            }
        });

        final int amount = amountGeneric;

        Runnable runnableEyesBonus = new Runnable () {
            @Override
            public void run() {
                ChallengeActivity self = ChallengeActivity.this;
                self.counter++;
                int newValue = amount + self.counter;
                textBonus.setText(newValue + "");
                if (self.counter == award.getAmount()) {
                    self.counter = 0;
                    self.timer.stopTimer();
                    setAnimGetAwardsFinished(true);
                }
            }
        };

        Runnable runnableCoin = new Runnable () {
            @Override
            public void run() {
                ChallengeActivity self = ChallengeActivity.this;
                self.counter++;
                int newValue = amount + self.counter;
                textCoin.setText(newValue + "");
                if (self.counter == award.getAmount()) {
                    self.counter = 0;
                    self.timer.stopTimer();
                    setAnimGetAwardsFinished(true);
                }
            }
        };

        if(award.getAwardChallengeType().equals(AwardChallengeType.EYE_BONUS)) {
            timer = new Timer(runnableEyesBonus, 50, true);
            User.getInstance().getBonus().setAmount(User.getInstance().getBonus().getAmount() + award.getAmount());
        } else {
            timer = new Timer(runnableCoin, 50, true);
            User.getInstance().getCoin().setAmount(User.getInstance().getCoin().getAmount() + award.getAmount());
        }

    }

    public static boolean isAnimGetAwardsFinished() {
        return animGetAwardsFinished;
    }

    public static void setAnimGetAwardsFinished(boolean animGetAwardsFinished) {
        ChallengeActivity.animGetAwardsFinished = animGetAwardsFinished;
    }
}
